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

	private static final char ZERO = '0';
	private static final String ATTRIBUTE_DELIMITER = " ";
	private static final String IDENTIFIER_DELIMITER = ":";

	public LabelledDataRow(String identifier, boolean label, List<Integer> attributes) {
		this.identifier = identifier;
		this.label = label;
		this.attributes = new LinkedList<>();
		this.attributes.addAll(attributes);
	}

	/**
	 * Parse the serialized input to create a new instance of LabelledDataRow
	 * @param serializedData String in the given format Sample Input String : "data_5:  1 1 1 1 3 1  ->  1"
	 * @return new instance of LabelledDataRow
	 */
	public static LabelledDataRow parse(String serializedData) {
		String[] split = serializedData.split(IDENTIFIER_DELIMITER);

		String identifier = split[0];

		int labelValue = getLabel(serializedData);

		List<Integer> attributes = getAttributes(split[1].split("->")[0].trim());

		return new LabelledDataRow(identifier, labelValue == 0, attributes);
	}

	private static List<Integer> getAttributes(String s) {
		String[] attr = s.trim().split(ATTRIBUTE_DELIMITER);
		List<Integer> attributes = new ArrayList<>();

		Arrays.stream(attr).forEach((l) -> {
			attributes.add(l.charAt(0) - ZERO);
		});
		return attributes;
	}

	private static int getLabel(String serializedData) {
		String[] strings = serializedData.split("->");
		return strings[1].trim().charAt(0) - ZERO;
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
