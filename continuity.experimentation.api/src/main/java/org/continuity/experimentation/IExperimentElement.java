package org.continuity.experimentation;

/**
 * Common interface for experiment elements.
 *
 * @author Henning Schulz
 *
 */
public interface IExperimentElement {

	/**
	 * Marks the end of the experiment chain.
	 */
	public static final IExperimentElement END = new IExperimentElement() {

		@Override
		public void setNextOrFail(IExperimentElement next) throws UnsupportedOperationException {
			throw new UnsupportedOperationException("Cannot set next of END!");
		}

		@Override
		public boolean hasAction() {
			return false;
		}

		@Override
		public IExperimentElement getNext() {
			return END;
		}

		@Override
		public double count() {
			return 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isEnd() {
			return true;
		}

		@Override
		public String toString(String newLinePrefix) {
			return "END";
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return "END";
		}

		@Override
		public void updateContext(Context context) {
		}
	};

	/**
	 * Appends a new element to the current context. Will be executed immediately before the held
	 * action is retrieved or the next element is visited.
	 *
	 * @param context
	 *            The current context.
	 */
	void updateContext(Context context);

	/**
	 * Returns whether the element holds an action that is to be executed.
	 *
	 * @return
	 */
	boolean hasAction();

	/**
	 * Gets the hold action or {@code null} if there is none.
	 *
	 * @return
	 */
	default IExperimentAction getAction() {
		return null;
	}

	/**
	 * Gets the next experiment element.
	 *
	 * @return
	 */
	IExperimentElement getNext();

	/**
	 * Sets the next element if possible or throws an {@link UnsupportedOperationException}
	 * otherwise.
	 *
	 * @param next
	 *            The next element.
	 * @throws UnsupportedOperationException
	 *             If setting the next element is not supported.
	 */
	void setNextOrFail(IExperimentElement next) throws UnsupportedOperationException;

	/**
	 * Counts the number actions in this element and the subsequent ones.
	 *
	 * @return The number of actions.
	 */
	double count();

	/**
	 * Returns whether this is the final element.
	 *
	 * @return {@code true} if this is {@link #END} or {@code false} otherwise.
	 */
	default boolean isEnd() {
		return false;
	}

	/**
	 * Converts the element to a string respecting a prefix for new lines.
	 *
	 * @param newLinePrefix
	 *            A prefix for new lines.
	 * @return A string representation.
	 */
	String toString(String newLinePrefix);

}
