package org.continuity.experimentation.action.continuity;

import java.util.HashMap;
import java.util.Map;

import org.continuity.experimentation.action.AbstractRestAction;
import org.continuity.experimentation.data.IDataHolder;

/**
 * @author Henning Schulz
 *
 */
public class WorkloadTransformationAndExecution extends AbstractRestAction {

	private final String loadTestType;

	private final IDataHolder<String> tag;

	private final IDataHolder<String> workloadLink;

	public WorkloadTransformationAndExecution(String host, String port, String loadTestType, IDataHolder<String> tag, IDataHolder<String> workloadLink) {
		super(host, port);

		this.loadTestType = loadTestType;
		this.tag = tag;
		this.workloadLink = workloadLink;
	}

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

		post("loadtest/" + loadTestType + "/createandexecute", String.class, message);
	}

}
