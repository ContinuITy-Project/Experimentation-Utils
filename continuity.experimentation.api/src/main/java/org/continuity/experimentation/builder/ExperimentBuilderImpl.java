package org.continuity.experimentation.builder;

import java.util.function.BooleanSupplier;

import org.continuity.experimentation.Experiment;
import org.continuity.experimentation.IExperimentAction;
import org.continuity.experimentation.IExperimentElement;

/**
 * @author Henning Schulz
 *
 */
public class ExperimentBuilderImpl extends AbstractExperimentBuilder<Experiment> implements StableExperimentBuilder {

	private String experimentName;

	public ExperimentBuilderImpl(String name) {
		super(null, null);

		this.experimentName = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StableExperimentBuilder append(IExperimentAction action) {
		appendAction(action);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LoopBuilder<StableExperimentBuilder> loop(int numIterations) {
		return new LoopBuilderImpl<>(this, this::appendElement, numIterations);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IfBranchBuilder<StableExperimentBuilder> ifThen(BooleanSupplier condition) {
		return new BranchBuilderImpl<>(this, this::appendElement, condition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConcurrentBuilder<StableExperimentBuilder> newThread() {
		return new ConcurrentBuilderImpl<>(this, this::appendElement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Experiment build() {
		getCurrent().setNextOrFail(IExperimentElement.END);
		return new Experiment(experimentName, getFirst());
	}

}
