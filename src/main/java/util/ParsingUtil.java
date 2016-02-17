package util;

import data.LabelledDataRow;
import features.Feature;
import features.SimpleFeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author viresh.gehlawat
 */

//Adding current data set specific functions here for working with current data set.
public class ParsingUtil {
	private static final char ZERO = '0';
	private static final String ATTRIBUTE_DELIMITER = " ";
	private static final String IDENTIFIER_DELIMITER = ":";
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
}
