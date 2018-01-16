package org.continuity.experimentation.exception;

import java.util.Objects;

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

	private static final String MESSAGE = "The inner element has been aborted du to '{}'. The current context was: {}";

	private static final String PATTERN = "\\{\\}";

	/**
	 * Constructor.
	 *
	 * @param context
	 *            The current context.
	 * @param cause
	 *            The originally thrown exception.
	 */
	public AbortInnerException(Context context, Exception cause) {
		super(MESSAGE.replaceFirst(PATTERN, cause.getClass().getSimpleName()).replaceFirst(PATTERN, context.toString()), cause);
	}

	/**
	 * Constructor.
	 *
	 * @param context
	 *            The current context.
	 * @param additionalMessage
	 *            An additional message to be appended.
	 */
	public AbortInnerException(Context context, String additionalMessage) {
		super(MESSAGE.replaceFirst(PATTERN, additionalMessage).replaceFirst(PATTERN, Objects.toString(context)));
	}

}
