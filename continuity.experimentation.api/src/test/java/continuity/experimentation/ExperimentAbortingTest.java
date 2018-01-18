package continuity.experimentation;

import java.io.File;
import java.nio.file.Path;

import org.continuity.experimentation.Experiment;
import org.continuity.experimentation.action.Delay;
import org.continuity.experimentation.exception.AbortException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import continuity.experimentation.action.AbortingAction;

/**
 * @author Henning Schulz
 *
 */
public class ExperimentAbortingTest {

	private Experiment experiment;

	private AbortingAction abortingAction;

	private MockedContext context;

	@Before
	public void setup() {
		abortingAction = new AbortingAction();

		Path pathMock = Mockito.mock(Path.class);
		File fileMock = Mockito.mock(File.class);
		Mockito.when(pathMock.toFile()).thenReturn(fileMock);
		context = new MockedContext(pathMock);

		experiment = Experiment.newExperiment("ExperimentAbortingTest") //
				.loop(5) //
				.append(new Delay(1)) //
				.newThread().append(new Delay(1)).append(abortingAction) //
				.newThread().append(new Delay(1)).join() //
				.ifThen(() -> true).append(abortingAction).endIf() // end branch
				.endLoop() // end loop
				.build();
	}

	@Test
	public void testNotAborting() throws AbortException {
		experiment.execute(context);
	}

	@Test
	public void testAbortingInner() throws AbortException {
		abortingAction.setAbortInner(true);
		experiment.execute(context);
	}

	@Test(expected = AbortException.class)
	public void testAborting() throws AbortException {
		abortingAction.setAbort(true);
		experiment.execute(context);
	}

}
