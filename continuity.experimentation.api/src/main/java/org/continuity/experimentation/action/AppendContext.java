package org.continuity.experimentation.action;

import org.continuity.experimentation.Context;
import org.continuity.experimentation.IExperimentAction;

/**
 * Appends a context.
 *
 * @author Henning Schulz
 *
 */
public class AppendContext implements IExperimentAction {

	private final String context;

	public AppendContext(String context) {
		this.context = context;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context) {
		context.append(this.context);
	}

}
