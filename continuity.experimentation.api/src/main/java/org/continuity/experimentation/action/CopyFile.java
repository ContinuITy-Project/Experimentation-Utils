package org.continuity.experimentation.action;

import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.continuity.experimentation.Context;
import org.continuity.experimentation.IExperimentAction;
import org.continuity.experimentation.data.IDataHolder;
import org.continuity.experimentation.exception.AbortException;
import org.continuity.experimentation.exception.AbortInnerException;

/**
 * Copies a file from one location to another.
 *
 * @author Henning Schulz
 *
 */
public class CopyFile implements IExperimentAction {

	private final IDataHolder<Path> fromPath;
	private final IDataHolder<Path> toPath;

	/**
	 * @param fromPath
	 * @param toPath
	 */
	public CopyFile(IDataHolder<Path> fromPath, IDataHolder<Path> toPath) {
		this.fromPath = fromPath;
		this.toPath = toPath;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context) throws AbortInnerException, AbortException, Exception {
		FileUtils.copyFile(fromPath.get().toFile(), toPath.get().toFile());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Copy file from \"" + fromPath + "\" to \"" + toPath + "\"";
	}

}
