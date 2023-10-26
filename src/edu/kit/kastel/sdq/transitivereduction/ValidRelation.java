package edu.kit.kastel.sdq.transitivereduction;

import java.util.Set;

public class ValidRelation<T> {

	Set<T> fromCombinedLevel;
	Set<T> toCombinedLevel;
	
	public ValidRelation(Set<T> fromCombinedLevel, Set<T> toCombinedLevel) {
		this.fromCombinedLevel = fromCombinedLevel;
		this.toCombinedLevel = toCombinedLevel;
	}
	
	public Set<T> getFrom() {
		return this.fromCombinedLevel;
	}
	
	public Set<T> getTo() {
		return this.toCombinedLevel;
	}
}
