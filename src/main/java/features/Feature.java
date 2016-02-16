package features;

import data.DataRow;

import java.util.List;

/**
 * @author viresh.gehlawat
 */
public interface Feature {
	public boolean isApplicable(DataRow dataRow);
	public List<List<DataRow>> splitData(List<DataRow> dataRows);
}
