package org.continuity.experimentation.action;

import org.continuity.experimentation.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Restarts the DVDstore.
 *
 * TODO: Generalize it - e.g., send an arbitrary command to the satellite.
 *
 * @author Tobias Angerstein
 *
 */
public class RestartDVDStore extends AbstractRestAction {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RestartDVDStore.class);

	public RestartDVDStore(String host, String port) {
		super(host, port);
	}

	public RestartDVDStore(String host) {
		super(host, "8765");
	}

	@Override
	public void execute(Context context) {
		LOGGER.info("Restarting DVDStore...");

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response;
		boolean dvdStoreIsOnline = false;
		while (!dvdStoreIsOnline) {
			get("/restart/dvdstore", String.class);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e2) {
				LOGGER.warn("Interrupted during waiting after DVDStore restart: {}", e2.getMessage());
			}
			for (int i = 0; i < 25; i++) {
				try {
					response = restTemplate.getForEntity("http://letslx036:8080/dvdstore/home", String.class);
					if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
						dvdStoreIsOnline = true;
						break;
					}
				} catch (RestClientException e1) {
					LOGGER.warn("Error when restarting the DVDStore: {}. Trying again after 5 seconds...", e1.getMessage());

					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						LOGGER.warn("Interrupted during waiting after DVDStore restart: {}", e.getMessage());
					}
				}
			}
		}

		LOGGER.info("DVDStore is available");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Restart the DVDStore at " + super.toString();
	}

}
