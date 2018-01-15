package continuity.experimentation;

import java.nio.file.Path;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.tuple.Pair;
import org.continuity.experimentation.Context;
import org.continuity.experimentation.IExperimentElement;

/**
 * @author Henning Schulz
 *
 */
public class MockedContext extends Context {

	private final Path pathMock;

	private final List<Path> createdPaths = new CopyOnWriteArrayList<>();

	public MockedContext(Path pathMock) {
		this.pathMock = pathMock;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Path toPath() {
		createdPaths.add(super.toPath());

		return pathMock;
	}

	/**
	 * Gets {@link #createdPaths}.
	 *
	 * @return {@link #createdPaths}
	 */
	public List<Path> getCreatedPaths() {
		return this.createdPaths;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Context clone() {
		return new Clone((Stack<Pair<IExperimentElement, String>>) getContextStack().clone());
	}

	public class Clone extends Context {

		public Clone(Stack<Pair<IExperimentElement, String>> contextStack) {
			super(contextStack);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Path toPath() {
			createdPaths.add(super.toPath());
			return pathMock;
		}

	}

}
