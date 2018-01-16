package org.continuity.experimentation.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.continuity.experimentation.Context;
import org.continuity.experimentation.IExperimentAction;
import org.continuity.experimentation.data.IDataHolder;
import org.continuity.experimentation.exception.AbortInnerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Selects a random element from an input list and stores it to an output data holder. Takes care of
 * not choosing the same element twice in a row (if there are at least two elements).
 *
 * @author Henning Schulz
 *
 */
public class RandomSelection<T> implements IExperimentAction {

	private static final Logger LOGGER = LoggerFactory.getLogger(RandomSelection.class);

	private final IDataHolder<List<T>> inputHolder;
	private final IDataHolder<T> outputHolder;

	private final boolean avoidTwiceInARow;

	private T last = null;

	private final Random random = new Random();

	/**
	 * @param inputHolder
	 * @param outputHolder
	 * @param avoidTwiceInARow
	 *            If {@code true}, the action won't take the same element twice in a row.
	 */
	public RandomSelection(IDataHolder<List<T>> inputHolder, IDataHolder<T> outputHolder, boolean avoidTwiceInARow) {
		this.inputHolder = inputHolder;
		this.outputHolder = outputHolder;
		this.avoidTwiceInARow = avoidTwiceInARow;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws AbortInnerException
	 */
	@Override
	public void execute(Context context) throws AbortInnerException {
		List<T> selection;

		if (!avoidTwiceInARow) {
			selection = inputHolder.get();
		} else {
			selection = new ArrayList<>(inputHolder.get());

			if ((last != null) && (inputHolder.get().size() > 1)) {
				selection.remove(last);
			}
		}

		int size = selection.size();
		T selected = selection.get(random.nextInt(size));
		outputHolder.set(selected);
		last = selected;

		LOGGER.info("Selected {} from {} elements.", selected, size);
	}

}
