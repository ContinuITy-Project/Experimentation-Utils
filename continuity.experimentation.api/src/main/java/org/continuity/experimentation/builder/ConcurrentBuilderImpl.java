package org.continuity.experimentation.builder;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import org.continuity.experimentation.IExperimentAction;
import org.continuity.experimentation.IExperimentElement;
import org.continuity.experimentation.element.ConcurrentElement;

/**
 * @author Henning Schulz
 *
 */
public class ConcurrentBuilderImpl<C> extends AbstractExperimentBuilder<C> implements ConcurrentBuilder<C> {

	private final ConcurrentElement concurrentElement = new ConcurrentElement();

	public ConcurrentBuilderImpl(C caller, Consumer<IExperimentElement> createdConsumer) {
		super(caller, createdConsumer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConcurrentBuilder<C> append(IExperimentAction action) {
		appendAction(action);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LoopBuilder<ConcurrentBuilder<C>> loop(int numIterations) {
		return new LoopBuilderImpl<>(this, this::appendElement, numIterations);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IfBranchBuilder<ConcurrentBuilder<C>> ifThen(BooleanSupplier condition) {
		return new BranchBuilderImpl<>(this, this::appendElement, condition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConcurrentBuilder<C> newThread() {
		finishCurrentThread();
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public C join() {
		finishCurrentThread();
		return returnToCaller(concurrentElement, concurrentElement.getJoin());
	}

	private void finishCurrentThread() {
		concurrentElement.addThread(getFirst());
		getCurrent().setNextOrFail(IExperimentElement.END);

		reset();
	}

}
