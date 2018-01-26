package org.continuity.experimentation;

import org.continuity.experimentation.exception.AbortException;
import org.continuity.experimentation.exception.AbortInnerException;

/**
 * Common interface for experiment actions. That is, an action that is to be executed during the
 * experiment.
 *
 * @author Henning Schulz
 *
 */
public interface IExperimentAction {

	/**
	 * Executes the experiment action.
	 *
	 * @param context
	 *            The current context.
	 * @throws AbortInnerException
	 *             If the inner element (loop, concurrent) should be aborted.
	 * @throws AbortException
	 *             If the whole experiment should be aborted.
	 * @throws Exception
	 */
	void execute(Context context) throws AbortInnerException, AbortException, Exception;

	/**
	 * To be called by the experiment builder after the experiment has been created.
	 *
	 * @param experiment
	 *            The built experiment.
	 */
	default void bypassExperiment(Experiment experiment) {
		// do nothing by default
	}

}
