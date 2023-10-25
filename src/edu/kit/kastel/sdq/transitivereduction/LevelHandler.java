package edu.kit.kastel.sdq.transitivereduction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class LevelHandler<T> {

	private Map<T, Integer> levelToIndexMap = new HashMap<T, Integer>(); // e.g. CreditCardInfo --> 1
	private List<T> reverseMap = new ArrayList<>(); // e.g. [1] = CreditCardInfo
	private int numberOfSingleLevels;
	
	private TreeSet<CombinedLevel> combinedLevels;

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
		this.createCombinedLevel(singleLevels);
		combinedLevels.add(new CombinedLevel());
	}
	
	
	private void createCombinedLevel(Collection<T> singleLevels) {
		// 1) new BitSet b with b.set(index) for each singlelevel
		// 2) override greaterThan() with cardinality
		// 3) if not already exists create new CombinedLevel and insert in treeset
		
	}

	private Set<Set<T>> unMapBinary(Collection<List<Boolean>> sets) {
	    Set<Set<T>> ret = new HashSet<>();
	    for (List<Boolean> s : sets) {
	        HashSet<T> subset = new HashSet<>();
	        for (int i = 0; i < s.size(); i++) {
	            if (s.get(i)) {
	                subset.add(reverseMap.get(i));
	            }
	        }
	        ret.add(subset);
	    }
	    return ret;
	}
}
