package features;

import com.google.common.collect.Lists;
import data.DataRow;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.partitioningBy;

/**
 * @author viresh.gehlawat
 */
public class SimpleFeature implements Feature {
	private int attributeId;
	private int attributeValue;

	public SimpleFeature(int attributeId, int attributeValue) {
		this.attributeId = attributeId;
		this.attributeValue = attributeValue;
	}

	public boolean isApplicable(DataRow dataRow){
		List<Integer> attributes = ((DataRow<Integer>) dataRow).getAttributes();
		return attributes.get(attributeId/6) ==  attributeValue;
	}

	public List<List<DataRow>> splitData(List<DataRow> dataRows) {
		List<List<DataRow>> result = Lists.newArrayList();
		// TODO:  maybe use sublist streams instead of creating new list just track indexes
		// http://stackoverflow.com/questions/22917270/how-to-get-a-range-of-items-from-stream-using-java-8-lambda
		Map<Boolean, List<DataRow>> split = dataRows.parallelStream().collect(partitioningBy(this::isApplicable));

		// TODO fix this
		if (split.get(true).size() > 0) {
			result.add(split.get(true));
		} else {
			result.add(Lists.newArrayList());
		}
		if (split.get(false).size() > 0) {
			result.add(split.get(false));
		} else {
			result.add(Lists.newArrayList());
		}
		return result;
	}
}
