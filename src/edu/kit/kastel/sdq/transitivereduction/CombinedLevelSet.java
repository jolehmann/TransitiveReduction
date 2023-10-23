package edu.kit.kastel.sdq.transitivereduction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CombinedLevelSet<T> {

	private Map<T, Integer> map = new HashMap<T, Integer>(); 	// e.g. CreditCardInfo --> 1
	private List<T> reverseMap = new ArrayList<>();				// e.g. [1] = CreditCardInfo

	
	/**
	 * Init with all separate values of Levels
	 */
	private void initializeLevels(Collection<T> collection) {
	    int mapId = 0;
	    for (T c : collection) {
	        map.put(c, mapId++);
	        reverseMap.add(c);
	    }
	}
	
	/**
	 * Init with all appearing sets of combined Levels
	 */
	public void initializeCombinedLevels(Collection<List<T>> combinedLevels) {
		// 1) collect all single levels and call: initializeLevels(); [https://www.baeldung.com/java-power-set-of-a-set]
		// 2) replace a combinedLevel with a List<Boolean> referring to the single level to index map
		// 3) sort the combinedLevels by their #n numberOfTrue or size of singleLevels included
		// 4) this order should be stored in a treeSet [https://www.baeldung.com/java-tree-set]
		// 5) get the transitive reduction: iterateReverse from highest to lowest #n
		//    each time: compare to all #n+1 and ifSubsetOf() store the edge in graph
		//    each time: compare to all #n+2, #n+3... if those are not already transitive reachable by already found edges
		// 6) return the found edges (by unmapping the binary representation)
		// 7) AND, OR connectors of CombinedLevels define the direction of the edges
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
