package org.continuity.experimentation;

import java.util.List;

/**
 * @author Henning Schulz
 *
 */
public class ExperimentReport {

	private final Context context;

	private final List<Exception> caughtExceptions;

	private final List<Exception> uncaughtExceptions;

	/**
	 * @param context
	 * @param caughtExceptions
	 * @param uncaughtExceptions
	 */
	public ExperimentReport(Context context, List<Exception> caughtExceptions, List<Exception> uncaughtExceptions) {
		this.context = context;
		this.caughtExceptions = caughtExceptions;
		this.uncaughtExceptions = uncaughtExceptions;
	}

	/**
	 * Gets {@link #context}.
	 *
	 * @return {@link #context}
	 */
	public Context getContext() {
		return this.context;
	}

	/**
	 * Gets {@link #caughtExceptions}.
	 *
	 * @return {@link #caughtExceptions}
	 */
	public List<Exception> getCaughtExceptions() {
		return this.caughtExceptions;
	}

	/**
	 * Gets {@link #uncaughtExceptions}.
	 *
	 * @return {@link #uncaughtExceptions}
	 */
	public List<Exception> getUncaughtExceptions() {
		return this.uncaughtExceptions;
	}

	public boolean isOk() {
		return caughtExceptions.isEmpty() && uncaughtExceptions.isEmpty();
	}

	public boolean isWarning() {
		return !caughtExceptions.isEmpty() && uncaughtExceptions.isEmpty();
	}

	public boolean isError() {
		return !uncaughtExceptions.isEmpty();
	}

}
