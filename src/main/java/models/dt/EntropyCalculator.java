package models.dt;

import data.DataRow;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @see <a href="http://www2.cs.uregina.ca/~hamilton/courses/831/notes/ml/dtrees/4_dtrees2.html">Decision trees with ID3</a>
 * @author viresh.gehlawat
 */
public class EntropyCalculator {

	/**
	 * Calculates entropy value.
	 * A high value of entropy implies less information gain and more random labels.
	 * @param dataRows Data subset on which entropy is calculated.
	 * @return entropy.
	 */
	public static double calculate(List<DataRow> dataRows) {
		List<Boolean> labels = dataRows.parallelStream().map(data -> data.getLabel()).distinct().collect(
				Collectors.toList());

		if (labels.isEmpty()) throw new IllegalArgumentException("Check input data for sanity " + dataRows);

		// If only a single label is present then data is pure.
		if (labels.size() == 1)  return 0.0;

		return getEntropy(dataRows);
	}

	/**
	 * Calculate and return entropy on the basis of empirical probability of Label true
	 * @param dataRows Data on which positive label probability is calculated.
	 */
	private static double getEntropy(List<DataRow> dataRows) {
		long count = dataRows.parallelStream().filter(DataRow::getLabel).count();
		double p =   (double) count / dataRows.size();
		return -1.0 * p * log2(p) - ((1.0 - p) * log2(1.0 - p));
	}

	private static double log2(double x) {
		return Math.log(x) / Math.log(2);
	}

}
