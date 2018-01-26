package org.continuity.experimentation.element;

import java.util.Collection;
import java.util.Collections;

import org.continuity.experimentation.Context;
import org.continuity.experimentation.IExperimentAction;
import org.continuity.experimentation.IExperimentElement;
import org.continuity.experimentation.exception.AbortInnerException;

/**
 * Element holding an action.
 *
 * @author Henning Schulz
 *
 */
public class ExperimentActionElement implements IExperimentElement {

	private final IExperimentAction action;

	private IExperimentElement next;

	/**
	 * Constructor setting the action.
	 *
	 * @param action
	 *            The action to be executed.
	 */
	public ExperimentActionElement(IExperimentAction action) {
		this.action = action;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasAction() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExperimentAction getAction() {
		return action;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExperimentElement getNext() {
		return next;
	}

	/**
	 * Sets {@link #next}.
	 *
	 * @param next
	 *            New value for {@link #next}
	 */
	public void setNext(IExperimentElement next) {
		this.next = next;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNextOrFail(IExperimentElement next) throws UnsupportedOperationException {
		setNext(next);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double count() {
		if (next == null) {
			return 1;
		} else {
			return 1 + next.count();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(String prefix) {
		StringBuilder builder = new StringBuilder();

		builder.append(prefix);
		builder.append(action.getClass().getSimpleName());
		builder.append(": ");
		builder.append(action.toString());

		if (next != null) {
			builder.append("\n");
			builder.append(next.toString(prefix));
		}

		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return toString("");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateContext(Context context) {
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExperimentElement handleAborted(AbortInnerException exception) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<IExperimentElement> iterateToNext() {
		return Collections.singleton(next);
	}

}
