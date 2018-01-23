package continuity.experimentation;

import org.continuity.experimentation.Experiment;
import org.continuity.experimentation.action.Delay;

import continuity.experimentation.action.AbortingAction;

/**
 * @author Henning Schulz
 *
 */
public class Playground {

	public static void main(String[] args) {
		AbortingAction abortingAction = new AbortingAction();

		Experiment experiment = Experiment.newExperiment("ExperimentAbortingTest") //
				.loop(5) //
				.append(new Delay(1)) //
				.newThread().append(new Delay(1)).append(abortingAction) //
				.newThread().append(new Delay(2)).join() //
				.ifThen(() -> true).append(abortingAction).loop(2).append(new Delay(1)).endLoop().append(new Delay(10)).endIf() // end
				// branch
				.ifThen(() -> true).append(abortingAction).elseThen().append(new Delay(3)).endIf() // end
				// branch
				.endLoop() // end loop
				.append(new Delay(4))
				.build();

		System.out.println(experiment);
	}

}
