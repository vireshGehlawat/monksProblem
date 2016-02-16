package models.dt;

import features.Feature;

import java.util.LinkedList;
import java.util.List;

/**
 * @author viresh.gehlawat
 */

//Decision tree node
public class Node {
	private List<Node> children;
	private Node parent;
	private Feature feature;
	private Boolean value;

	public Node(Boolean value) {
		this.value = value;
	}

	public Node(Feature bestSplit) {
		this.feature = bestSplit;
	}

	public List<Node> getChildren() {
		return children;
	}

	public Feature getFeature() {
		return feature;
	}

	public Boolean getValue() {
		return value;
	}

	public boolean isLeaf() {
		return children == null || children.size() == 0;
	}

	public static Node newLeafNode(Boolean value) {
		return new Node(value);
	}

	public static Node newNode(Feature bestSplit) {
		return new Node(bestSplit);
	}

	public void addChild(Node node) {
		if (children == null) {
			children = new LinkedList<>();
		}
		children.add(node);
	}
}
