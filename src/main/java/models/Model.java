package models;

import data.DataRow;
import features.Feature;

import java.util.List;

/**
 * @author viresh.gehlawat
 */
public interface Model<T, V> {
	public void train(List<DataRow> inputDataList, List<Feature> features);
	public V predict(DataRow dataRow);
}
