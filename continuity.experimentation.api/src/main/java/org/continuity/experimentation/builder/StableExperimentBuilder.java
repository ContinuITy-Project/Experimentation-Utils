package org.continuity.experimentation.builder;

import org.continuity.experimentation.Experiment;

/**
 * Is the start and end point of experiment builders and allows to retrieve the built experiment.
 *
 * @author Henning Schulz
 *
 */
public interface StableExperimentBuilder extends ExperimentBuilder<StableExperimentBuilder, ConcurrentBuilder<StableExperimentBuilder>>, Branchable<IfBranchBuilder<StableExperimentBuilder>> {

	/**
	 * Constructs the built experiment.
	 *
	 * @return The built experiment.
	 */
	Experiment build();

}
