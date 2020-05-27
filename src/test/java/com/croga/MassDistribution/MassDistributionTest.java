/**
 * 
 */
package com.croga.MassDistribution;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import static java.util.Map.entry;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author croga
 *
 */
public class MassDistributionTest {

	/**
	 * Test method for {@link com.croga.MassDistribution.MassDistribution#MassDistribution(java.util.Map)}.
	 */
	@Test
	public final void InstantiateACorrectMassDistributionObject() {
		Map<Double, Double> testMap = new HashMap<>();
		testMap.put(1.0, 1.0/6);
		MassDistribution md = new MassDistribution(testMap);
		assertTrue(md instanceof MassDistribution);
	}

	/**
	 * Test method for {@link com.croga.MassDistribution.MassDistribution#getMassDistribution()}.
	 */
	@Test
	public final void CanIRetrieveTheOriginalMap() {
		Map<Double, Double> testMap = new HashMap<>();
		testMap.put(1.0, 1.0/6);
		MassDistribution md = new MassDistribution(testMap);
		Map<Double, Double> resultMap = md.getMassDistribution();
		assertThat(resultMap, is(testMap));
	}

	/**
	 * Test method for {@link com.croga.MassDistribution.MassDistribution#createMassDistribution(java.util.List)}.
	 */
	@ParameterizedTest
	@MethodSource("testData")
	public final void createMassDistributionCreatesAMassDistributionWithTheCorrectMap(List<Double> testData, Map<Double, Double> expectedResult, Double average) {
		MassDistribution md = MassDistribution.createMassDistribution(testData);
		Map<Double, Double> resultMap = md.getMassDistribution();
		assertThat(resultMap, is(expectedResult));
	}

	/**
	 * Test method for {@link com.croga.MassDistribution.MassDistribution#Add(com.croga.MassDistribution.MassDistribution)}.
	 */
	@ParameterizedTest
	@MethodSource("testDataAdd")
	public final void CanIAddAnotherArrayToAnExistingMassDistribution(List<Double> testData, List<Double> testDataToAdd, Map<Double, Double> expectedResultMap, Double bracketMass, Double[] bracketValue) {
		MassDistribution md = MassDistribution.createMassDistribution(testData);
		MassDistribution resultMd = md.Add(MassDistribution.createMassDistribution(testDataToAdd));
		Map<Double, Double> resultMap = resultMd.getMassDistribution();
		for (Double key : resultMap.keySet()) {
			Double result = resultMap.get(key);
			Double expectedResult = expectedResultMap.get(key);
			assertEquals(result, expectedResult, 0.0000001);
		}
	}

	/**
	 * Test method for {@link com.croga.MassDistribution.MassDistribution#Average()}.
	 */
	@ParameterizedTest
	@MethodSource("testData")
	public final void testAverageReturnsCorrectAverage(List<Double> testData, Map<Double, Double> resultMap, Double expectedResult) {
		MassDistribution md = MassDistribution.createMassDistribution(testData);
		Double result = md.Average();
		assertEquals(expectedResult, result);
	}

	/**
	 * Test method for {@link com.croga.MassDistribution.MassDistribution#Bracket(java.lang.Double)}.
	 */
	@ParameterizedTest
	@MethodSource("testDataAdd")
	public final void ShowMeUpperAndLowerValueForCertainMass(List<Double> testData, List<Double> testDataToAdd, Map<Double, Double> resultMap, Double bracketMass, Double[] expectedResult) {
		MassDistribution md = MassDistribution.createMassDistribution(testData);
		MassDistribution resultMd = md.Add(MassDistribution.createMassDistribution(testDataToAdd));
		Double[] result = resultMd.Bracket(bracketMass);
		assertEquals(expectedResult[0], result[0], 0.0000001);
		assertEquals(expectedResult[1], result[1], 0.0000001);
	}
	
	private static Stream<Arguments> testData() {
		return Stream.of(
				Arguments.of((Object) new ArrayList<Double>(Arrays.asList(1.0, 2.0, 2.0, 1.0)),
						(Object) Map.ofEntries(entry(1.0, 2.0/4), entry(2.0, 2.0/4)),
						Double.valueOf(1.5)
				),
				Arguments.of((Object) new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0)),
						(Object) Map.ofEntries(entry(1.0, 1.0/4), entry(2.0, 1.0/4), entry(3.0, 1.0/4), entry(4.0, 1.0/4)),
						Double.valueOf(2.5)
				),
				Arguments.of((Object) new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)),
						(Object) Map.ofEntries(entry(1.0, 1.0/6), entry(2.0, 1.0/6), entry(3.0, 1.0/6), entry(4.0, 1.0/6), entry(5.0, 1.0/6), entry(6.0, 1.0/6)),
						Double.valueOf(3.5)
				),
				Arguments.of((Object) new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0)),
						(Object) Map.ofEntries(entry(1.0, 1.0/8), entry(2.0, 1.0/8), entry(3.0, 1.0/8), entry(4.0, 1.0/8), entry(5.0, 1.0/8), entry(6.0, 1.0/8), entry(7.0, 1.0/8), entry(8.0, 1.0/8)),
						Double.valueOf(4.5)
						)
				);
	}
	
	private static Stream<Arguments> testDataAdd() {
		return Stream.of(
				Arguments.of((Object) new ArrayList<Double>(Arrays.asList(1.0, 2.0, 2.0, 1.0)),
						(Object) new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0)),
						(Object) Map.ofEntries(entry(2.0, 2.0/16), entry(3.0, 4.0/16), entry(4.0, 4.0/16), entry(5.0, 4.0/16), entry(6.0, 2.0/16)),
						Double.valueOf(0.05),
						new Double[]{2.0,6.0}						
				),
				Arguments.of((Object) new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0)),
						(Object) new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0)),
						(Object) Map.ofEntries(entry(2.0, 1.0/16), entry(3.0, 2.0/16), entry(4.0, 3.0/16), entry(5.0, 4.0/16), entry(6.0, 3.0/16), entry(7.0, 2.0/16), entry(8.0, 1.0/16)),
						Double.valueOf(0.25),
						new Double[]{4.0,6.0}						
				),
				Arguments.of((Object) new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)),
						(Object) new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)),
						(Object) Map.ofEntries(entry(2.0, 1.0/36), entry(3.0, 2.0/36), entry(4.0, 3.0/36), entry(5.0, 4.0/36), entry(6.0, 5.0/36), entry(7.0, 6.0/36), entry(8.0, 5.0/36), entry(9.0, 4.0/36), entry(10.0, 3.0/36), entry(11.0, 2.0/36), entry(12.0, 1.0/36)),
						Double.valueOf(0.25),
						new Double[]{5.0,9.0}
				),
				Arguments.of((Object) new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0)),
						(Object) new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)),
						(Object) Map.ofEntries(entry(2.0, 1.0/24), entry(3.0, 2.0/24), entry(4.0, 3.0/24), entry(5.0, 4.0/24), entry(6.0, 4.0/24), entry(7.0, 4.0/24), entry(8.0, 3.0/24), entry(9.0, 2.0/24), entry(10.0, 1.0/24)),
						Double.valueOf(0.25),
						new Double[]{4.0,8.0}
				)
		);
	}
	
}
