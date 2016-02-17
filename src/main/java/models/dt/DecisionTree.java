package models.dt;

import data.DataRow;
import features.Feature;
import models.Classifier;

import java.util.List;

import static java.util.stream.Collectors.groupingBy;

/**
 * Implementation of a decision tree learning algorithm for classification.
 * Handles only discrete feature values
 * @author viresh.gehlawat
 */
public class DecisionTree implements Classifier {

	private DecisionTreeCreator treeCreator;
	private Node root;

	private static final Boolean DEFAULT_PREDICTION = false;

	// Chooses ID3 as default treeCreator
	public DecisionTree(int maxDepth, double homogeneity) {
		if (maxDepth <= 0) {
			throw new IllegalArgumentException("Invalid value " + maxDepth + " given for max depth of decision tree.");
		}
		if (homogeneity <= 0) {
			throw new IllegalArgumentException("Invalid value " + homogeneity + " given for homogeneity of decision tree.");
		}
		this.treeCreator = new ID3TreeCreator(maxDepth, homogeneity);
	}

	// Generic constructor that allows to choose different implementation of treeCreator.
	public DecisionTree(DecisionTreeCreator treeCreator) {
		this.treeCreator = treeCreator;
	}

	@Override
	public void train(List<DataRow> dataRows, List<Feature> features) {
		if (dataRows == null || dataRows.isEmpty() || features == null || features.isEmpty()) return;
		root = treeCreator.createTree(dataRows, features);
	}

	/**
	 * Iterates over the tree and gives out the appropriate label.
	 * This could either be from iterating the tree or DEFAULT_PREDICTION value in case tree iteration is futile.
	 * @param dataRow Input data set
	 * @return Label for input data.
	 */
	@Override
	public Boolean predict(DataRow dataRow) {
		Node node = root;
		while (node != null && !node.isLeaf()) {
			Feature feature = node.getFeature();
			node = node.getChildren((Integer) feature.getValue(dataRow));
		}
		return node == null ? DEFAULT_PREDICTION : node.getValue();
	}
}
