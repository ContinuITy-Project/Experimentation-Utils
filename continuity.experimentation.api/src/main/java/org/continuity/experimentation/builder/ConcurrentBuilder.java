package org.continuity.experimentation.builder;

/**
 * Builds concurrent threads.
 *
 * @author Henning Schulz
 *
 * @param <C>
 *            Type of the builder that called this builder.
 */
public interface ConcurrentBuilder<C> extends ExperimentBuilder<ConcurrentBuilder<C>, ConcurrentBuilder<C>>, Branchable<IfBranchBuilder<ConcurrentBuilder<C>>> {

	/**
	 * Joins all currently open threads. That is, all threads that were created since the last join.
	 *
	 * @return A builder for adding synchronous elements after the concurrent part.
	 */
	C join();

}
