package continuity.experimentation.action;

import static org.junit.Assert.assertEquals;

import org.continuity.experimentation.Experiment;
import org.continuity.experimentation.data.AppendingStringHolder;
import org.continuity.experimentation.data.SimpleDataHolder;
import org.continuity.experimentation.exception.AbortException;
import org.continuity.experimentation.exception.AbortInnerException;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Henning Schulz
 *
 */
public class ConcurrentActionTest {

	private Experiment experiment;

	private SimpleDataHolder<String> str1 = new SimpleDataHolder<>("str1", "Hi, how are you?");
	private SimpleDataHolder<String> str2 = new SimpleDataHolder<>("str2", String.class);
	private SimpleDataHolder<String> str3 = new SimpleDataHolder<>("str3", String.class);
	private SimpleDataHolder<String> str4 = new SimpleDataHolder<>("str4", String.class);

	private AppendingStringHolder counter = new AppendingStringHolder("counter");

	@Before
	public void setupExperiment() {
		DummyExperimentAction step1 = new DummyExperimentAction(str1, str2, "Hello");
		DummyExperimentAction step2 = new DummyExperimentAction(str2, str3, "I'm fine.");
		DummyExperimentAction step3 = new DummyExperimentAction(str2, str4, "I'm not feeling good.");

		DummyExperimentAction countStep = new DummyExperimentAction(counter, counter, "|");

		experiment = Experiment.newExperiment("Concurrent") //
				.newThread().append(step1).append(step2) //
				.newThread().append(step3) //
				.newThread().loop(5).append(countStep).endLoop() //
				.join().build();
	}

	@Test
	public void test() throws AbortException, AbortInnerException {
		experiment.execute();

		assertEquals("Step one should output 'Hello'.", "Hello", str2.get());
		assertEquals("Step two should output 'I'm fine'.", "I'm fine.", str3.get());

		assertEquals("The loop should count to 5.", "|||||", counter.get());
	}

}
