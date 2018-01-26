package org.continuity.experimentation.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.continuity.experimentation.AbstractExperimentExecutor;
import org.continuity.experimentation.Context;
import org.continuity.experimentation.IExperimentAction;
import org.continuity.experimentation.IExperimentElement;
import org.continuity.experimentation.exception.AbortException;
import org.continuity.experimentation.exception.AbortInnerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Element for executing actions concurrently.
 *
 * @author Henning Schulz
 *
 */
public class ConcurrentElement implements IExperimentElement {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentElement.class);

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
		return new ThreadedAction(threads, this);
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
		return threads.stream().mapToDouble(IExperimentElement::count).sum() + (join.getNext() == null ? 0 : join.getNext().count());
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
		char counter = 'a';
		StringBuilder builder = new StringBuilder();

		for (IExperimentElement thread : threads) {
			builder.append(prefix);
			builder.append("THREAD ");
			builder.append(counter++);
			builder.append(":\n");
			builder.append(thread.toString(prefix + SHIFTING));
		}

		builder.append(prefix);
		builder.append("JOIN");

		if (join.getNext() != null) {
			builder.append("\n");
			builder.append(join.getNext().toString(prefix));
		}

		return builder.toString();
	}

	private static class ThreadedAction implements IExperimentAction {

		private static final Logger LOGGER = LoggerFactory.getLogger(ThreadedAction.class);

		private final List<IExperimentElement> threads;

		private final ExecutorService executorService;

		private final ConcurrentElement outer;

		private ThreadedAction(List<IExperimentElement> threads, ConcurrentElement outer) {
			this.threads = threads;
			this.executorService = Executors.newFixedThreadPool(threads.size());
			this.outer = outer;
		}

		/**
		 * {@inheritDoc}
		 *
		 */
		@Override
		public void execute(Context context) throws AbortException {
			final AtomicInteger counter = new AtomicInteger(1);

			List<AbortException> thrownExceptions = threads.stream().map(t -> new ThreadExecutor(outer, t, context, counter.getAndIncrement())).map(ThreadExecutor::executeCatchedWithContext)
					.filter(Objects::nonNull).collect(Collectors.toList());

			executorService.shutdown();

			try {
				executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				LOGGER.warn("Thread was interrupted during waiting for the concurrent actions to finish!");
				e.printStackTrace();
			}

			if (thrownExceptions.size() == 1) {
				throw thrownExceptions.get(0);
			} else if (!thrownExceptions.isEmpty()) {
				throw new AbortException(context, thrownExceptions.size() + " exceptions", thrownExceptions.get(0));
			}
		}

	}

	private static class ThreadExecutor extends AbstractExperimentExecutor {

		private static final Logger LOGGER = LoggerFactory.getLogger(ThreadExecutor.class);

		private static final String PREFIX_THREAD = "thread#";

		private final Context context;
		private final int number;
		private final ConcurrentElement outer;

		protected ThreadExecutor(ConcurrentElement outer, IExperimentElement first, Context context, int number) {
			super(first);
			this.context = context.clone();
			this.number = number;
			this.outer = outer;
		}

		public AbortException executeCatchedWithContext() {
			AbortException thrownException = null;
			context.append(outer, PREFIX_THREAD + number);

			try {
				execute(context);
			} catch (AbortException e) {
				thrownException = e;
				LOGGER.warn("An uncaught {} has been thrown! Passing it to the ConcurrentHandler.", e.getClass().getSimpleName());
				e.printStackTrace();
			}

			context.remove(PREFIX_THREAD + number);
			return thrownException;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExperimentElement handleAborted(AbortInnerException exception) {
		LOGGER.info("Handling a {} by stopping the thread.", exception.getClass().getSimpleName());
		return IExperimentElement.END;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<IExperimentElement> iterateToNext() {
		List<IExperimentElement> nextElements = new ArrayList<>(threads.size() + 1);
		nextElements.addAll(threads);

		if (join.getNext() != null) {
			nextElements.add(join.getNext());
		}

		return nextElements;
	}

}
