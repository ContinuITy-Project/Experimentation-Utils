package org.continuity.experimentation.builder;

import java.util.LinkedList;
import java.util.List;

import org.continuity.experimentation.IExperimentElement;

/**
 * Common base for experiment builders.
 *
 * @author Henning Schulz
 *
 */
public abstract class AbstractExperimentElementBuilder<C extends IExperimentBuilder> extends AbstractExperimentBuilder {

	private final C caller;

	private List<IExperimentBuilder> interBuilders = new LinkedList<>();

	public AbstractExperimentElementBuilder(C caller) {
		this.caller = caller;
	}

	/**
	 * Return to the previous builder.
	 *
	 * @return The previous builder.
	 */
	public C end() {
		IExperimentElement result = getResult();
		IExperimentElement last = getLast();

		for (IExperimentBuilder inter : interBuilders) {
			if (inter instanceof AbstractExperimentBuilder) {
				((AbstractExperimentBuilder) inter).onReturn(result, last);
				result = inter.getResult();
				last = inter.getLast();
			}
		}

		if (caller instanceof AbstractExperimentBuilder) {
			((AbstractExperimentBuilder) caller).onReturn(result, last);
		}

		return caller;
	}

	/**
	 * For internal use.
	 *
	 * @param interBuilder
	 *            A builder in between this and the caller of this.
	 */
	protected void addInterBuilder(IExperimentBuilder interBuilder) {
		interBuilders.add(0, interBuilder);
	}

	/**
	 * Gets {@link #caller}.
	 *
	 * @return {@link #caller}
	 */
	protected C getCaller() {
		return this.caller;
	}

}
