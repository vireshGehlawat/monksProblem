package models.dt;

import data.DataRow;
import features.Feature;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * @author viresh.gehlawat
 */
public class ID3Algorithm implements DTAlgorithm {

	@Override
	public Feature getBestFeature(List<DataRow> dataRows, List<Feature> features) {

		double currentImpurity = 1;
		Feature bestFeature = null;

		for (Feature feature : features) {
			List<List<DataRow>> splitData = feature.splitData(dataRows);

			double calculatedSplitImpurity = splitData.parallelStream().filter(list -> !list.isEmpty()).mapToDouble(
					InformationGainIndicator::calculateImpurity).average().getAsDouble();

			if (calculatedSplitImpurity < currentImpurity) {
				currentImpurity = calculatedSplitImpurity;
				bestFeature = feature;
			}
		}

		return bestFeature;
	}

	public Boolean getDefaultValue(List<DataRow> inputDataList) {
		return getMajorityLabel(inputDataList);
	}

	protected Boolean getMajorityLabel(List<DataRow> data) {
		// group by to map <Label, count> like in getLabels() but return Label with most counts
		return data.parallelStream().collect(groupingBy(DataRow::getLabel, counting())).entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
	}
}
