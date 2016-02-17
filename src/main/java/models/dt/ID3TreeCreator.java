package models.dt;

import data.DataRow;
import features.Feature;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Uses ID3 Algorithm for creating a decision tree.
 * @see <a href="http://www2.cs.uregina.ca/~hamilton/courses/831/notes/ml/dtrees/4_dtrees2.html">Decision trees with ID3</a>
 * @see <a href="http://www.cise.ufl.edu/~ddd/cap6635/Fall-97/Short-papers/2.htm">Short paper on ID3</a>
 * @author viresh.gehlawat
 */
public class ID3TreeCreator implements DecisionTreeCreator {

	private final int maxDepth;
	private final double homogeneity;

	public ID3TreeCreator(int maxDepth, double homogeneity) {
		this.maxDepth = maxDepth;
		this.homogeneity = homogeneity;
	}

	@Override
	public Node createTree(List<DataRow> dataRows, List<Feature> features) {
		return createNode(dataRows, features, 0, null);
	}

	private Node createNode(List<DataRow> dataRows, List<Feature> features, int depth, Integer currentFeatureValue) {

		// if data set already homogeneous enough (has label assigned) make this node a leaf
		Boolean homogeneousLabel = getHomogeneousLabel(dataRows);
		if (homogeneousLabel != null) {
			return Node.newLeafNode(homogeneousLabel, currentFeatureValue);
		}

		//Algorithm terminated as per configs, give default value to node.
		if (exitStateReached(features, depth)) {
			Boolean defaultValue = getDefaultValue(dataRows);
			return Node.newLeafNode(defaultValue, currentFeatureValue);
		}

		// Get best feature
		Feature bestFeature = getBestFeature(dataRows, features);

		// Split data on the basis of this feature
		Map<Integer, List<DataRow>> dataSplitMap = bestFeature.splitData(dataRows);

		// Remove selected feature from feature list
		List<Feature> newFeatures = features.stream().
				filter(p -> !p.equals(bestFeature)).collect(toList());

		Node node = Node.newInternalNode(bestFeature, currentFeatureValue);

		// Add children to current node according to split
		for (Map.Entry<Integer, List<DataRow>> entry : dataSplitMap.entrySet()) {
			List<DataRow> subset = entry.getValue();
			Integer value = entry.getKey();

			if (subset.isEmpty()) {
				// If subset data is empty add a leaf with label calculated from initial data
				Boolean defaultValue = getDefaultValue(dataRows);
				node.addChild(Node.newLeafNode(defaultValue, value));
			} else {
				// Recurse
				node.addChild(createNode(subset, newFeatures, depth + 1, value));
			}
		}
		return node;
	}

	/**
	 * Core of ID3 Algorithm, here we choose the best feature by looking at the impact on information gain on choosing each feature.
	 * Aim is to choose the feature that has the minimum value of impurity and hence would result in max amount of information gain.
	 * @param dataRows Input data set
	 * @param features List of features on which we can split at this instant
	 * @return Feature to be used for splitting.
	 */
	private Feature getBestFeature(List<DataRow> dataRows, List<Feature> features) {
		double minImpurity = 1;
		Feature bestFeature = null;

		for (Feature feature : features) {
			Map<Integer, List<DataRow>> dataSplitMap = feature.splitData(dataRows);

			List<List<DataRow>> splits = dataSplitMap.values().parallelStream().filter(list -> !list.isEmpty()).collect(Collectors.toList());

			// Computing the average impurity across all values of a feature
			double impurity = splits.stream().mapToDouble(EntropyCalculator::calculate).average().getAsDouble();

			if (impurity < minImpurity) {
				minImpurity = impurity;
				bestFeature = feature;
			}
		}
		return bestFeature;
	}

	/**
	 * Counts No. of positive and negative labels in the subtree of this node and returns homogeneous label if exists.
	 * @param dataList Input data set
	 */
	private Boolean getHomogeneousLabel(List<DataRow> dataList) {
		Map<Boolean, Long> labelCountMap = getLabelCountMap(dataList);
		long totalCount = dataList.size();
		for (Boolean label : labelCountMap.keySet()) {
			long nbOfLabels = labelCountMap.get(label);
			if (((double) nbOfLabels / (double) totalCount) >= homogeneity) {
				return label;
			}
		}
		return null;
	}

	/**
	 * @param dataRows Input collection of data sets.
	 * @return Label with most counts.
	 */
	private Boolean getDefaultValue(List<DataRow> dataRows) {
		Map<Boolean, Long> map = getLabelCountMap(dataRows);
		return map.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
	}

	private Map<Boolean, Long> getLabelCountMap(List<DataRow> dataList) {
		return dataList.parallelStream().collect(groupingBy(DataRow::getLabel, counting()));
	}

	private boolean exitStateReached(List<Feature> features, int depth) {
		return features.isEmpty() || depth >= maxDepth;
	}
}
