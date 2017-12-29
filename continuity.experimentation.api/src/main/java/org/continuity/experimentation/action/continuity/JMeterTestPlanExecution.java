package org.continuity.experimentation.action.continuity;

import java.util.Map;

import org.continuity.experimentation.action.AbstractRestAction;
import org.continuity.experimentation.data.IDataHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Executes a JMeter test plan.
 *
 * @author Henning Schulz
 *
 */
public class JMeterTestPlanExecution extends AbstractRestAction {

	private static final Logger LOGGER = LoggerFactory.getLogger(JMeterTestPlanExecution.class);

	private final IDataHolder<TestPlanBundle> testPlanBundle;

	/**
	 * Constructor.
	 *
	 * @param host
	 *            The host of the ContinuITy frontend.
	 * @param port
	 *            The port of the ContinuITy frontend.
	 * @param testPlanBundle
	 *            The JMeter test plan bundle to be executed.
	 */
	public JMeterTestPlanExecution(String host, String port, IDataHolder<TestPlanBundle> testPlanBundle) {
		super(host, port);
		this.testPlanBundle = testPlanBundle;
	}

	/**
	 * Constructor using the default port 8080.
	 *
	 * @param host
	 *            The host of the ContinuITy frontend.
	 * @param testPlanBundle
	 *            The JMeter test plan bundle to be executed.
	 */
	public JMeterTestPlanExecution(String host, IDataHolder<TestPlanBundle> testPlanBundle) {
		this(host, "8080", testPlanBundle);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute() {
		String response = post("loadtest/jmeter/execute", String.class, testPlanBundle.get());
		LOGGER.info("Response from frontend: {}", response);
	}

	/**
	 * JMeter test plan bundle.
	 *
	 * @author Henning Schulz
	 *
	 */
	public static class TestPlanBundle {

		@JsonProperty("test-plan")
		private String testPlan;

		private Map<String, String[][]> behaviors;

		/**
		 * Gets {@link #testPlan}.
		 *
		 * @return {@link #testPlan}
		 */
		public String getTestPlan() {
			return this.testPlan;
		}

		/**
		 * Sets {@link #testPlan}.
		 *
		 * @param testPlan
		 *            New value for {@link #testPlan}
		 */
		public void setTestPlan(String testPlan) {
			this.testPlan = testPlan;
		}

		/**
		 * Gets {@link #behaviors}.
		 *
		 * @return {@link #behaviors}
		 */
		public Map<String, String[][]> getBehaviors() {
			return this.behaviors;
		}

		/**
		 * Sets {@link #behaviors}.
		 *
		 * @param behaviors
		 *            New value for {@link #behaviors}
		 */
		public void setBehaviors(Map<String, String[][]> behaviors) {
			this.behaviors = behaviors;
		}

	}

}
