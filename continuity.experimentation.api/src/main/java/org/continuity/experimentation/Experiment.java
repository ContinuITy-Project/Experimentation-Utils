package org.continuity.experimentation;

/**
 * An Experiment holds a name and the first {@link IExperimentElement} to be executed. Each element
 * can, but need not, hold an {@link IExperimentAction}.
 *
 * @author Henning Schulz
 *
 */
public class Experiment {

	private final String name;

	private IExperimentElement first;

	public Experiment() {
		this("experiment");
	}

	public Experiment(String name) {
		this.name = name;
	}

	/**
	 * Executes the experiment.
	 *
	 */
	public void execute() {
		execute(new Context());
	}

	/**
	 * Executes the experiment in an initial context.
	 *
	 * @param context
	 *            The initial context.
	 */
	public void execute(Context context) {
		context.append(name);

		IExperimentElement current = first;

		while ((current != null) && !current.isEnd()) {
			current.updateContext(context);

			context.toPath().toFile().mkdirs();

			if (current.hasAction()) {
				current.getAction().execute(context);
			}

			current = current.getNext();
		}

		context.remove(name);
	}

	/**
	 * Sets {@link #first}.
	 *
	 * @param first
	 *            New value for {@link #first}
	 */
	public void setFirst(IExperimentElement first) {
		this.first = first;
	}

	/**
	 * Gets the number of experiment actions to be executed.
	 *
	 * @return The number of actions.
	 */
	public double getNumberOfActions() {
		return first.count();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Experiment \"" + name + "\" (" + getNumberOfActions() + " steps):\n" + first.toString();
	}

}
