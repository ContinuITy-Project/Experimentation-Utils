package org.continuity.experimentation.action;

/**
 * Encapsulates an {@link AppendContext} and {@link RemoveContext} for one context.
 *
 * @author Henning Schulz
 *
 */
public class ContextChange {

	private final String context;

	public ContextChange(String context) {
		this.context = context;
	}

	public AppendContext append() {
		return new AppendContext(context);
	}

	public RemoveContext remove() {
		return new RemoveContext(context);
	}

}
