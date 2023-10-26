package edu.kit.kastel.sdq.transitivereduction.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.BitSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.sdq.transitivereduction.CombinedLevel;

class CombinedLevelTest {

	@Test
	@DisplayName("Equality of BitSets if one has more 0s in front")
	void bitSetTest() {
		BitSet b1 = new BitSet();
		b1.set(3); // 1000
		BitSet b2 = new BitSet(5);
		b2.set(3); // 1000
		
		assertTrue(b1.equals(b2));
	}
	
	@Test
	@DisplayName("Test getCardinality()")
	void combinedLevelTest() {
		BitSet b1 = new BitSet();
		b1.set(3); //   1000
		b1.set(5); // 101000
		CombinedLevel cLevel = new CombinedLevel(b1);
		
		assertEquals(cLevel.getCardinality(), 2);
	}
	
	@Test
	@DisplayName("Test if getCopyOfIncludedLevelsBinaryRepresentation() returns a copy")
	void bitSetCloningTest() {
		BitSet b1 = new BitSet();
		b1.set(3); //   1000
		CombinedLevel cLevel = new CombinedLevel(b1);
		BitSet b1Copy = cLevel.getCopyOfIncludedLevelsBinaryRepresentation();
		b1Copy.set(5);
		
		assertEquals(cLevel.getCardinality(), 1);
		assertEquals(b1Copy.cardinality(), 2);
	}
	
	@Test
	@DisplayName("Successor adding, subsetOf testing")
	void successorTest() {
		BitSet b1 = new BitSet();
		b1.set(3); //   1000
		CombinedLevel cLevel = new CombinedLevel(b1);
		BitSet b1Copy = cLevel.getCopyOfIncludedLevelsBinaryRepresentation();
		b1Copy.set(5); // 101000
		CombinedLevel cLevel2 = new CombinedLevel(b1Copy);
		
		assertTrue(cLevel.isSubsetOf(cLevel2));
		assertFalse(cLevel2.isSubsetOf(cLevel));
		
		cLevel.addSuccessor(cLevel2);
		
		assertTrue(cLevel.getDirectSuccessors().contains(cLevel2));
		assertFalse(cLevel.getDirectSuccessors().contains(cLevel));
		assertTrue(cLevel.getAllSuccessors().contains(cLevel2));
		assertFalse(cLevel.getAllSuccessors().contains(cLevel));
	}
	
	@Test
	@DisplayName("Direct Successors != All Successors")
	void directVsAllSuccessorsTest() {
		BitSet b1 = new BitSet();
		b1.set(3); // 001000
		BitSet b2 = new BitSet();
		b2.set(3); // 001000
		b2.set(5); // 101000
		BitSet b3 = new BitSet();
		b3.set(3); // 001000
		b3.set(5); // 101000
		b3.set(0); // 101001
		CombinedLevel cLevel = new CombinedLevel(b1);
		CombinedLevel cLevel2 = new CombinedLevel(b2);
		CombinedLevel cLevel3 = new CombinedLevel(b3);
		
		assertTrue(cLevel.isSubsetOf(cLevel2));
		assertTrue(cLevel.isSubsetOf(cLevel3));
		assertTrue(cLevel2.isSubsetOf(cLevel3));
		
		cLevel.addSuccessor(cLevel2);
		cLevel2.addSuccessor(cLevel3);
		
		assertTrue(cLevel.getDirectSuccessors().contains(cLevel2));
		assertFalse(cLevel.getDirectSuccessors().contains(cLevel3));
		assertTrue(cLevel.getAllSuccessors().contains(cLevel2));
		assertTrue(cLevel.getAllSuccessors().contains(cLevel3));
	}
	
	@Test
	@DisplayName("Test subsetOf if both are no subset of the other")
	void IntersectionTest() {
		BitSet b1 = new BitSet();
		b1.set(3); // 001000
		b1.set(4); // 011000
		BitSet b2 = new BitSet();
		b2.set(3); // 001000
		b2.set(5); // 101000
		CombinedLevel cLevel = new CombinedLevel(b1);
		CombinedLevel cLevel2 = new CombinedLevel(b2);
		
		assertFalse(cLevel.isSubsetOf(cLevel2));
		assertFalse(cLevel2.isSubsetOf(cLevel));
	}

}
