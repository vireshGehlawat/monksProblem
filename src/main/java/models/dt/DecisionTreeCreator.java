package models.dt;

import com.sun.org.apache.xpath.internal.operations.Bool;
import data.DataRow;
import features.Feature;

import java.util.List;

/**
 * @author viresh.gehlawat
 */
public interface DecisionTreeCreator {
	public Node createTree(List<DataRow> dataRows, List<Feature> features);
}
