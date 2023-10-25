package edu.kit.kastel.sdq.transitivereduction;

import java.util.Set;

public class ValidRelation<T> {

	Set<T> fromCombinedLevel;
	Set<Set<T>> toCombinedLevels;
	
	public ValidRelation(Set<T> fromCombinedLevel, Set<Set<T>> toCombinedLevels) {
		this.fromCombinedLevel = fromCombinedLevel;
		this.toCombinedLevels = toCombinedLevels;
	}
	
	public Set<T> getFrom() {
		return this.fromCombinedLevel;
	}
	
	public Set<Set<T>> getTo() {
		return this.toCombinedLevels;
	}
}
