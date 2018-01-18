package org.continuity.experimentation.builder;

/**
 * Builds loops.
 *
 * @author Henning Schulz
 *
 * @param <C>
 *            Type of the builder that called this builder.
 */
public interface LoopBuilder<C> extends ExperimentBuilder<LoopBuilder<C>, ConcurrentBuilder<LoopBuilder<C>>>, Branchable<IfBranchBuilder<LoopBuilder<C>>> {

	/**
	 * Ends the loop.
	 *
	 * @return A builder for adding further elements after the loop.
	 */
	C endLoop();

}
