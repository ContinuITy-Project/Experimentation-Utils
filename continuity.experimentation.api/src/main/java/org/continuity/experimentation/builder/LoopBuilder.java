package org.continuity.experimentation.builder;

import org.continuity.experimentation.IExperimentElement;
import org.continuity.experimentation.element.LoopElement;

/**
 * Builder for adding loops.
 *
 * @author Henning Schulz
 *
 */
public class LoopBuilder<C extends IExperimentBuilder> extends AbstractExperimentElementBuilder<C> {

	private final LoopElement loopElement;

	public LoopBuilder(C caller, int numIterations) {
		super(caller);

		this.loopElement = new LoopElement(numIterations);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onReturn(IExperimentElement result, IExperimentElement last) {
		loopElement.setLoopStart(result);
		last.setNextOrFail(loopElement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExperimentElement getResult() {
		return loopElement;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExperimentElement getLast() {
		return loopElement;
	}

}
