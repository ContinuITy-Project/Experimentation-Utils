package org.continuity.experimentation;

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
	 */
	void execute(Context context);

}
