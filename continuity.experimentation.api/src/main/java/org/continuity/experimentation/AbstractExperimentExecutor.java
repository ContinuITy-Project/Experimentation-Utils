package org.continuity.experimentation;

import org.continuity.experimentation.exception.AbortException;
import org.continuity.experimentation.exception.AbortInnerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Subsumes common means for executing {@link IExperimentElement} chains.
 *
 * @author Henning Schulz
 *
 */
public abstract class AbstractExperimentExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExperimentExecutor.class);

	private final IExperimentElement first;

	protected AbstractExperimentExecutor(IExperimentElement first) {
		this.first = first;
	}

	/**
	 * Gets {@link #first}.
	 *
	 * @return {@link #first}
	 */
	public IExperimentElement getFirst() {
		return this.first;
	}

	/**
	 * Executes the experiment.
	 *
	 * @throws AbortException
	 *
	 */
	public void execute() throws AbortException {
		execute(new Context());
	}

	/**
	 * Executes the experiment in an initial context.
	 *
	 * @param context
	 *            The initial context.
	 * @throws AbortException
	 */
	public void execute(Context context) throws AbortException {
		IExperimentElement current = first;

		while ((current != null) && !current.isEnd()) {
			current.updateContext(context);

			context.toPath().toFile().mkdirs();

			if (current.hasAction()) {
				try {
					current.getAction().execute(context);
				} catch (AbortInnerException e) {
					current = handleAbortInnerException(e, context);
					continue;
				} catch (AbortException e) {
					throw e;
				} catch (Exception e) {
					LOGGER.warn("Action '{}' threw a {}. Ignoring and continuing.", current.getAction(), e.getClass().getSimpleName());
					e.printStackTrace();
				}
			}

			current = current.getNext();
		}
	}

	private IExperimentElement handleAbortInnerException(AbortInnerException e, Context context) throws AbortException {
		IExperimentElement element = context.resetToUpmostElement();

		if (element != null) {
			IExperimentElement next = element.handleAborted(e);

			if (next == null) {
				handleAbortInnerException(e, context);
			} else {
				return next;
			}
		} else {
			throw new AbortException(context, e);
		}

		return null;
	}

}
