package org.continuity.experimentation.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
	 * storage name.
	 */
	private String storageName;

	/**
	 * recording duration.
	 */
	private String recordingDuration;

	/**
	 * Constructor
	 * 
	 * @param storageName
	 *            name of new storage
	 * @param recordingDuration
	 *            duration of the recording
	 */
	public StartNewRecording(String storageName, long recordingDuration) {
		super("letslx037", "8182");
		this.storageName = storageName;
		this.recordingDuration = Long.toString(recordingDuration);
	}

	public static void main(String[] args) {
		StartNewRecording rec = new StartNewRecording("test", 20000);
		rec.execute();
	}

	@Override
	public void execute() {
		try {
			// Create new storage
			String creationResponse = get("/rest/storage/" + storageName + "/create", String.class);

			// Get id of created storage
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode node = mapper.readValue(creationResponse, ObjectNode.class);
			String storageId = node.get("storage").get("id").asText();

			// Start recording.
			Map<String, String> uriVariables = new HashMap<String, String>();
			uriVariables.put("id", storageId);
			uriVariables.put("recordingDuration", recordingDuration);
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/rest/storage/start")
			        .queryParam("id", storageId)
			        .queryParam("recordingDuration", recordingDuration);
			get(builder.build().encode().toUri().toString(), String.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
