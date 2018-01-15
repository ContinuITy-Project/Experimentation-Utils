package org.continuity.experimentation.builder;

import org.continuity.experimentation.Experiment;
import org.continuity.experimentation.IExperimentElement;

/**
 * A builder for {@link Experiment}s.
 *
 * @author Henning Schulz
 *
 */
public class ExperimentBuilder extends AbstractExperimentBuilder {

	private String experimentName;
	private ExperimentElementBuilder<ExperimentBuilder> elementBuilder;

	public ExperimentElementBuilder<ExperimentBuilder> newExperiment(String name) {
		experimentName = name;
		elementBuilder = new ExperimentElementBuilder<>(this);
		return elementBuilder;
	}

	/**
	 * Build the experiment.
	 *
	 * @return The constructed experiment.
	 */
	public Experiment build() {
		return new Experiment(experimentName, elementBuilder.getResult());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onReturn(IExperimentElement result, IExperimentElement last) {
		last.setNextOrFail(IExperimentElement.END);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExperimentElement getResult() {
		return elementBuilder.getResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExperimentElement getLast() {
		return elementBuilder.getLast();
	}

}
