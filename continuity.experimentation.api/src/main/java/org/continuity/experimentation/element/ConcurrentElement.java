package org.continuity.experimentation.element;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.continuity.experimentation.Context;
import org.continuity.experimentation.Experiment;
import org.continuity.experimentation.IExperimentAction;
import org.continuity.experimentation.IExperimentElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Element for executing actions concurrently.
 *
 * @author Henning Schulz
 *
 */
public class ConcurrentElement implements IExperimentElement {

	private final JoinElement join = new JoinElement();

	private final List<IExperimentElement> threads = new ArrayList<>();

	/**
	 * Adds an element as new thread. The last element of this chain has to be {@link join}.
	 *
	 * @param thread
	 *            The element to be executed as new thread.
	 */
	public void addThread(IExperimentElement thread) {
		threads.add(thread);
	}

	/**
	 * Gets {@link #join}.
	 *
	 * @return {@link #join}
	 */
	public JoinElement getJoin() {
		return this.join;
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
		return new ThreadedAction(threads);
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
		return join;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNextOrFail(IExperimentElement next) throws UnsupportedOperationException {
		join.setNext(next);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double count() {
		return threads.stream().mapToDouble(IExperimentElement::count).sum();
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
	public String toString(String newLinePrefix) {
		char counter = 'a';
		StringBuilder builder = new StringBuilder();

		builder.append("CONCURRENT:");

		for (IExperimentElement thread : threads) {
			builder.append("\n");
			builder.append(newLinePrefix);
			builder.append("    ");
			builder.append(counter++);
			builder.append(") ");
			builder.append(thread.toString(newLinePrefix + "       "));
		}

		if (join.getNext() != null) {
			builder.append("\n");
			String nextLinePrefix = newLinePrefix;

			if (nextLinePrefix.length() >= 4) {
				// Omit the '--> '
				nextLinePrefix = nextLinePrefix.substring(0, nextLinePrefix.length() - 4);
			}

			builder.append(nextLinePrefix);
			builder.append("--> ");
			builder.append(join.getNext().toString(nextLinePrefix + "    "));
		}

		return builder.toString();
	}

	private static class ThreadedAction implements IExperimentAction {

		private static final Logger LOGGER = LoggerFactory.getLogger(ThreadedAction.class);

		private static final String PREFIX_PARALLEL = "thread#";

		private final List<IExperimentElement> threads;

		private final ExecutorService executorService;

		private ThreadedAction(List<IExperimentElement> threads) {
			this.threads = threads;
			this.executorService = Executors.newFixedThreadPool(threads.size());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void execute(Context context) {
			int i = 1;

			for (IExperimentElement thread : threads) {
				executeThread(thread, context, i);
				i++;
			}

			executorService.shutdown();

			try {
				executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				LOGGER.warn("Thread was interrupted during waiting for the concurrent actions to finish!");
				e.printStackTrace();
			}
		}

		private void executeThread(IExperimentElement first, Context context, int number) {
			executorService.execute(() -> {
				Experiment exp = new Experiment(PREFIX_PARALLEL + number);
				exp.setFirst(first);
				exp.execute(context.clone());
			});
		}

	}

}
