package models;

import data.DataRow;
import features.Feature;

import java.util.List;

/**
 * @author viresh.gehlawat
 */

/**
 *
 */
public interface Classifier {
	public void train(List<DataRow> inputDataList, List<Feature> features);
	public Boolean predict(DataRow dataRow);
}
