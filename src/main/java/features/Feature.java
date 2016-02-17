package features;

import data.DataRow;

import java.util.List;
import java.util.Map;

/**
 * @author viresh.gehlawat
 */
public interface Feature <T> {
	/**
	 * Splits data set as per value of the current feature.
	 * @param dataRows Input data set.
	 * @return Map of feature value to data sets with this feature value.
	 */
	public Map<T, List<DataRow>> splitData(List<DataRow> dataRows);

	/**
	 * @param dataRow Input data instance.
	 * @return The value of this feature over given input data.
	 */
	public T getValue(DataRow dataRow);
}
