import com.google.common.collect.Lists;
import data.DataRow;
import features.Feature;
import features.SimpleFeature;
import models.Model;
import models.dt.DecisionTree;
import util.ParsingUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author viresh.gehlawat
 */
public class MonksProblem {

	public static void main(String[] args) {

		String trainingFile = args[0];
		List<DataRow> trainingSet = readInputData(trainingFile, ParsingUtil::parse);

		List<Feature> features = prepareFeatureList();

		//TODO get this from config.
		int maxDepth = 7;
		double homogeneity = 0.8;

		//Create new instance of decision tree.
		Model<Boolean> model = new DecisionTree(maxDepth, homogeneity);

		//Train the model with given training set.
		model.train(trainingSet, features);

		String testFile = args[1];
		//Evaluate and compute accuracy.
		List<DataRow> testSet = readInputData(testFile, ParsingUtil::parse);
		final double[] count = {0.0,0.0};
		testSet.stream().forEach(set -> {
			if (model.predict(set) == set.getLabel()) {
				count[0]++;
			}
			count[1]++;
		});
		System.out.println("Accuracy: " + (count[0] * 100)/count[1]);
	}

	/**
	 * Parsing input training data set.
	 * @param fileName Location of input data.
	 * @param f Parser function that takes serialised data as input and returns an instance of DataRow
	 * @return List of DataRow
	 * @throws IllegalArgumentException in case there is any exception in reading or parsing data.
	 */
	private static List<DataRow> readInputData(String fileName, Function<String, DataRow> f) throws IllegalArgumentException {
		List<DataRow> inputData = Lists.newArrayList();
		try {
			Path path = Paths.get(fileName);
			Stream<String> lines = Files.lines(path);
			lines.forEach(line -> inputData.add(f.apply(line)));
		} catch (IOException e) {
			throw new IllegalArgumentException("Error in reading training data from file ");//; + fileName, e);
		}
		return inputData;
	}

	/**
	 * Prepares a list of features of this given problem statement.
	 * In this particular case this function has logic SPECIFIC TO THIS INSTANCE of problem where
	 * we know the values that each attribute can take and we create the features accordingly.
	 * @return List of features.
	 */
	private static List<Feature> prepareFeatureList() {
		List<Feature> features = new ArrayList<>();
		features.add(new SimpleFeature(0, 1));
		features.add(new SimpleFeature(1, 2));
		features.add(new SimpleFeature(2, 3));
		features.add(new SimpleFeature(6, 1));
		features.add(new SimpleFeature(7, 2));
		features.add(new SimpleFeature(8, 3));
		features.add(new SimpleFeature(12, 1));
		features.add(new SimpleFeature(13, 2));
		features.add(new SimpleFeature(18, 1));
		features.add(new SimpleFeature(19, 2));
		features.add(new SimpleFeature(20, 3));
		features.add(new SimpleFeature(24, 1));
		features.add(new SimpleFeature(25, 2));
		features.add(new SimpleFeature(26, 3));
		features.add(new SimpleFeature(27, 4));
		features.add(new SimpleFeature(30, 1));
		features.add(new SimpleFeature(31, 2));
		return features;
	}

}
