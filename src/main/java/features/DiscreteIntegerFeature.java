package features;

import data.DataRow;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.partitioningBy;

/**
 * @author viresh.gehlawat
 */
public class DiscreteIntegerFeature implements Feature<Integer> {
	private Integer id;

	public DiscreteIntegerFeature(Integer id) {
		this.id = id;
	}

	public Map<Integer, List<DataRow>> splitData(List<DataRow> dataRows) {
		return dataRows.parallelStream().collect(groupingBy(this::getValue));
	}

	@Override
	public Integer getValue(DataRow dataRow) {
		return ((DataRow<Integer>) dataRow).getAttributes().get(id);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DiscreteIntegerFeature)) {
			return false;
		}

		DiscreteIntegerFeature that = (DiscreteIntegerFeature) o;

		if (id != that.id) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return id;
	}

}
