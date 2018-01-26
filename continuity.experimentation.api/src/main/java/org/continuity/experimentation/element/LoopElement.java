package org.continuity.experimentation.element;

import java.util.Collection;
import java.util.Collections;

import org.continuity.experimentation.Context;
import org.continuity.experimentation.IExperimentElement;
import org.continuity.experimentation.exception.AbortInnerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loop in the experiment chain.
 *
 * @author Henning Schulz
 *
 */
public class LoopElement implements IExperimentElement {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoopElement.class);

	private static final String PREFIX_CONTEXT = "iteration#";

	private IExperimentElement loopStart;

	private IExperimentElement afterLoop;

	private final int numIterations;

	private int currentIteration = 1;

	public LoopElement(int numIterations) {
		this.numIterations = numIterations;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateContext(Context context) {
		if (currentIteration > 1) {
			context.remove(PREFIX_CONTEXT + (currentIteration - 1));
		}

		if (currentIteration <= numIterations) {
			context.append(this, PREFIX_CONTEXT + currentIteration);
		}
	}

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
	public IExperimentElement getNext() {
		int it = currentIteration++;

		if (it <= numIterations) {
			return loopStart;
		} else {
			return afterLoop;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNextOrFail(IExperimentElement next) throws UnsupportedOperationException {
		setAfterLoop(next);
	}

	/**
	 * Sets {@link #loopStart}.
	 *
	 * @param loopStart
	 *            New value for {@link #loopStart}
	 */
	public void setLoopStart(IExperimentElement loopStart) {
		this.loopStart = loopStart;
	}

	/**
	 * Sets {@link #afterLoop}.
	 *
	 * @param afterLoop
	 *            New value for {@link #afterLoop}
	 */
	public void setAfterLoop(IExperimentElement afterLoop) {
		this.afterLoop = afterLoop;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double count() {
		return (loopStart.count() * numIterations) + (afterLoop == null ? 0 : afterLoop.count());
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
	public String toString(String prefix) {
		StringBuilder builder = new StringBuilder();

		builder.append(prefix);
		builder.append("LOOP (");
		builder.append(numIterations);
		builder.append(" iterations):\n");
		builder.append(loopStart.toString(prefix + SHIFTING));

		builder.append(prefix);
		builder.append("END-LOOP");

		if (afterLoop != null) {
			builder.append("\n");
			builder.append(afterLoop.toString(prefix));
		}

		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExperimentElement handleAborted(AbortInnerException exception) {
		LOGGER.info("Handling a {} and continuing with the next iteration.", exception.getClass().getSimpleName());
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<IExperimentElement> iterateToNext() {
		return Collections.singleton(loopStart);
	}

	/**
	 * Gets an {@link IExperimentElement} to be added as successor of the last element in the loop.
	 *
	 * @return An element representing the end of a loop.
	 */
	public IExperimentElement getLoopEnd() {
		return new End(this);
	}

	private final class End implements IExperimentElement {

		private final LoopElement loop;

		private End(LoopElement loop) {
			this.loop = loop;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void updateContext(Context context) {
			loop.updateContext(context);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasAction() {
			return loop.hasAction();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public IExperimentElement getNext() {
			return loop.getNext();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setNextOrFail(IExperimentElement next) throws UnsupportedOperationException {
			loop.setNextOrFail(next);
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
		public String toString(String newLinePrefix) {
			return "";
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return "END-LOOP";
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public IExperimentElement handleAborted(AbortInnerException exception) {
			return loop.handleAborted(exception);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Collection<IExperimentElement> iterateToNext() {
			return Collections.singleton(loop.afterLoop);
		}

	}

}
