package org.continuity.experimentation.action.inspectit;

import java.io.IOException;

import org.continuity.experimentation.Context;
import org.continuity.experimentation.action.AbstractRestAction;
import org.continuity.experimentation.action.RandomSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Creates new storage and starts new recording.
 *
 * @author Tobias Angerstein
 *
 */
public class StartNewRecording extends AbstractRestAction {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RandomSelection.class);

	/**
	 * storage name.
	 */
	private String storageName;

	/**
	 * Constructor
	 *
	 * @param storageName
	 *            name of new storage
	 * @param recordingDuration
	 *            duration of the recording
	 */
	public StartNewRecording(String storageName) {
		// TODO: Make the host and port configurable
		super("letslx037", "8182");
		this.storageName = storageName;
	}

	@Override
	public void execute(Context context) {
		// Create new storage
		String creationResponse = get("/rest/storage/" + storageName + "/create", String.class);

		// Get id of created storage
		ObjectMapper mapper = new ObjectMapper();
		try {
			ObjectNode node = mapper.readValue(creationResponse, ObjectNode.class);
			String storageId = node.get("storage").get("id").asText();

			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/rest/storage/start").queryParam("id", storageId);
			get(builder.build().encode().toUri().toString(), String.class);

			LOGGER.info("Recording '" + storageName + "' started");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
