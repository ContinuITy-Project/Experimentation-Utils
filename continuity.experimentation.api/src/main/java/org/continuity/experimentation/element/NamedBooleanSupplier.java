package org.continuity.experimentation.element;

import java.util.function.BooleanSupplier;

/**
 * @author Henning Schulz
 *
 */
public class NamedBooleanSupplier implements BooleanSupplier {

	private final String name;

	private final BooleanSupplier nested;

	/**
	 * @param name
	 * @param nested
	 */
	public NamedBooleanSupplier(String name, BooleanSupplier nested) {
		this.name = name;
		this.nested = nested;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getAsBoolean() {
		return nested.getAsBoolean();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return name;
	}

}
