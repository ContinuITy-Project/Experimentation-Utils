package org.continuity.experimentation.action.inspectit;

import java.util.Date;

import org.continuity.experimentation.action.AbstractRestAction;
import org.continuity.experimentation.action.RandomSelection;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(RandomSelection.class);

	/**
	 * Stop time data holder.
	 */
	private final IDataHolder<Date> stopTimeDataHolder;

	/**
	 * Constructor
	 * 
	 * @param stopTimeDataHolder
	 */
	public StopRecording(IDataHolder<Date> stopTimeDataHolder) {
		// TODO: Make the host and port configurable
		super("letslx037", "8182");
		this.stopTimeDataHolder = stopTimeDataHolder;
	}

	@Override
	public void execute() {
		stopTimeDataHolder.set(new Date());
		get("/rest/storage/stop", String.class);

		LOGGER.info("Recording stopped");
	}
}
