package models.dt;

import com.sun.org.apache.xpath.internal.operations.Bool;
import data.DataRow;
import features.Feature;

import java.util.List;

/**
 * @author viresh.gehlawat
 */
public interface DTAlgorithm {
	public Feature getBestFeature(List<DataRow> dataRows, List<Feature> features);
	public Boolean getDefaultValue(List<DataRow> inputDataList);
}
