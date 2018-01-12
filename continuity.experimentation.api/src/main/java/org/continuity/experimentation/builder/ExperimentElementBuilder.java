package org.continuity.experimentation.builder;

import org.continuity.experimentation.IExperimentAction;
import org.continuity.experimentation.IExperimentElement;
import org.continuity.experimentation.element.ExperimentActionElement;

/**
 * Builder for single {@link IExperimentElement}s.
 *
 * @author Henning Schulz
 *
 */
public class ExperimentElementBuilder<C extends IExperimentBuilder> extends AbstractExperimentElementBuilder<C> implements IExperimentBuilder {

	private IExperimentElement first;
	private IExperimentElement current;

	public ExperimentElementBuilder(C caller) {
		super(caller);
	}

	/**
	 * Appends a new experiment step with an action.
	 *
	 * @param action
	 *            The action to be executed.
	 * @return A builder for adding subsequent experiment elements.
	 */
	public ExperimentElementBuilder<C> append(IExperimentAction action) {
		ExperimentActionElement next = new ExperimentActionElement(action);

		if (current == null) {
			first = next;
		} else {
			current.setNextOrFail(next);
		}

		current = next;

		return this;
	}

	/**
	 * Adds a branch.
	 *
	 * @return A builder for specifying the branch.
	 */
	public BranchBuilder<ExperimentElementBuilder<C>> branch() {
		return new BranchBuilder<>(this);
	}

	/**
	 * Adds a loop.
	 *
	 * @param numIterations
	 *            The number of iterations of the loop.
	 * @return A builder for specifying the loop.
	 */
	public ExperimentElementBuilder<ExperimentElementBuilder<C>> loop(int numIterations) {
		IExperimentBuilder loopBuilder = new LoopBuilder<>(this, numIterations);
		ExperimentElementBuilder<ExperimentElementBuilder<C>> insideLoopBuilder = new ExperimentElementBuilder<ExperimentElementBuilder<C>>(this);
		insideLoopBuilder.addInterBuilder(loopBuilder);
		return insideLoopBuilder;
	}

	/**
	 * Adds a {@code ConcurrentElement} for executing parallel threads.
	 *
	 * @return A builder for specifying the threads.
	 */
	public ConcurrentBuilder<ExperimentElementBuilder<C>> concurrent() {
		return new ConcurrentBuilder<>(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onReturn(IExperimentElement result, IExperimentElement last) {
		if (result != null) {
			if (current == null) {
				first = result;
			} else {
				current.setNextOrFail(result);
			}
		}

		current = last;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExperimentElement getLast() {
		return current;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExperimentElement getResult() {
		return this.first;
	}

}
