package org.continuity.experimentation.action;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.continuity.experimentation.Context;
import org.continuity.experimentation.Experiment;
import org.continuity.experimentation.IExperimentAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Henning Schulz
 *
 */
public class ExperimentSummarySaving implements IExperimentAction {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentSummarySaving.class);

	private final Path dir;

	private final Experiment experiment;

	public ExperimentSummarySaving(Path dir, Experiment experiment) {
		this.dir = dir;
		this.experiment = experiment;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HH30");
		String filename = dateFormat.format(new Date()) + "-summary.txt";
		try {
			Files.write(dir.resolve(filename), Arrays.asList(experiment.toString().split("\\n")));
			LOGGER.info("Wrote the experiment summary to {}", dir.resolve(filename));
		} catch (IOException e) {
			LOGGER.error("Could not write the experiment summary to {}", dir.resolve(filename));
			e.printStackTrace();
		}
	}

}
