package continuity.experimentation.action;

import org.continuity.experimentation.Context;
import org.continuity.experimentation.IExperimentAction;
import org.continuity.experimentation.data.IDataHolder;
import org.continuity.experimentation.exception.AbortInnerException;

/**
 * @author Henning Schulz
 *
 */
public class DummyExperimentAction implements IExperimentAction {

	private IDataHolder<String> inputHolder;
	private IDataHolder<String> outputHolder;

	private String message;

	/**
	 *
	 */
	public DummyExperimentAction(IDataHolder<String> inputHolder, IDataHolder<String> outputHolder, String message) {
		this.inputHolder = inputHolder;
		this.outputHolder = outputHolder;
		this.message = message;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws AbortInnerException
	 */
	@Override
	public void execute(Context context) throws AbortInnerException {
		System.out.println("Input: " + (inputHolder.isSet() ? inputHolder.get() : "not set"));
		outputHolder.set(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Write \"" + message + "\" to " + outputHolder;
	}

}
