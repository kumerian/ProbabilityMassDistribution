/**
 * Models a discrete probability distribution.
 */
package com.croga.MassDistribution;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author croga
 *
 */
public class MassDistribution {
	
	private Map<Double, Double> massDistribution;
	private Map<Double, Double> cumulativeMass;
	
    /**
     * Constructs a distribution with the given PMD.
     *
     * @param $pmd
     *   The probability mass distribution. Array[int] of float.
     *   $pmd[$i] = probability of $i.
     *
     * Note that the indices of $pmd need not be consecutive.
     */
	public MassDistribution(Map<Double, Double> pmd) {
		this.massDistribution = pmd;
	}
	
	public Map<Double, Double> getMassDistribution() {
		return this.massDistribution;
	}
	
    /**
     * Creates a Distribution from an array of values with the same Mass.
     * To make things confusing: The values become the keys of the Map.
     *
     * @param $values
     *  The possible values, each with the same chance of occurrence.
     * @return MassDistribution
     *  Probability Mass Distribution based on the entered values, each with the same Probability Mass.
     */
	static public MassDistribution createMassDistribution(List<Double> values) {
		Double probability = Double.valueOf(1.0 / values.size());
		Map<Double, Double> pmd = new HashMap<>();
		for (Double value : values) {
			if (pmd.containsKey(value)) {
				Double originalProbability = pmd.get(value);
				pmd.put(value, originalProbability + probability);
			} else {
				pmd.put(value, probability);
			}
		}
		return new MassDistribution(pmd);
	}

	/// Adds the Distributions $this and $that; returns the sum Distribution.
	public MassDistribution Add(MassDistribution that) {
		Map<Double, Double> pmd = new HashMap<>(); 
		Map<Double, Double> thisMap = this.massDistribution;
		Map<Double, Double> thatMap = that.getMassDistribution();
		
		for (Entry<Double, Double> thisDamage : thisMap.entrySet()) {
			for (Entry<Double, Double> thatDamage : thatMap.entrySet()) {
				Double newValue = thisDamage.getKey() + thatDamage.getKey();
				Double newProbability = thisDamage.getValue() * thatDamage.getValue();
				if (pmd.containsKey(newValue)) {
					Double originalProbability = pmd.get(newValue);
					pmd.put(newValue, originalProbability + newProbability);
				} else {
					pmd.put(newValue, newProbability);
				}
			}
		}
		return new MassDistribution(pmd);
	}	
	
	/// Returns the average value of the distribution.
	public Double Average() {
		Double weightedSum = 0.0;
		for (Entry<Double, Double> entry : this.massDistribution.entrySet()) {
			weightedSum += entry.getKey() * entry.getValue();
		}
		return weightedSum;
	}
	
	/**
	 * Returns the lowest and highest damage corresponding to probability cutoff.
	 * 
	 * @param cutoff
	 *   the Cutoff probability
	 * @return Double[2]
	 *   where the first entry is the low value
	 *   and the second entry is the high value
	 */
	public Double[] Bracket(Double cutoff) {
		Double low = cutoff;
		Double high = 1.0 - cutoff;
		String lookingFor = "left";
		Double left = -1.0;
		Double right = -1.0;
		Double[] support = this.Support();
		
		for(Double value : support ) {
			switch (lookingFor) {
			case "left":
				if (this.CMD(value) >= low) {
					left = value;
					lookingFor = "right";
				}
				break;
			case "right":
				if (this.CMD(value) >= high) {
					right = value;
					lookingFor = "done";
				}
				break;
			default:
				break;
			}
			if (lookingFor == "done") {
				break;
			}
		}
		if (right == -1.0) {
			right = this.SupportEdges()[1];
		}
		Double[] returnValue = new Double[2];
		returnValue[0] = left;
		returnValue[1] = right;
		return returnValue;
	}
	
	/// Returns the cumulative probability mass of $x (= P(x <= $x)).
	private Double CMD(Double x) {
		Double[] support = this.Support();
		if (this.cumulativeMass == null) {
			 Double previous = 0.0;
			 this.cumulativeMass = new HashMap<>();
			 for (Double entry : support) {
				 Double probability = massDistribution.get(entry); 
				 Double cumulativeMassValue = previous + probability;
				 this.cumulativeMass.put(entry, cumulativeMassValue);
				 previous = cumulativeMassValue;
			 }
		}
		return this.cumulativeMass.get(x);
	}
	
	/// Returns the left and right edges (inclusive) of the distribution's support.
	private Double[] SupportEdges() {
		Double[] support = this.Support();
		Double left = support[0];
		Double right = support[support.length-1];
		return new Double[] {left, right};
	}
	
	/// Returns the support of the distribution.
	private Double[] Support() {
		Double[] support = new Double[this.massDistribution.size()];
		support = this.massDistribution.keySet().toArray(support);
		Arrays.parallelSort(support);
		return support;
	}
}
