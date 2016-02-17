package models;

import data.DataRow;
import features.Feature;

import java.util.List;

/**
 * @author viresh.gehlawat
 */
public interface Classifier {
	/**
	 * Trains a classifier. Must not change the input data set.
	 * @param inputDataList Input training data set to train the classifier.
	 * @param features List of features for this classifier.
	 */
	public void train(List<DataRow> inputDataList, List<Feature> features);

	/**
	 * @param dataRow input data set to be predicted
	 * @return prediction
	 */
	public Boolean predict(DataRow dataRow);
}
