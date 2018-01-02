package org.continuity.experimentation.data;

import java.util.List;

public class SimpleListHolder<T> extends AbstractDataHolder<List> {

	private List<T> data;

	public SimpleListHolder(String name) {
		super(name, List.class);
	}

	@SuppressWarnings("unchecked")
	public SimpleListHolder(String name, List initialData) {
		super(name, (Class<List>) initialData.getClass());
		this.data = initialData;
		notifyWrite();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<T> getWithoutNotification() {
		return this.data;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setWithoutNotification(List data) {
		this.data = data;
	}

}
