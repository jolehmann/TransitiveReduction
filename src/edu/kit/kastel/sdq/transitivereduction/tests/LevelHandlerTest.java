package edu.kit.kastel.sdq.transitivereduction.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.sdq.transitivereduction.LevelHandler;
import edu.kit.kastel.sdq.transitivereduction.ValidRelation;

class LevelHandlerTest {

	// Single Levels: CreditCardInfo, UserDetails, TravelData, AirlineData
	// following 6 inputs as combinedLevels

	List<String> input1 = Arrays.asList("UserDetails"); // 0100
	List<String> input2 = Arrays.asList("AirlineData"); // 0001
	List<String> input3 = Arrays.asList("CreditCardInfo", "UserDetails"); // 1100
	List<String> input4 = Arrays.asList("CreditCardInfo", "UserDetails", "TravelData"); // 1110
	List<String> input5 = Arrays.asList("CreditCardInfo", "UserDetails", "AirlineData"); // 1101
	List<String> input6 = Arrays.asList("UserDetails", "TravelData", "AirlineData"); // 0111

	@Test
	@DisplayName("insert 6 inputs representing combined levels")
	void testLevelHandler() {
		LevelHandler<String> levelHandler = new LevelHandler<String>();
		levelHandler.insertCombinedLevel(input1);
		levelHandler.insertCombinedLevel(input1); // Duplicate insertions of same Level are possible
		levelHandler.insertCombinedLevel(input2);
		levelHandler.insertCombinedLevel(input3);
		levelHandler.insertCombinedLevel(input4);
		levelHandler.insertCombinedLevel(input5);
		levelHandler.insertCombinedLevel(input6);

		List<ValidRelation<String>> transitiveReduction = levelHandler.getTransitiveReduction();

		// There should be 6 ValidRelation objects because
		// 4 from the cardinality 1 levels (which should both produce 2 relations)
		// 2 from the cardinality 2 level (which should produce 2 relations)
		// 0 from the cardinality 3 levels, because there is no level with higher
		// cardinality --> no superset
		assertEquals(6, transitiveReduction.size());

		// print relations:
		System.out.println(getStringOf(input1));
		System.out.println(getStringOf(input2));
		System.out.println(getStringOf(input3));
		System.out.println(getStringOf(input4));
		System.out.println(getStringOf(input5));
		System.out.println(getStringOf(input6));
		System.out.println();
		for (ValidRelation<String> v : transitiveReduction) {
			Set<String> from = v.getFrom();
			Set<String> to = v.getTo();
			System.out.println(getStringOf(from) + " --> " + getStringOf(to));
		}
	}

	private String getStringOf(Collection<String> setOfStrings) {
		String result = "{";
		for (String s : setOfStrings) {
			if (!result.endsWith("{")) {
				result = result + ", ";
			}
			result = result + s;
		}
		return result + "}";
	}

}
