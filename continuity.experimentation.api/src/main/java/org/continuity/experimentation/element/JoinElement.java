package org.continuity.experimentation.element;

import org.continuity.experimentation.Context;
import org.continuity.experimentation.IExperimentElement;
import org.continuity.experimentation.exception.AbortInnerException;

/**
 * Can be used for joining branches, loops or forks.
 *
 * @author Henning Schulz
 *
 */
public class JoinElement implements IExperimentElement {

	private IExperimentElement next;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasAction() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateContext(Context context) {
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
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "JOIN";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(String newLinePrefix) {
		return "";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExperimentElement handleAborted(AbortInnerException exception) {
		return null;
	}

}
