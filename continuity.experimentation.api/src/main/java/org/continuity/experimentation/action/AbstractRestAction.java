package org.continuity.experimentation.action;

import org.continuity.experimentation.IExperimentAction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Provides means for executing REST requests.
 *
 * @author Henning Schulz
 *
 */
public abstract class AbstractRestAction implements IExperimentAction {

	private final String host;
	private final String port;

	private final RestTemplate restTemplate;

	public AbstractRestAction(String host, String port, RestTemplate restTemplate) {
		this.host = host;
		this.port = port;

		if (restTemplate != null) {
			this.restTemplate = restTemplate;
		} else {
			this.restTemplate = new RestTemplate();
		}
	}

	public AbstractRestAction(String host, String port) {
		this(host, port, null);
	}

	public AbstractRestAction(String host) {
		this(host, "80");
	}

	/**
	 * Performs a GET request.
	 *
	 * @param uri
	 *            The URI. Should start with a /.
	 * @param responseType
	 *            The response type.
	 * @return The retrieved entity.
	 * @throws RuntimeException
	 *             If the response code was not a 2xx.
	 */
	protected <T> T get(String uri, Class<T> responseType) throws RuntimeException {
		ResponseEntity<T> response = restTemplate.getForEntity("http://" + host + ":" + port + uri, responseType);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new RuntimeException("Return code was " + response.getStatusCode());
		}

		return response.getBody();
	}

	/**
	 * Performs a POST request.
	 *
	 * @param uri
	 *            The URI. Should start with a /.
	 * @param responseType
	 *            The response type.
	 * @param body
	 *            The body to be sent.
	 * @return The retrieved entity.
	 * @throws RuntimeException
	 *             If the response code was not a 2xx.
	 */
	protected <T, S> T post(String uri, Class<T> responseType, S body) throws RuntimeException {
		ResponseEntity<T> response = restTemplate.postForEntity("http://" + host + ":" + port + uri, body, responseType);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new RuntimeException("Return code was " + response.getStatusCode());
		}

		return response.getBody();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return host + ":" + port;
	}

}
