package org.continuity.experimentation.exception;

import java.util.Objects;

import org.continuity.experimentation.Context;

/**
 * Exception that will abort the current experiment if thrown.
 *
 * @author Henning Schulz
 *
 */
public class AbortException extends Exception {

	private static final long serialVersionUID = -6918165207376138297L;

	private static final String MESSAGE = "The experiment has been aborted due to '{}'. The current context was: {}";

	private static final String PATTERN = "\\{\\}";

	/**
	 *
	 * Constructor.
	 *
	 * @param context
	 *            The current context.
	 * @param cause
	 *            The originally thrown exception.
	 */
	public AbortException(Context context, Exception cause) {
		super(MESSAGE.replaceFirst(PATTERN, (cause != null ? cause.getClass().getSimpleName() : null)).replaceFirst(PATTERN, Objects.toString(context)), cause);
	}

	/**
	 *
	 * Constructor.
	 *
	 * @param context
	 *            The current context.
	 * @param additionalMessage
	 *            A message to be added to the exception.
	 */
	public AbortException(Context context, String additionalMessage) {
		super(MESSAGE.replaceFirst(PATTERN, additionalMessage).replaceFirst(PATTERN, Objects.toString(context)));
	}

	/**
	 *
	 * Constructor. The message will be formatted as "The experiment has been aborted due to
	 * '{@code additionalMessage} ({@code cause.getClass().getSimpleName()})'. The current context
	 * was: {@code cause}".
	 *
	 * @param context
	 *            The current context.
	 * @param additionalMessage
	 *            A message to be added to the exception.
	 * @param cause
	 *            The originally thrown exception.
	 */
	public AbortException(Context context, String additionalMessage, Exception cause) {
		super(MESSAGE.replaceFirst(PATTERN, additionalMessage + " ").replaceFirst(PATTERN, Objects.toString(context)));
	}

}
