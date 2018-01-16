package continuity.experimentation.builder;

import static org.junit.Assert.assertEquals;

import org.continuity.experimentation.Experiment;
import org.continuity.experimentation.builder.ExperimentBuilder;
import org.continuity.experimentation.data.AppendingStringHolder;
import org.continuity.experimentation.data.SimpleDataHolder;
import org.continuity.experimentation.exception.AbortException;
import org.continuity.experimentation.exception.AbortInnerException;
import org.junit.Test;

import continuity.experimentation.action.DummyExperimentAction;

/**
 * @author Henning Schulz
 *
 */
public class BuilderTest {

	@Test
	public void test() throws AbortException, AbortInnerException {
		SimpleDataHolder<String> str1 = new SimpleDataHolder<>("str1", "Hi, how are you?");
		SimpleDataHolder<String> str2 = new SimpleDataHolder<>("str2", String.class);
		SimpleDataHolder<String> str3 = new SimpleDataHolder<>("str3", String.class);

		AppendingStringHolder counter = new AppendingStringHolder("counter");

		DummyExperimentAction step1 = new DummyExperimentAction(str1, str2, "Hello");
		DummyExperimentAction step2 = new DummyExperimentAction(str2, str3, "I'm fine.");
		DummyExperimentAction step3 = new DummyExperimentAction(str2, str3, "I'm not feeling good.");

		DummyExperimentAction countStep = new DummyExperimentAction(counter, counter, "|");

		ExperimentBuilder builder = new ExperimentBuilder();
		Experiment experiment = builder.newExperiment("My Experiment") //
				.branch().elseThen().append(step1) //
				.branch().ifThen(() -> true).append(step2).end().elseThen().append(step3).end() //
				.loop(5).append(countStep).end() //
				.end().end() //
				.build();

		System.out.println(experiment);

		experiment.execute();

		assertEquals("Step one should output 'Hello'.", "Hello", str2.get());
		assertEquals("Step two should output 'I'm fine'.", "I'm fine.", str3.get());

		assertEquals("The loop should count to 5.", "|||||", counter.get());

		assertEquals("The experiment should contain x elements", 7.0, experiment.getNumberOfActions(), 0.0001);

		Experiment experiment2 = builder.newExperiment("My Experiment") //
				.append(step1) //
				.branch().ifThen(() -> false).append(step2).end().elseThen().append(step3).end() //
				.loop(2).append(countStep).end() //
				.end() //
				.build();
		experiment2.execute();

		assertEquals("Step one should output 'Hello'.", "Hello", str2.get());
		assertEquals("Step two should output 'I'm not feeling good.'.", "I'm not feeling good.", str3.get());

		assertEquals("The loop should count to 7.", "|||||||", counter.get());
	}

}
