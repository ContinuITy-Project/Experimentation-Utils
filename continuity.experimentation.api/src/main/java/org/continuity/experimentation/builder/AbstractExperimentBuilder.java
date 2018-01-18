package org.continuity.experimentation.builder;

import java.util.function.Consumer;

import org.continuity.experimentation.IExperimentAction;
import org.continuity.experimentation.IExperimentElement;
import org.continuity.experimentation.element.ExperimentActionElement;

/**
 * @author Henning Schulz
 *
 */
public abstract class AbstractExperimentBuilder<C> {

	private final C caller;

	private final Consumer<IExperimentElement> createdConsumer;

	private IExperimentElement first;
	private IExperimentElement current;

	public AbstractExperimentBuilder(C caller, Consumer<IExperimentElement> createdConsumer) {
		this.caller = caller;
		this.createdConsumer = createdConsumer;
	}

	protected void appendAction(IExperimentAction action) {
		appendElement(new ExperimentActionElement(action));
	}

	protected void appendElement(IExperimentElement element) {
		if (current == null) {
			first = element;
		} else {
			current.setNextOrFail(element);
		}

		current = element;
	}

	protected IExperimentElement getFirst() {
		return first;
	}

	protected IExperimentElement getCurrent() {
		return this.current;
	}

	protected void setCurrent(IExperimentElement current) {
		this.current = current;
	}

	protected void reset() {
		first = null;
		current = null;
	}

	protected C returnToCaller(IExperimentElement created, IExperimentElement last) {
		createdConsumer.accept(created);

		return caller;
	}

}
