package org.continuity.experimentation.data;

import org.continuity.experimentation.exception.AbortInnerException;

/**
 * Abstract class providing management of write and read notifications.
 *
 * @author Henning Schulz
 *
 */
public abstract class AbstractDataHolder<T> implements IDataHolder<T> {

	private final String name;
	private Class<T> dataType;

	private boolean writeNotified = false;

	public AbstractDataHolder(String name, Class<T> dataType) {
		this.name = name;
		this.dataType = dataType;
	}

	/**
	 * To be called when the content of this holder has been set.
	 */
	protected void notifyWrite() {
		writeNotified = true;
	}

	/**
	 * To be called before the content of this holder is to be read.
	 *
	 * @throws IllegalStateException
	 *             If the content is not ready to be read.
	 * @throws AbortInnerException
	 */
	protected void notifyRead() throws AbortInnerException {
		if (!writeNotified) {
			throw new AbortInnerException(null, getClass().getSimpleName() + " '" + name + "' (holds " + dataType.getSimpleName() + ") is notified to be read before data has been written!");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws AbortInnerException
	 */
	@Override
	public T get() throws AbortInnerException {
		notifyRead();
		return getWithoutNotification();
	}

	/**
	 * Simply gets the resource without caring about notifications. Implementation can assume that
	 * notification checking has already been done.
	 *
	 * @return The hold data.
	 */
	protected abstract T getWithoutNotification();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(T data) {
		setWithoutNotification(data);
		notifyWrite();
	}

	/**
	 * Simply sets the data without caring about notifications. Implementation can assume that
	 * notification checking will be done afterwards.
	 *
	 * @param data
	 *            The data to be set.
	 */
	protected abstract void setWithoutNotification(T data);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invalidate() {
		writeNotified = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSet() {
		return writeNotified;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + " \"" + name + "\" holding " + dataType.getSimpleName();
	}

}
