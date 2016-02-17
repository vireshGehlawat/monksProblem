package data;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author viresh.gehlawat
 */
public class LabelledDataRow implements DataRow<Integer> {

	/**
	 * Unique identifier for this input. An example could be "DataSet1"
	 */
	private String identifier;

	/**
	 * Label associated with this data set.
	 */
	private boolean label;

	/**
	 * Collection of attributes within this data set. Each attribute refer to one or more {@see Feature}.
	 */
	private List<Integer> attributes;

	public LabelledDataRow(String identifier, boolean label, List<Integer> attributes) {
		this.identifier = identifier;
		this.label = label;
		this.attributes = new LinkedList<>();
		this.attributes.addAll(attributes);
	}

	public String getIdentifier() {
		return identifier;
	}

	@Override
	public Boolean getLabel() {
		return label;
	}

	public List<Integer> getAttributes() {
		return attributes;
	}

	@Override
	public String toString() {
		return "LabelledDataRow{" +
				"identifier='" + identifier + '\'' +
				", label=" + label +
				", attributes=" + attributes +
				'}';
	}

}
