package org.continuity.experimentation.builder;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import org.continuity.experimentation.IExperimentAction;
import org.continuity.experimentation.IExperimentElement;
import org.continuity.experimentation.element.LoopElement;

/**
 * @author Henning Schulz
 *
 */
public class LoopBuilderImpl<C> extends AbstractExperimentBuilder<C> implements LoopBuilder<C> {

	private final int numIterations;

	public LoopBuilderImpl(C caller, Consumer<IExperimentElement> createdConsumer, int numIterations) {
		super(caller, createdConsumer);

		this.numIterations = numIterations;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LoopBuilder<C> append(IExperimentAction action) {
		appendAction(action);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LoopBuilder<LoopBuilder<C>> loop(int numIterations) {
		return new LoopBuilderImpl<>(this, this::appendElement, numIterations);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IfBranchBuilder<LoopBuilder<C>> ifThen(BooleanSupplier condition) {
		return new BranchBuilderImpl<>(this, this::appendElement, condition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConcurrentBuilder<LoopBuilder<C>> newThread() {
		return new ConcurrentBuilderImpl<>(this, this::appendElement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public C endLoop() {
		LoopElement result = new LoopElement(numIterations);
		result.setLoopStart(getFirst());
		getCurrent().setNextOrFail(result);

		return returnToCaller(result, result);
	}

}
