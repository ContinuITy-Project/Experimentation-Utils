package org.continuity.experimentation.builder;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import org.continuity.experimentation.IExperimentAction;
import org.continuity.experimentation.IExperimentElement;
import org.continuity.experimentation.element.BranchElement;

/**
 * @author Henning Schulz
 *
 */
public class BranchBuilderImpl<C> extends AbstractExperimentBuilder<C> implements IfBranchBuilder<C> {

	private final BranchElement branch = new BranchElement();

	private BooleanSupplier currentCondition;
	private boolean elseBranch = false;

	public BranchBuilderImpl(C caller, Consumer<IExperimentElement> createdConsumer, BooleanSupplier firstCondition) {
		super(caller, createdConsumer);

		this.currentCondition = firstCondition;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IfBranchBuilder<C> append(IExperimentAction action) {
		appendAction(action);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LoopBuilder<IfBranchBuilder<C>> loop(int numIterations) {
		return new LoopBuilderImpl<>(this, this::appendElement, numIterations);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IfBranchBuilder<C> ifThen(BooleanSupplier condition) {
		finishCurrentBranch();
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConcurrentBuilder<IfBranchBuilder<C>> newThread() {
		return new ConcurrentBuilderImpl<>(this, this::appendElement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ElseBranchBuilder<C> elseThen() {
		finishCurrentBranch();
		elseBranch = true;
		return new ElseBranchBuilderWrapper<>(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public C endIf() {
		finishCurrentBranch();
		return returnToCaller(branch, branch.getJoin());
	}

	private void finishCurrentBranch() {
		if (elseBranch) {
			branch.setElseBranch(getFirst());
		} else {
			branch.addBranch(currentCondition, getFirst());
		}

		getCurrent().setNextOrFail(branch.getJoin());

		reset();
	}

}
