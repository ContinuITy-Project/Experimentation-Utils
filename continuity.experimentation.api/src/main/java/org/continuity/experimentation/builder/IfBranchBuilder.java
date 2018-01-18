package org.continuity.experimentation.builder;

/**
 * Builds if branches.
 *
 * @author Henning Schulz
 *
 * @param <C>
 *            Type of the builder that called this builder.
 *
 */
public interface IfBranchBuilder<C> extends ExperimentBuilder<IfBranchBuilder<C>, ConcurrentBuilder<IfBranchBuilder<C>>>, Branchable<IfBranchBuilder<C>> {

	/**
	 * Adds an else branch that is executed if none of the conditions of the if branches hold.
	 *
	 * @return A builder for adding elements to the branch.
	 */
	ElseBranchBuilder<C> elseThen();

	/**
	 * Ends the if/else block.
	 *
	 * @return A builder for adding elements after the if/else block.
	 */
	C endIf();

}
