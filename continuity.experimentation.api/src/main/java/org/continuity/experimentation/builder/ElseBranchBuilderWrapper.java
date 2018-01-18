package org.continuity.experimentation.builder;

import org.continuity.experimentation.IExperimentAction;

/**
 * Wrapper around a {@link BranchBuilderImpl} for building else branches without allowing a user to
 * call {@link ifThen()} or {@link elseThen()}.
 *
 * @author Henning Schulz
 *
 */
public class ElseBranchBuilderWrapper<C> implements ElseBranchBuilder<C> {

	private final BranchBuilderImpl<C> wrapped;

	public ElseBranchBuilderWrapper(BranchBuilderImpl<C> wrapped) {
		this.wrapped = wrapped;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ElseBranchBuilder<C> append(IExperimentAction action) {
		wrapped.append(action);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LoopBuilder<ElseBranchBuilder<C>> loop(int numIterations) {
		return new LoopBuilderImpl<>(this, wrapped::appendElement, numIterations);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConcurrentBuilder<ElseBranchBuilder<C>> newThread() {
		return new ConcurrentBuilderImpl<>(this, wrapped::appendElement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public C endIf() {
		return wrapped.endIf();
	}

}
