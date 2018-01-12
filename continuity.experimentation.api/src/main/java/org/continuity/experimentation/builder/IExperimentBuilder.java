package org.continuity.experimentation.builder;

import org.continuity.experimentation.Experiment;
import org.continuity.experimentation.IExperimentElement;

/**
 * Common interface for builders around {@link Experiment}.
 *
 * @author Henning Schulz
 *
 */
public interface IExperimentBuilder {

	/**
	 * Gets the result of this builder. Can consist of several nested elements.
	 *
	 * @return The constructed elements.
	 */
	IExperimentElement getResult();

	/**
	 * Gets the last element of the result. That is, if the results is {@code A->B->C}, the return
	 * value is {@code C}.
	 *
	 * @return The last element of the result.
	 */
	IExperimentElement getLast();

}
