package org.continuity.experimentation.action.inspectit;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.continuity.experimentation.action.AbstractRestAction;
import org.continuity.experimentation.action.RandomSelection;
import org.continuity.experimentation.data.IDataHolder;
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
	 * run count
	 */
	private static int runCount = 0;

	/**
	 * Flag, which determine, whether storage name is set.
	 */
	private boolean storageNameSet = false;

	/**
	 * Time data holder.
	 */
	private final IDataHolder<Date> timeDataHolder;
	
	/**
	 * Is running loadtest automatically generated.
	 */
	private boolean generatedLoadtest;

	/**
	 * Constructor
	 *
	 * @param storageName
	 *            name of new storage
	 * @param recordingDuration
	 *            duration of the recording
	 * @param dataHolder
	 */
	public StartNewRecording(String storageName, IDataHolder<Date> timeDataHolder) {
		// TODO: Make the host and port configurable
		super("letslx037", "8182");
		this.storageName = storageName;
		this.storageNameSet = true;
		this.timeDataHolder = timeDataHolder;
	}

	/**
	 * Constructor; uses own counter to name the storages
	 *
	 *
	 * @param recordingDuration
	 *            duration of the recording
	 */
	public StartNewRecording(IDataHolder<Date> timeDataHolder, boolean generatedLoadtest) {
		// TODO: Make the host and port configurable
		super("letslx037", "8182");
		this.timeDataHolder = timeDataHolder;
		this.generatedLoadtest = generatedLoadtest;

	}

	@Override
	public void execute() {
		Date currentDate = new Date();
		String pattern = "yyyy-mm-dd-hh-mm-ss";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		if (!storageNameSet) {
			if (generatedLoadtest) {
				// Set name properly
				this.storageName = "GeneratedLoadTest--Run-" + runCount + "--" + format.format(currentDate);
				runCount++;
			} else {
				// Set name properly
				this.storageName = "ReferenceLoadTest--Run-" + runCount + "--" + format.format(currentDate);
			}
		}
		// Set startDate
		timeDataHolder.set(new Date());

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
