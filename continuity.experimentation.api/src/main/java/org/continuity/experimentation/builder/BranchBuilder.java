package org.continuity.experimentation.builder;

import java.util.function.BooleanSupplier;

import org.continuity.experimentation.IExperimentElement;
import org.continuity.experimentation.element.BranchElement;

/**
 * Builder for adding branches.
 *
 * @author Henning Schulz
 *
 */
public class BranchBuilder<C extends IExperimentBuilder> extends AbstractExperimentElementBuilder<C> implements IExperimentBuilder {

	private BranchElement branch = new BranchElement();

	private BooleanSupplier currentCondition = null;

	private boolean currentIsElse = false;

	public BranchBuilder(C caller) {
		super(caller);
	}

	/**
	 * Adds an if branch with the specified condition.
	 *
	 * @param condition
	 *            The condition of the branch.
	 * @return A builder for filling the branch.
	 */
	public ExperimentElementBuilder<BranchBuilder<C>> ifThen(BooleanSupplier condition) {
		currentCondition = condition;
		return new ExperimentElementBuilder<BranchBuilder<C>>(this);
	}

	/**
	 * Adds an else branch.
	 *
	 * @return A builder for filling the branch.
	 */
	public ExperimentElementBuilder<C> elseThen() {
		currentIsElse = true;
		ExperimentElementBuilder<C> elseBuilder = new ExperimentElementBuilder<>(getCaller());
		elseBuilder.addInterBuilder(this);
		return elseBuilder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onReturn(IExperimentElement result, IExperimentElement last) {
		if (result != null) {
			if (currentIsElse) {
				branch.setElseBranch(result);
			} else {
				branch.addBranch(currentCondition, result);
			}

			last.setNextOrFail(branch.getJoin());
		}

		currentCondition = null;
		currentIsElse = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExperimentElement getLast() {
		return branch.getJoin();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExperimentElement getResult() {
		if (branch.getElseBranch() == null) {
			branch.setElseBranch(branch.getJoin());
		}

		return branch;
	}

}
