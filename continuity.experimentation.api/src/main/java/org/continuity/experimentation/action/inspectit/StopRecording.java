package org.continuity.experimentation.action.inspectit;

import org.continuity.experimentation.action.AbstractRestAction;
import org.continuity.experimentation.action.RandomSelection;
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
	 * Constructor
	 */
	public StopRecording() {
		// TODO: Make the host and port configurable
		super("letslx037", "8182");
	}

	@Override
	public void execute() {
		get("/rest/storage/stop", String.class);

		LOGGER.info("Recording stopped");
	}
}
