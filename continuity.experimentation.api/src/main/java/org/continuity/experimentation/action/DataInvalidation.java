package org.continuity.experimentation.action;

import java.util.Arrays;
import java.util.List;

import org.continuity.experimentation.Context;
import org.continuity.experimentation.IExperimentAction;
import org.continuity.experimentation.data.IDataHolder;

/**
 * Invalidates all specified {@link IDataHolder}s.
 *
 * @author Henning Schulz
 *
 */
public class DataInvalidation implements IExperimentAction {

	private final List<IDataHolder<?>> dataHolders;

	public DataInvalidation(List<IDataHolder<?>> dataHolders) {
		this.dataHolders = dataHolders;
	}

	public DataInvalidation(IDataHolder<?>... dataHolders) {
		this(Arrays.asList(dataHolders));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context) {
		dataHolders.forEach(IDataHolder::invalidate);
	}

}
