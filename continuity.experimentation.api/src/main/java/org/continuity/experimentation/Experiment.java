package org.continuity.experimentation;

import org.continuity.experimentation.builder.ExperimentBuilderImpl;
import org.continuity.experimentation.builder.StableExperimentBuilder;
import org.continuity.experimentation.exception.AbortException;

/**
 * An Experiment holds a name and the first {@link IExperimentElement} to be executed. Each element
 * can, but need not, hold an {@link IExperimentAction}.
 *
 * @author Henning Schulz
 *
 */
public class Experiment extends AbstractExperimentExecutor {

	private final String name;

	public Experiment(IExperimentElement first) {
		this("experiment", first);
	}

	public Experiment(String name, IExperimentElement first) {
		super(first);
		this.name = name;
	}

	/**
	 * Starts building a new experiment.
	 * 
	 * @param name
	 *            The name of the experiment to be built.
	 * @return A builder for building the experiment.
	 */
	public static StableExperimentBuilder newExperiment(String name) {
		return new ExperimentBuilderImpl(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context) throws AbortException {
		context.append(name);

		super.execute(context);

		context.remove(name);
	}

	/**
	 * Gets the number of experiment actions to be executed.
	 *
	 * @return The number of actions.
	 */
	public double getNumberOfActions() {
		return getFirst().count();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Experiment \"" + name + "\" (" + getNumberOfActions() + " steps):\n" + getFirst().toString();
	}

}
