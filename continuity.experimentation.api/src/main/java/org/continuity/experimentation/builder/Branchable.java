package org.continuity.experimentation.builder;

import java.util.function.BooleanSupplier;

/**
 * Common interface for builder that can start new branches.
 *
 * @author Henning Schulz
 *
 * @param <T>
 *            Type of the builder to be used for building if/else branches.
 *
 */
public interface Branchable<T> {


	/**
	 * Adds a new if branch.
	 *
	 * @param condition
	 *            The condition that must be {@code true} to execute this branch.
	 * @return A builder for adding elements to the branch.
	 */
	T ifThen(BooleanSupplier condition);

	/**
	 * Adds a new if branch.
	 *
	 * @param name
	 *            Name of the branch.
	 * @param condition
	 *            The condition that must be {@code true} to execute this branch.
	 * @return A builder for adding elements to the branch.
	 */
	T ifThen(String name, BooleanSupplier condition);

}
