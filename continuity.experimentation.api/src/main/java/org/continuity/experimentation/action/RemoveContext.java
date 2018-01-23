package org.continuity.experimentation.action;

import org.continuity.experimentation.Context;
import org.continuity.experimentation.IExperimentAction;

/**
 * Removes a context.
 *
 * @author Henning Schulz
 *
 */
public class RemoveContext implements IExperimentAction {

	private final String context;

	public RemoveContext(String context) {
		this.context = context;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context) {
		context.remove(this.context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Remove context \"" + context + "\"";
	}

}
