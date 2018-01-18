package org.continuity.experimentation.builder;

import org.continuity.experimentation.Experiment;
import org.continuity.experimentation.IExperimentAction;

/**
 * Common interface for builders for {@link Experiment}s.
 *
 * @author Henning Schulz
 *
 * @param <T>
 *            Type of the subinterface.
 * @param <C>
 *            Type of the builder to be used for building concurrent threads.
 */
public interface ExperimentBuilder<T, C> {

	/**
	 * Appends a new {@link IExperimentAction}.
	 *
	 * @param action
	 *            The action to be added.
	 * @return A builder for adding further elements.
	 */
	T append(IExperimentAction action);

	/**
	 * Starts a new loop.
	 *
	 * @param numIterations
	 *            The number of iterations of the loop.
	 * @return A builder for adding further elements to the loop.
	 */
	LoopBuilder<T> loop(int numIterations);

	/**
	 * Creates a new parallel thread. All opened threads can be joined again by calling
	 * {@link ConcurrentBuilder#join()}.
	 *
	 * @return A builder for adding elements to the thread.
	 */
	C newThread();

}
