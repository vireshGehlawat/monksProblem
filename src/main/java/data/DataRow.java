package data;

import java.util.List;

/**
 * Models input data set as a domain object.
 * @author viresh.gehlawat
 */
public interface DataRow <T> {
	/**
	 * @return List of all attributes applicable to the input.
	 */
	public List<T> getAttributes();

	/**
	 * @return Unique identifier. To be be used for logging purposes.
	 */
	String getIdentifier();

	/**
	 * @return Label value in case inout data set is labelled.
	 */
	Boolean getLabel();
}
