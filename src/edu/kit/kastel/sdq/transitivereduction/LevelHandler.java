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

	// 1) collect all single levels and call: initializeLevels();
	// [https://www.baeldung.com/java-power-set-of-a-set]
	// 2) replace a combinedLevel with a List<Boolean> referring to the single level
	// to index map
	// 3) sort the combinedLevels by their #n numberOfTrue or size of singleLevels
	// included
	// 4) this order should be stored in a treeSet
	// [https://www.baeldung.com/java-tree-set]
	// 5) get the transitive reduction: iterateReverse from highest to lowest #n
	// each time: compare to all #n+1 and ifSubsetOf() store the edge in graph
	// each time: compare to all #n+2, #n+3... if those are not already transitive
	// reachable by already found edges
	// 6) return the found edges (by unmapping the binary representation)
	// 7) AND, OR connectors of CombinedLevels define the direction of the edges
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
	
	/**
	 * Calculates the valid relations/edges of a transitive reduced lattice/graph of (Combined)levels.
	 * Default case is the assumption of disjunctive linked levels inside a combined level.
	 * In case of a conjunctive combination use getTransitiveReduction(false);
	 * @return the relations as ValidRelation objects
	 */
	public List<ValidRelation<T>> getTransitiveReduction() {
		return this.getTransitiveReduction(true);
	}

	/**
	 * The relation can be calculated for a conjunctive or a disjunctive representation of combined levels.
	 * @param disjunctive boolean where true triggers disjunctive (OR), false triggers conjunctive (AND)
	 * @return the valid relations of a transitive reduced lattice of levels
	 */
	public List<ValidRelation<T>> getTransitiveReduction(boolean disjunctive) {
		List<ValidRelation<T>> relations = new ArrayList<ValidRelation<T>>();

		// trigger successor calculation
		this.findDirectSuccessorsOfCombinedLevelsIterative();

		// create edges. AND/OR define the direction of the ValidRelations
		for (int i = 0; i < this.numberOfSingleLevels; i++) {
			if (cardinalityOrderedCombinedLevels.containsKey(i)) {
				for (CombinedLevel cLevelFrom : cardinalityOrderedCombinedLevels.get(i)) {
					if (!cLevelFrom.getDirectSuccessors().isEmpty()) {
						for(CombinedLevel cLevelTo : cLevelFrom.getDirectSuccessors()) {
							relations.add(new ValidRelation<T>(
									this.unMapBinary((disjunctive)? cLevelFrom : cLevelTo),
									this.unMapBinary((disjunctive)? cLevelTo : cLevelFrom)));
						}
					}
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
		Set<T> result = new HashSet<T>();
		BitSet bitSet = cLevel.getCopyOfIncludedLevelsBinaryRepresentation();
		for (int i = 0; i < bitSet.length(); i++) {
			if (bitSet.get(i)) {
				result.add(reverseMap.get(i));
			}
		}
		return result;
	}
}
