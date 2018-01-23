package org.continuity.experimentation.action.continuity;

import java.util.HashMap;
import java.util.Map;

import org.continuity.experimentation.Context;
import org.continuity.experimentation.action.AbstractRestAction;
import org.continuity.experimentation.data.IDataHolder;
import org.continuity.experimentation.exception.AbortInnerException;
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

	private final int numUsers;

	private final long duration;

	private final int rampup;

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
	 * @param numUsers
	 *            The number of users for the test.
	 * @param duration
	 *            The duration of the test in seconds.
	 * @param rampup
	 *            The ramp up time in seconds.
	 */
	public WorkloadTransformationAndExecution(String host, String port, String loadTestType, IDataHolder<String> tag, IDataHolder<String> workloadLink, int numUsers, long duration, int rampup) {
		super(host, port);

		this.loadTestType = loadTestType;
		this.tag = tag;
		this.workloadLink = workloadLink;
		this.numUsers = numUsers;
		this.duration = duration;
		this.rampup = rampup;
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
	 * @param numUsers
	 *            The number of users for the test.
	 * @param duration
	 *            The duration of the test in seconds.
	 * @param rampup
	 *            The ramp up time in seconds.
	 */
	public WorkloadTransformationAndExecution(String host, String loadTestType, IDataHolder<String> tag, IDataHolder<String> workloadLink, int numUsers, long duration, int rampup) {
		this(host, "8080", loadTestType, tag, workloadLink, numUsers, duration, rampup);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context) throws AbortInnerException {
		LOGGER.info("Transforming the workload model {} to a {} test and executing it...", workloadLink.get(), loadTestType);

		Map<String, String> message = new HashMap<>();
		message.put("tag", tag.get());
		message.put("workload-link", workloadLink.get());
		message.put("num-users", Integer.toString(numUsers));
		message.put("duration", Long.toString(duration));
		message.put("rampup", Integer.toString(rampup));

		String response = post("loadtest/" + loadTestType + "/createandexecute", String.class, message);
		LOGGER.info("Response from frontend: {}", response);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Transform the workload model at \"" + workloadLink + "\" with tag " + tag + " to a " + loadTestType + " test with " + numUsers + " users, " + duration + " s duration and " + rampup
				+ " s rampup and execute it";
	}

}
