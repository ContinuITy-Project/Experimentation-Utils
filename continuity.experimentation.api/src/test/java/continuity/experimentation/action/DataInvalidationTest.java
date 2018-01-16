package continuity.experimentation.action;

import org.continuity.experimentation.Experiment;
import org.continuity.experimentation.action.DataInvalidation;
import org.continuity.experimentation.builder.ExperimentBuilder;
import org.continuity.experimentation.data.SimpleDataHolder;
import org.continuity.experimentation.exception.AbortException;
import org.continuity.experimentation.exception.AbortInnerException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Henning Schulz
 *
 */
public class DataInvalidationTest {

	private Experiment experiment;

	private SimpleDataHolder<String> str1 = new SimpleDataHolder<>("str1", "A");
	private SimpleDataHolder<String> str2 = new SimpleDataHolder<>("str2", String.class);

	@Before
	public void setupExperiment() {
		DummyExperimentAction action = new DummyExperimentAction(str1, str2, "B");
		DataInvalidation invalidation = new DataInvalidation(str1);

		ExperimentBuilder builder = new ExperimentBuilder();
		experiment = builder.newExperiment("Invalidation").append(action).append(invalidation).end().build();
	}

	@Test(expected = AbortInnerException.class)
	public void test() throws AbortException, AbortInnerException {
		experiment.execute();

		Assert.assertEquals("Expected action to write B to str2.", "B", str2.get());

		str1.get();
	}

}
