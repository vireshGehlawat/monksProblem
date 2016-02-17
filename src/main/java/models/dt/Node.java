package models.dt;

import features.Feature;

import java.util.HashMap;
import java.util.Map;

import static models.dt.Node.NodeType.*;

/**
 * Decision tree node
 * @author viresh.gehlawat
 */
public class Node {
	private Map<Object, Node> children;
	private Boolean value;
	private Integer label;
	private Feature feature;
	private NodeType nodeType;

	private Node(Boolean value, Integer label) {
		this.children = new HashMap<>();
		this.value = value;
		this.label = label;
		this.feature = null;
		this.nodeType = LEAF;
	}

	private Node(Feature bestSplit, Integer label) {
		this.children = new HashMap<>();
		this.value = null;
		this.label = label;
		this.feature = bestSplit;
		this.nodeType = INTERNAL;
	}

	public Node getChildren(Integer childLabel) {
		return children.get(childLabel);
	}

	public Feature getFeature() {
		return feature;
	}

	public Boolean getValue() {
		return value;
	}

	public boolean isLeaf() {
		return nodeType.equals(LEAF);
	}

	public static Node newLeafNode(Boolean value, Integer label) {
		return new Node(value, label);
	}

	public static Node newInternalNode(Feature bestSplit, Integer label) {
		return new Node(bestSplit, label);
	}

	public void addChild(Node node) {
		this.children.put(node.label, node);
	}

	enum NodeType { LEAF, INTERNAL }
}
