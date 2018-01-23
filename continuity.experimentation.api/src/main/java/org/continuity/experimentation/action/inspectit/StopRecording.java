package org.continuity.experimentation.action.inspectit;

import java.util.Date;

import org.continuity.experimentation.Context;
import org.continuity.experimentation.action.AbstractRestAction;
import org.continuity.experimentation.data.IDataHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stops the recording
 *
 * @author Tobias Angerstein
 *
 */
public class StopRecording extends AbstractRestAction {
	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(StopRecording.class);

	/**
	 * Stop time data holder.
	 */
	private final IDataHolder<Date> stopTimeDataHolder;

	/**
	 * Constructor.
	 *
	 * @param stopTimeDataHolder
	 *            [OUTPUT] Holds the stop time.
	 * @param host
	 *            Host name of the inspectIT CMR.
	 * @param port
	 *            Port of the inspectIT CMR.
	 */
	public StopRecording(IDataHolder<Date> stopTimeDataHolder, String host, String port) {
		super(host, port);
		this.stopTimeDataHolder = stopTimeDataHolder;
	}

	/**
	 * Constructor. Uses the default port 8182.
	 *
	 * @param stopTimeDataHolder
	 *            [OUTPUT] Holds the stop time.
	 * @param host
	 *            Host name of the inspectIT CMR.
	 */
	public StopRecording(IDataHolder<Date> stopTimeDataHolder, String host) {
		super(host, "8182");
		this.stopTimeDataHolder = stopTimeDataHolder;
	}

	@Override
	public void execute(Context context) {
		stopTimeDataHolder.set(new Date());
		get("/rest/storage/stop", String.class);

		LOGGER.info("Recording stopped");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Stops inspectIT recording " + " at " + super.toString() + " and stores current time to \"" + stopTimeDataHolder + "\"";
	}
}
