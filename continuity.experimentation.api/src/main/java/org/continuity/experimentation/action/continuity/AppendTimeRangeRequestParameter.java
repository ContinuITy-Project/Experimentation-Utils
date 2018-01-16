package org.continuity.experimentation.action.continuity;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.continuity.experimentation.Context;
import org.continuity.experimentation.IExperimentAction;
import org.continuity.experimentation.data.IDataHolder;
import org.continuity.experimentation.exception.AbortException;
import org.continuity.experimentation.exception.AbortInnerException;

/**
 * Appends a request parameter holding a time range to an existing request. E.g., adds the following
 * string: {@code ?fromDate=2018-01-01T00:00:00.000Z&toDate=2018-01-01T01:00:00.000Z}
 *
 * @author Henning Schulz
 *
 */
public class AppendTimeRangeRequestParameter implements IExperimentAction {

	private final IDataHolder<Date> startTimeDataHolder;
	private final IDataHolder<Date> stopTimeDataHolder;

	private final IDataHolder<String> requestHolder;

	private final String startParamName;
	private final String stopParamName;

	/**
	 * Constructor. Uses 'fromDate' and 'toDate' as start and stop time parameter names.
	 *
	 * @param startTimeDataHolder
	 *            [INPUT] Holds the start time.
	 * @param stopTimeDataHolder
	 *            [INPUT] Holds the stop time.
	 * @param requestHolder
	 *            [INPUT/OUTPUT] Holds the request to which the parameter will be appended.
	 */
	public AppendTimeRangeRequestParameter(IDataHolder<Date> startTimeDataHolder, IDataHolder<Date> stopTimeDataHolder, IDataHolder<String> requestHolder) {
		this(startTimeDataHolder, stopTimeDataHolder, requestHolder, "fromDate", "toDate");
	}

	/**
	 * Constructor.
	 *
	 * @param startTimeDataHolder
	 *            [INPUT] Holds the start time.
	 * @param stopTimeDataHolder
	 *            [INPUT] Holds the stop time.
	 * @param requestHolder
	 *            [INPUT/OUTPUT] Holds the request to which the parameter will be appended.
	 * @param startParamName
	 *            The name of the start time parameter.
	 * @param stopParamName
	 *            The name of the stop time parameter.
	 */
	public AppendTimeRangeRequestParameter(IDataHolder<Date> startTimeDataHolder, IDataHolder<Date> stopTimeDataHolder, IDataHolder<String> requestHolder, String startParamName,
			String stopParamName) {
		this.startTimeDataHolder = startTimeDataHolder;
		this.stopTimeDataHolder = stopTimeDataHolder;
		this.requestHolder = requestHolder;
		this.startParamName = startParamName;
		this.stopParamName = stopParamName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context) throws AbortInnerException, AbortException, Exception {
		Date startTime = startTimeDataHolder.get();
		Date stopTime = stopTimeDataHolder.get();
		String pattern = "yyyy/MM/dd/HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String startTimeString = format.format(startTime);
		String stopTimeString = format.format(stopTime);

		String request = requestHolder.get();
		String sep;

		if (request.contains("?")) {
			sep = "&";
		} else {
			sep = "?";
		}

		requestHolder.set(request + sep + startParamName + "=" + startTimeString + "&" + stopParamName + "=" + stopTimeString);
	}

}
