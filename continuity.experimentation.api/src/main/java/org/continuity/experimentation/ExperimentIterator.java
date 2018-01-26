package org.continuity.experimentation;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Iterates over all elements of an experiment. The order can be various and is not necessarily
 * correlated with the order of the experiment execution.
 *
 * @author Henning Schulz
 *
 */
public class ExperimentIterator implements Iterator<IExperimentElement> {

	private Collection<IExperimentElement> current;

	private Iterator<IExperimentElement> iterator;

	public ExperimentIterator(Experiment experiment) {
		this.current = Collections.singleton(experiment.getFirst());
		this.iterator = current.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		if (!iterator.hasNext()) {
			refreshCurrent();
		}

		return iterator.hasNext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExperimentElement next() {
		return iterator.next();
	}

	private void refreshCurrent() {
		current = current.stream().map(IExperimentElement::iterateToNext).flatMap(Collection::stream).collect(Collectors.toList());
		iterator = current.iterator();
	}

}
