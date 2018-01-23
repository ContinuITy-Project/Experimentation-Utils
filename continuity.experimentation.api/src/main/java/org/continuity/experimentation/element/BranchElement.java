package org.continuity.experimentation.element;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import org.apache.commons.math3.util.Pair;
import org.continuity.experimentation.Context;
import org.continuity.experimentation.IExperimentElement;
import org.continuity.experimentation.exception.AbortInnerException;

/**
 * Represents an if-else branch in the experiment chain.
 *
 * @author Henning Schulz
 *
 */
public class BranchElement implements IExperimentElement {

	private final JoinElement join = new JoinElement();

	private List<Pair<BooleanSupplier, IExperimentElement>> branches = new ArrayList<>();

	IExperimentElement elseBranch = null;

	/**
	 * Gets {@link #join}.
	 *
	 * @return {@link #join}
	 */
	public JoinElement getJoin() {
		return this.join;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasAction() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateContext(Context context) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExperimentElement getNext() {
		for (Pair<BooleanSupplier, IExperimentElement> pair : branches) {
			if (pair.getFirst().getAsBoolean()) {
				return pair.getSecond();
			}
		}

		return elseBranch;
	}

	/**
	 * Adds an if branch. The last element of this chain has to be {@link join}.
	 *
	 * @param condition
	 *            Condition to be met to execute the branch.
	 * @param element
	 *            The element to be processed as branch.
	 */
	public void addBranch(BooleanSupplier condition, IExperimentElement element) {
		branches.add(new Pair<>(condition, element));
	}

	/**
	 * Sets {@link #elseBranch}. The last element of this chain has to be {@link join}.
	 *
	 * @param elseBranch
	 *            New value for {@link #elseBranch}
	 */
	public void setElseBranch(IExperimentElement elseBranch) {
		this.elseBranch = elseBranch;
	}

	/**
	 * Gets {@link #elseBranch}.
	 *
	 * @return {@link #elseBranch}
	 */
	public IExperimentElement getElseBranch() {
		return this.elseBranch;
	}

	/**
	 * Gets {@link #branches}.
	 *
	 * @return {@link #branches}
	 */
	public List<Pair<BooleanSupplier, IExperimentElement>> getBranches() {
		return this.branches;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNextOrFail(IExperimentElement next) throws UnsupportedOperationException {
		join.setNext(next);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double count() {
		double sum = 0;
		double num = branches.size();

		for (Pair<BooleanSupplier, IExperimentElement> branch : branches) {
			sum += branch.getSecond().count();
		}

		if (elseBranch != null) {
			sum += elseBranch.count();
			num++;
		}

		return (sum / num) + (join.getNext() == null ? 0 : join.getNext().count());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return toString("");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(String prefix) {
		char counter = 'a';
		StringBuilder builder = new StringBuilder();

		for (Pair<BooleanSupplier, IExperimentElement> branch : branches) {
			builder.append(prefix);
			builder.append("IF ");
			builder.append(counter++);
			builder.append(":\n");
			builder.append(branch.getSecond().toString(prefix + SHIFTING));
		}

		if (elseBranch != null) {
			builder.append(prefix);
			builder.append("ELSE:\n");
			builder.append(elseBranch.toString(prefix + SHIFTING));
		}

		builder.append(prefix);
		builder.append("END-IF");

		if (join.getNext() != null) {
			builder.append("\n");
			builder.append(join.getNext().toString(prefix));
		}

		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExperimentElement handleAborted(AbortInnerException exception) {
		return null;
	}

}
