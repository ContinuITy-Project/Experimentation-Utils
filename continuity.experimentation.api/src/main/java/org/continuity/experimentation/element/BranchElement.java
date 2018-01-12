package org.continuity.experimentation.element;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import org.apache.commons.math3.util.Pair;
import org.continuity.experimentation.Context;
import org.continuity.experimentation.IExperimentElement;

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
		throw new UnsupportedOperationException("Cannot add next to BranchElement! Use addBranch() instead.");
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

		return sum / num;
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
	public String toString(String newLinePrefix) {
		char counter = 'a';
		StringBuilder builder = new StringBuilder();

		builder.append("BRANCH:");

		for (Pair<BooleanSupplier, IExperimentElement> branch : branches) {
			builder.append("\n");
			builder.append(newLinePrefix);
			builder.append("    ");
			builder.append(counter++);
			builder.append(") ");
			builder.append(branch.getSecond().toString(newLinePrefix + "       "));
		}

		builder.append("\n");
		builder.append(newLinePrefix);
		builder.append("    x) ");
		builder.append(elseBranch.toString(newLinePrefix + "       "));

		if (join.getNext() != null) {
			builder.append("\n");
			String nextLinePrefix = newLinePrefix;

			if (nextLinePrefix.length() >= 4) {
				// Omit the '--> '
				nextLinePrefix = nextLinePrefix.substring(0, nextLinePrefix.length() - 4);
			}

			builder.append(nextLinePrefix);
			builder.append("--> ");
			builder.append(join.getNext().toString(nextLinePrefix + "    "));
		}

		return builder.toString();
	}

}
