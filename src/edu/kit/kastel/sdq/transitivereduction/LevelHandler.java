package edu.kit.kastel.sdq.transitivereduction;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LevelHandler<T> {

	private Map<T, Integer> levelToIndexMap = new HashMap<T, Integer>(); // e.g. CreditCardInfo --> 1
	private List<T> reverseMap = new ArrayList<>(); // e.g. [1] = CreditCardInfo
	private int numberOfSingleLevels;

	private Map<Integer, Set<CombinedLevel>> cardinalityOrderedCombinedLevels = new HashMap<Integer, Set<CombinedLevel>>();

	public LevelHandler() {
		numberOfSingleLevels = 0;
	}

	public LevelHandler(Collection<T> singleLevels) {
		numberOfSingleLevels = 0;
		this.registerNewSingleLevels(singleLevels);
	}

	public void registerNewSingleLevels(Collection<T> singleLevels) {
		for (T level : singleLevels) {
			this.registerNewSingleLevel(level);
		}
	}

	public void registerNewSingleLevel(T level) {
		if (!levelToIndexMap.containsKey(level)) {
			levelToIndexMap.put(level, numberOfSingleLevels++);
			reverseMap.add(level);
		}
	}

	public void insertCombinedLevel(Collection<T> singleLevels) {
		this.registerNewSingleLevels(singleLevels);

		CombinedLevel cLevel = this.createCombinedLevel(singleLevels);

		Integer cardinality = cLevel.getCardinality();

		if (!cardinalityOrderedCombinedLevels.containsKey(cardinality)) {
			cardinalityOrderedCombinedLevels.put(cardinality, new HashSet<CombinedLevel>());
		}

		Set<CombinedLevel> combinedLevels = cardinalityOrderedCombinedLevels.get(cardinality);

		combinedLevels.add(cLevel);
	}

	private CombinedLevel createCombinedLevel(Collection<T> singleLevels) {
		BitSet includedLevelsBinaryRepresentation = new BitSet(numberOfSingleLevels);
		for (T level : singleLevels) {
			includedLevelsBinaryRepresentation.set(levelToIndexMap.get(level));
		}
		CombinedLevel cLevel = new CombinedLevel(includedLevelsBinaryRepresentation);

		return cLevel;
	}

	public List<ValidRelation<T>> getTransitiveReduction() {
		List<ValidRelation<T>> relations = new ArrayList<ValidRelation<T>>();

		// trigger successor calculation
		this.findDirectSuccessorsOfCombinedLevelsIterative();

		// create edges. AND/OR define the direction of the ValidRelations
		// TODO switch case
		for (int i = 0; i < this.numberOfSingleLevels; i++) {
			if (cardinalityOrderedCombinedLevels.containsKey(i)) {
				for (CombinedLevel cLevel : cardinalityOrderedCombinedLevels.get(i)) {
					relations.add(new ValidRelation<T>(
							this.unMapBinary(cLevel),
							this.unMapBinary(cLevel.getDirectSuccessors())));
				}
			}
		}

		return relations;
	}

	private void findDirectSuccessorsOfCombinedLevelsIterative() {
		// the superset element has no successors --> start with cardinality - 1
		// iterate downwards
		for (int i = this.numberOfSingleLevels - 1; i >= 0; i--) {
			if (cardinalityOrderedCombinedLevels.containsKey(i)) {
				for (CombinedLevel cLevel : cardinalityOrderedCombinedLevels.get(i)) {
					this.findDirectSuccessorsOfCombinedLevel(cLevel);
				}
			}
		}
	}

	private void findDirectSuccessorsOfCombinedLevel(CombinedLevel cLevel) {
		Set<CombinedLevel> alreadyReachable = new HashSet<CombinedLevel>();
		int startingCardinality = cLevel.getCardinality() + 1;
		
		for (int j = startingCardinality; j < this.numberOfSingleLevels; j++) {
			if (cardinalityOrderedCombinedLevels.containsKey(j)) {
				for (CombinedLevel nextTestingLevel : cardinalityOrderedCombinedLevels.get(j)) {
					if ((!alreadyReachable.contains(nextTestingLevel)) && cLevel.isSubsetOf(nextTestingLevel)) {
						cLevel.addSuccessor(nextTestingLevel);
						alreadyReachable.add(nextTestingLevel);
						alreadyReachable.addAll(nextTestingLevel.getAllSuccessors());
					}
				}
			}
		}

	}
	
	private Set<T> unMapBinary(CombinedLevel cLevel) {
		Set <T> result = new HashSet<T> ();
		BitSet bitSet = cLevel.getCopyOfIncludedLevelsBinaryRepresentation();
		for (int i = 0; i < bitSet.length(); i++) {
			if (bitSet.get(i)) {
				result.add(reverseMap.get(i));
			}
		}
		return result;
	}
	
	private Set<Set<T>> unMapBinary(Set<CombinedLevel> cLevels) {
		Set <Set<T>> result = new HashSet<Set<T>> ();
		
		for (CombinedLevel cLevel : cLevels) {
			result.add(unMapBinary(cLevel));
		}
		return result;
	}
}
