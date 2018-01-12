package org.continuity.experimentation.builder;

import org.continuity.experimentation.IExperimentElement;

/**
 * @author Henning Schulz
 *
 */
public abstract class AbstractExperimentBuilder implements IExperimentBuilder {

	/**
	 * For internal use. Is called when the called builder returns.
	 *
	 * @param result
	 *            The result of the called builder.
	 * @param last
	 *            The last element of the result.
	 */
	protected abstract void onReturn(IExperimentElement result, IExperimentElement last);

}
