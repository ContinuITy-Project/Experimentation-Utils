package org.continuity.experimentation.action.continuity;

import java.util.HashMap;
import java.util.Map;

import org.continuity.experimentation.Context;
import org.continuity.experimentation.action.AbstractRestAction;
import org.continuity.experimentation.data.IDataHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Causes generation of a new workload model based on a data link and stores the link to the
 * generated workload in an output.
 *
 * @author Henning Schulz
 *
 */
public class WorkloadModelGeneration extends AbstractRestAction {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkloadModelGeneration.class);

	private final String wmType;
	private final String tag;

	private final IDataHolder<String> dataLink;
	private final IDataHolder<String> workloadLink;

	/**
	 * Creates a new workload model generation action.
	 *
	 * @param restTemplate
	 *            The {@link RestTemplate} to be used. If it is {@code null}, a default template
	 *            will be instantiated.
	 * @param host
	 *            The hostname or IP of the ContinuITy frontend.
	 * @param port
	 *            The port of the ContinuITy frontend.
	 * @param wmType
	 *            The type of the workload model (e.g., wessbas).
	 * @param tag
	 *            The tag to be used for the workload model.
	 * @param dataLink
	 *            Input data holder: The link to be used to retrieve the monitoring data.
	 * @param workloadLink
	 *            Output data holder: The link to the generated workload model. To be used with the
	 *            frontend: {@code<fontend-url>/workloadmodel/get/<workload-link>}.
	 */
	public WorkloadModelGeneration(RestTemplate restTemplate, String host, String port, String wmType, String tag, IDataHolder<String> dataLink, IDataHolder<String> workloadLink) {
		super(host, port, restTemplate);

		this.wmType = wmType;
		this.tag = tag;
		this.dataLink = dataLink;
		this.workloadLink = workloadLink;
	}

	/**
	 * Creates a new workload model generation action.
	 *
	 * @param host
	 *            The hostname or IP of the ContinuITy frontend.
	 * @param port
	 *            The port of the ContinuITy frontend.
	 * @param wmType
	 *            The type of the workload model (e.g., wessbas).
	 * @param tag
	 *            The tag to be used for the workload model.
	 * @param dataLink
	 *            Input data holder: The link to be used to retrieve the monitoring data.
	 * @param workloadLink
	 *            Output data holder: The link to the generated workload model. To be used with the
	 *            frontend: {@code<fontend-url>/workloadmodel/get/<workload-link>}.
	 */
	public WorkloadModelGeneration(String host, String port, String wmType, String tag, IDataHolder<String> dataLink, IDataHolder<String> workloadLink) {
		this(null, host, port, wmType, tag, dataLink, workloadLink);
	}

	/**
	 * Creates a new workload model generation action using the default port 80.
	 *
	 * @param host
	 *            The hostname or IP of the ContinuITy frontend.
	 * @param wmType
	 *            The type of the workload model (e.g., wessbas).
	 * @param tag
	 *            The tag to be used for the workload model.
	 * @param dataLink
	 *            Input data holder: The link to be used to retrieve the monitoring data.
	 * @param workloadLink
	 *            Output data holder: The link to the generated workload model. To be used with the
	 *            frontend: {@code<fontend-url>/workloadmodel/get/<workload-link>}.
	 */
	public WorkloadModelGeneration(String host, String wmType, String tag, IDataHolder<String> dataLink, IDataHolder<String> workloadLink) {
		this(host, "80", wmType, tag, dataLink, workloadLink);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context) {
		Map<String, String> body = new HashMap<>();
		body.put("data", dataLink.get());
		body.put("tag", tag);

		Map<?, ?> reponse = post("/workloadmodel/" + wmType + "/create", Map.class, body);

		String message = reponse.get("message").toString();
		String link = reponse.get("link").toString();

		if (link == null) {
			LOGGER.error("The response did not contain a link! Message from server: '{}'", message);
		} else {
			LOGGER.info("Workload model creation initiated. Message from server is '{}' and link is '{}'", message, link);
		}

		workloadLink.set(link);
	}

}
