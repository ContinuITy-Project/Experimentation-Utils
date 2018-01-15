package continuity.experimentation.action;

import org.continuity.experimentation.Context;
import org.continuity.experimentation.IExperimentAction;
import org.continuity.experimentation.exception.AbortException;
import org.continuity.experimentation.exception.AbortInnerException;

/**
 * @author Henning Schulz
 *
 */
public class AbortingAction implements IExperimentAction {

	private boolean abort = false;

	private boolean abortInner = false;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context) throws AbortInnerException, AbortException {
		if (abort) {
			throw new AbortException(context, "Intentionally aborting.");
		}

		if (abortInner) {
			throw new AbortInnerException(AbortInnerException.Type.LOOP, context, "Intentionally aborting.");
		}
	}

	/**
	 * Sets {@link #abort}.
	 *
	 * @param abort
	 *            New value for {@link #abort}
	 */
	public void setAbort(boolean abort) {
		this.abort = abort;
	}

	/**
	 * Gets {@link #abort}.
	 *
	 * @return {@link #abort}
	 */
	public boolean isAbort() {
		return this.abort;
	}

	/**
	 * Sets {@link #abortInner}.
	 *
	 * @param abortInner
	 *            New value for {@link #abortInner}
	 */
	public void setAbortInner(boolean abortInner) {
		this.abortInner = abortInner;
	}

	/**
	 * Gets {@link #abortInner}.
	 *
	 * @return {@link #abortInner}
	 */
	public boolean isAbortInner() {
		return this.abortInner;
	}

}
