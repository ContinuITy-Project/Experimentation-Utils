package org.continuity.experimentation.exception;

import org.continuity.experimentation.Context;

/**
 * Exception that aborts the current loop iteration or thread but does not stop the whole
 * experiment.
 *
 * @author Henning Schulz
 *
 */
public class AbortInnerException extends Exception {

	private static final long serialVersionUID = -6918165207376138297L;

	private static final String MESSAGE = "The {} has been aborted du to '{}'. The current context was: {}";

	private static final String PATTERN = "\\{\\}";

	/**
	 * Constructor.
	 *
	 * @param type
	 *            The type of inner element to be aborted (loop or concurrent).
	 * @param context
	 *            The current context.
	 * @param cause
	 *            The originally thrown exception.
	 */
	public AbortInnerException(Type type, Context context, Exception cause) {
		super(MESSAGE.replaceFirst(PATTERN, type.toString()).replaceFirst(PATTERN, cause.getClass().getSimpleName()).replaceFirst(PATTERN, context.toString()), cause);
	}

	/**
	 * Constructor.
	 *
	 * @param type
	 *            The type of inner element to be aborted (loop or concurrent).
	 * @param context
	 *            The current context.
	 * @param additionalMessage
	 *            A message to be added to the exception.
	 */
	public AbortInnerException(Type type, Context context, String additionalMessage) {
		super(MESSAGE.replaceFirst(PATTERN, type.toString()).replaceFirst(PATTERN, additionalMessage).replaceFirst(PATTERN, context.toString()));
	}

	public static enum Type {
		LOOP, CONCURRENT_ELEMENT;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return name().toLowerCase().replaceAll("\\_", " ");
		}
	}

}
