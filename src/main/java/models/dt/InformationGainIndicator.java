package models.dt;

import data.DataRow;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author viresh.gehlawat
 */
public class InformationGainIndicator {

	/**
	 * Calculates impurity value. High impurity implies low information gain and more random labels of data which in
	 * turn means that split is not very good.
	 *
	 * @param splitData
	 *            Data subset on which impurity is calculated.
	 *
	 * @return Impurity.
	 */
	public static double calculateImpurity(List<DataRow> splitData) {
		List<Boolean> labels = splitData.parallelStream().map(data -> data.getLabel()).distinct().collect(
				Collectors.toList());
		if (labels.size() > 1) {
			// TODO fix to multiple labels
			double p = getEmpiricalProbability(splitData);
			return -1.0 * p * log2(p) - ((1.0 - p) * log2(1.0 - p));
		} else if (labels.size() == 1) {
			return 0.0; // if only one label data is pure
		} else {
			throw new IllegalStateException("This should never happen. Probably a bug.");
		}
	}

	/**
	 * Calculate and return empirical probability of positive class. p+ = n+ / (n+ + n-).
	 *
	 * @param splitData Data on which positive label probability is calculated.
	 * @return Empirical probability.
	 */
	static double getEmpiricalProbability(List<DataRow> splitData) {
		// TODO calculated counts can be cached
		return (double) splitData.parallelStream().filter(DataRow::getLabel).count() / splitData.size();
	}


	private static double log2(double x) {
		return Math.log(x) / Math.log(2);
	}

}
