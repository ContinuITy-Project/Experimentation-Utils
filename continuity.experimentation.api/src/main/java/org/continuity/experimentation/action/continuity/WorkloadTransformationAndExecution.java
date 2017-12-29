package org.continuity.experimentation.action.continuity;

import java.util.HashMap;
import java.util.Map;

import org.continuity.experimentation.action.AbstractRestAction;
import org.continuity.experimentation.data.IDataHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transforms a workload model into a load test and executes it.
 *
 * @author Henning Schulz
 *
 */
public class WorkloadTransformationAndExecution extends AbstractRestAction {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkloadTransformationAndExecution.class);

	private final String loadTestType;

	private final IDataHolder<String> tag;

	private final IDataHolder<String> workloadLink;

	/**
	 * Constructor.
	 *
	 * @param host
	 *            The host of the ContinuITy frontend.
	 * @param port
	 *            The port of the ContinuITy frontend.
	 * @param loadTestType
	 *            The type of the load test (e.g., jmeter).
	 * @param tag
	 *            The tag of the annotation to be used.
	 * @param workloadLink
	 *            The link to the workload model.
	 */
	public WorkloadTransformationAndExecution(String host, String port, String loadTestType, IDataHolder<String> tag, IDataHolder<String> workloadLink) {
		super(host, port);

		this.loadTestType = loadTestType;
		this.tag = tag;
		this.workloadLink = workloadLink;
	}

	/**
	 * Constructor using the default port 8080.
	 *
	 * @param host
	 *            The host of the ContinuITy frontend.
	 * @param loadTestType
	 *            The type of the load test (e.g., jmeter).
	 * @param tag
	 *            The tag of the annotation to be used.
	 * @param workloadLink
	 *            The link to the workload model.
	 */
	public WorkloadTransformationAndExecution(String host, String loadTestType, IDataHolder<String> tag, IDataHolder<String> workloadLink) {
		this(host, "8080", loadTestType, tag, workloadLink);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute() {
		Map<String, String> message = new HashMap<>();
		message.put("tag", tag.get());
		message.put("workload-link", workloadLink.get());

		String response = post("loadtest/" + loadTestType + "/createandexecute", String.class, message);
		LOGGER.info("Response from frontend: {}", response);
	}

}
