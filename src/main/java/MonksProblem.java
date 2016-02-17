import com.google.common.collect.Lists;
import data.DataRow;
import features.Feature;
import features.DiscreteIntegerFeature;
import models.Classifier;
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
		String testFile = args[1];

		List<DataRow> trainingSet = readInputData(trainingFile, ParsingUtil::parse);

		List<Feature> features = prepareFeatureList();

		//TODO get this from config.
		int maxDepth = 4;
		double homogeneity = 0.8 ;

		//Create new instance of decision tree.
		Classifier classifier = new DecisionTree(maxDepth, homogeneity);

		//Train the model with given training set.
		classifier.train(trainingSet, features);

		//Evaluate and compute accuracy.
		List<DataRow> testSet = readInputData(testFile, ParsingUtil::parse);

		//Print all predictions
		testSet.stream().forEach((dataRow) -> System.out.println(
				dataRow.getIdentifier() + " - " + classifier.predict(dataRow)));

		//Get precision recall
		getResults(classifier, testSet);
	}

	/**
	 * Parsing input training data set.
	 * @param fileName Location of input data.
	 * @param f Parser function that takes serialised data as input and returns an instance of DataRow
	 * @return List of DataRow
	 * @throws IllegalArgumentException in case there is any exception in reading data.
	 */
	private static List<DataRow> readInputData(String fileName, Function<String, DataRow> f) throws IllegalArgumentException {
		List<DataRow> inputData = Lists.newArrayList();
		try {
			Path path = Paths.get(fileName);
			Stream<String> lines = Files.lines(path);
			lines.forEach(line -> inputData.add(f.apply(line)));
		} catch (IOException e) {
			throw new IllegalArgumentException("Error in reading data from file " + fileName, e);
		}
		return inputData;
	}

	private static void getResults(Classifier classifier, List<DataRow> testSet) {
		final double[] count = {0,0,0,0};
		testSet.stream().forEach(set -> {
			if (classifier.predict(set) == set.getLabel()) {
				if (set.getLabel()) {
					//TRUE POSITIVES
					count[0]++;
				} else {
					//TRUE NEGATIVES
					count[1]++;
				}
			} else if (set.getLabel()) {
				//FALSE NEGATIVES
				count[2]++;
			} else {
				//FALSE POSITIVES
				count[3]++;
			}
		} );
//		System.out.println("TruePositives: " + count[0] + " FalsePositives: " + count[3] +
//				" TrueNegatives " + count[1] + " FalseNegatives " + count[2]);
		System.out.println("Precision: " + (count[0] * 100)/(count[3] + count[0]));
		System.out.println("Recall: " + (count[0] * 100)/(count[0] + count[2]));
		System.out.println("Accuracy: " + ((count[0] + count[1]) * 100)/(count[0] + count[2] + count[1] + count[3]));
	}

	/**
	 * Prepares a list of features of this given problem statement.
	 * In this particular case this function has logic SPECIFIC TO THIS INSTANCE of problem where
	 * we know the values that each attribute can take and we create the features accordingly.
	 * @return List of features.
	 */
	private static List<Feature> prepareFeatureList() {
		List<Feature> features = new ArrayList<>();
		features.add(new DiscreteIntegerFeature(0));
		features.add(new DiscreteIntegerFeature(1));
		features.add(new DiscreteIntegerFeature(2));
		features.add(new DiscreteIntegerFeature(3));
		features.add(new DiscreteIntegerFeature(4));
		features.add(new DiscreteIntegerFeature(5));
		return features;
	}
}
