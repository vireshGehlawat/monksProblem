package models.dt;

import data.DataRow;
import features.Feature;
import models.Model;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * @author viresh.gehlawat
 */
public class DecisionTree implements Model<Boolean> {

	private final int maxDepth;
	private final double homogeneity;
	private DTAlgorithm algorithm;
	private Node root;

	//Chooses ID3 as default algorithm
	public DecisionTree(int maxDepth, double homogeneity) {
		this.maxDepth = maxDepth;
		this.homogeneity = homogeneity;
		this.algorithm = new ID3Algorithm();
	}

	//Generic constructor that allows to choose different implementation of algorithm.
	public DecisionTree(int maxDepth, double homogeneity, DTAlgorithm algorithm) {
		this.maxDepth = maxDepth;
		this.homogeneity = homogeneity;
		this.algorithm = algorithm;
	}

	@Override
	public void train(List<DataRow> inputDataList, List<Feature> features) {
		root = train(inputDataList, features, 0);
	}

	private Node train(List<DataRow> inputDataList, List<Feature> features, int depth) {

		// if data set already homogeneous enough (has label assigned) make this node a leaf
		Boolean homogeneousLabel = getLabel(inputDataList);
		if (homogeneousLabel != null) {
			return Node.newLeafNode(homogeneousLabel);
		}

		//Algorithm terminated as per configs, give default value to node.
		if (exitStateReached(features, depth)) {
			return new Node(algorithm.getDefaultValue(inputDataList));
		}

		// Get best feature
		Feature bestFeature = algorithm.getBestFeature(inputDataList, features);

		// Split data on the basis of this feature
		List<List<DataRow>> splitData = bestFeature.splitData(inputDataList);

		// Remove selected feature from feature list
		List<Feature> newFeatures = features.stream().filter(p -> !p.equals(bestFeature)).collect(toList());
		Node node = Node.newNode(bestFeature);

		// add children to current node according to split
		for (List<DataRow> subsetTrainingData : splitData) {
			if (subsetTrainingData.isEmpty()) {
				// If subset data is empty add a leaf with label calculated from initial data
				node.addChild(Node.newLeafNode(algorithm.getDefaultValue(inputDataList)));
			} else {
				// Recurse
				node.addChild(train(subsetTrainingData, newFeatures, depth + 1));
			}
		}
		return node;
	}

	@Override
	public Boolean predict(DataRow dataRow) {
		Node node = root;
		while (!node.isLeaf()) {
			Feature feature = node.getFeature();
			if (feature.isApplicable(dataRow)) {
				node = node.getChildren().get(0);
			} else {
				node = node.getChildren().get(1);
			}
		}
		return node.getValue();
	}

	/**
	 * Counts No. of positive and negative labels in the subtree of this node and returns homogeneous label if exists.
	 * @param dataList Input data set
	 */
	private Boolean getLabel(List<DataRow> dataList) {
		Map<Boolean, Long> labelCount = dataList.parallelStream().collect(groupingBy(DataRow::getLabel, counting()));
		long totalCount = dataList.size();
		for (Boolean label : labelCount.keySet()) {
			long nbOfLabels = labelCount.get(label);
			if (((double) nbOfLabels / (double) totalCount) >= homogeneity) {
				return label;
			}
		}
		return null;
	}

	private boolean exitStateReached(List<Feature> features, int depth) {
		return features.isEmpty() || depth >= maxDepth;
	}
}
