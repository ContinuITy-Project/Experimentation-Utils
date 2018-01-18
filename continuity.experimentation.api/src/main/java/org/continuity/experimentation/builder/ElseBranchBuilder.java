package org.continuity.experimentation.builder;

/**
 * Builds else branches.
 *
 * @author Henning Schulz
 *
 */
public interface ElseBranchBuilder<C> extends ExperimentBuilder<ElseBranchBuilder<C>, ConcurrentBuilder<ElseBranchBuilder<C>>> {

	/**
	 * Ends the if/else block.
	 *
	 * @return A builder for adding elements after the if/else block.
	 */
	C endIf();

}
