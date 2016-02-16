package data;

/**
 * @author viresh.gehlawat
 */

import java.util.List;

/**
 * Markup interface for input data set (both train as well as test)
 */
public interface DataRow <T> {
	public List<T> getAttributes();

	Boolean getLabel();
}
