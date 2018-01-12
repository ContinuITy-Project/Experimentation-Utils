package org.continuity.experimentation;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holds the current context. Each action can add a context that will be appended to the current
 * one. A context could be {@code iteration#3/thread#1/foo}.
 *
 * @author Henning Schulz
 *
 */
public class Context implements Cloneable {

	private static final Logger LOGGER = LoggerFactory.getLogger(Context.class);

	private final Stack<String> contextStack;

	public Context() {
		this.contextStack = new Stack<>();
	}

	protected Context(Stack<String> contextStack) {
		this.contextStack = contextStack;
	}

	/**
	 * Gets {@link #contextStack}.
	 * 
	 * @return {@link #contextStack}
	 */
	protected Stack<String> getContextStack() {
		return this.contextStack;
	}

	/**
	 * Appends a context.
	 *
	 * @param context
	 *            The context to be appended.
	 */
	public void append(String context) {
		contextStack.push(context);
		LOGGER.info("Changed context to {}", toString());
	}

	/**
	 * Removes the current context if it is equal to the passed one. Otherwise, it throws an
	 * {@link IllegalArgumentException}.
	 *
	 * @param context
	 *            The context to be removed.
	 */
	public void remove(String context) {
		if (Objects.equals(contextStack.peek(), context)) {
			contextStack.pop();

			LOGGER.info("Removed context {}. Context is now {}", context, toString());
		} else {
			throw new IllegalArgumentException("Cannot remove context " + context + "! Current context is " + toString());
		}
	}

	/**
	 * Converts the context to a path, e.g., {@code iteration#3/branch#a/foo}.
	 *
	 * @return A path representing this context.
	 */
	public Path toPath() {
		Path path = Paths.get("");

		for (String element : contextStack) {
			path = path.resolve(element);
		}

		return path;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		boolean first = true;

		for (String element : contextStack) {
			if (first) {
				first = false;
			} else {
				builder.append("/");
			}

			builder.append(element);
		}

		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Context clone() {
		return new Context((Stack<String>) contextStack.clone());
	}

}
