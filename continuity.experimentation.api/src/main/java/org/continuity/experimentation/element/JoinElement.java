package org.continuity.experimentation.element;

import org.continuity.experimentation.Context;
import org.continuity.experimentation.IExperimentElement;

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
		if (next == null) {
			return 0;
		} else {
			return next.count();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return toString(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(String newLinePrefix) {
		return "JOIN";
	}

}
