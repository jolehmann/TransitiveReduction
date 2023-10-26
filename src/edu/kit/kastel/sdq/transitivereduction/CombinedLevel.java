package edu.kit.kastel.sdq.transitivereduction;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

public class CombinedLevel {

	private BitSet includedLevelsBinaryRepresentation;
	private Set<CombinedLevel> directSuccessors = new HashSet<CombinedLevel> ();
	
	public CombinedLevel(BitSet includedLevelsBinaryRepresentation) {
		this.includedLevelsBinaryRepresentation = includedLevelsBinaryRepresentation;
	}
	
	public int getCardinality() {
		return this.includedLevelsBinaryRepresentation.cardinality();
	}
	
	public void addSuccessor(CombinedLevel cLevel) {
		this.directSuccessors.add(cLevel);
	}
	
	public Set<CombinedLevel> getDirectSuccessors() {
		return this.directSuccessors;
	}
	
	public Set<CombinedLevel> getAllSuccessors() {
		Set<CombinedLevel> allSuccessors = this.getDirectSuccessors();
		for (CombinedLevel cLevel : this.getDirectSuccessors()) {
			allSuccessors.addAll(cLevel.getAllSuccessors());
		}
		return allSuccessors;
	}
	
	public BitSet getCopyOfIncludedLevelsBinaryRepresentation() {
		return (BitSet) this.includedLevelsBinaryRepresentation.clone();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		final CombinedLevel other = (CombinedLevel) obj;
		return this.includedLevelsBinaryRepresentation.equals(other.includedLevelsBinaryRepresentation);
	}
	
	@Override
	public int hashCode() {
		return this.includedLevelsBinaryRepresentation.hashCode();
	}

	public boolean isSubsetOf(CombinedLevel other) {
		// 1101011000: this
		// 1101011001: other
		// this.or(other).equals(other) ==>
		// this.isSubsetOf(other)
		
		BitSet copyOfThisBitSet = this.getCopyOfIncludedLevelsBinaryRepresentation();
		BitSet otherBitSet = other.getCopyOfIncludedLevelsBinaryRepresentation();
		
		copyOfThisBitSet.or(otherBitSet);
		
		return copyOfThisBitSet.equals(otherBitSet);
	}
	
}
