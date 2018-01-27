package org.continuity.experimentation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import org.continuity.experimentation.builder.ExperimentBuilderImpl;
import org.continuity.experimentation.builder.StableExperimentBuilder;
import org.continuity.experimentation.exception.AbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;

/**
 * An Experiment holds a name and the first {@link IExperimentElement} to be executed. Each element
 * can, but need not, hold an {@link IExperimentAction}.
 *
 * @author Henning Schulz
 *
 */
public class Experiment extends AbstractExperimentExecutor implements Iterable<IExperimentElement> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Experiment.class);

	private static final NumberFormat DECIMAL_FORMAT = NumberFormat.getInstance(Locale.ENGLISH);

	static {
		if (DECIMAL_FORMAT instanceof DecimalFormat) {
			((DecimalFormat) DECIMAL_FORMAT).applyPattern("#.##");
		}
	}

	private final String name;

	public Experiment(IExperimentElement first) {
		this("experiment", first);
	}

	public Experiment(String name, IExperimentElement first) {
		super(first);
		this.name = name;
	}

	/**
	 * Gets {@link #name}.
	 *
	 * @return {@link #name}
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Starts building a new experiment.
	 *
	 * @param name
	 *            The name of the experiment to be built.
	 * @return A builder for building the experiment.
	 */
	public static StableExperimentBuilder newExperiment(String name) {
		return new ExperimentBuilderImpl(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context) throws AbortException {
		context.append(name);

		setUniqueRootSuffix(context);
		configureLogFile(context);
		saveSummary(context);

		super.execute(context);

		context.remove(name);
	}

	/**
	 * Gets the number of experiment actions to be executed.
	 *
	 * @return The number of actions.
	 */
	public double getNumberOfActions() {
		return getFirst().count();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("Experiment \"");
		builder.append(name);
		builder.append("\" (~");
		builder.append(DECIMAL_FORMAT.format(getNumberOfActions()));
		builder.append(" steps):\n");
		builder.append(getFirst().toString());
		builder.append("END-EXPERIMENT");

		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<IExperimentElement> iterator() {
		return new ExperimentIterator(this);
	}

	private void setUniqueRootSuffix(Context context) {
		Path root = context.toPath().getName(0);
		Path newRoot = root;
		int i = 2;
		String suffix = "";

		while (newRoot.toFile().exists()) {
			suffix = "#" + i++;
			newRoot = Paths.get(root + suffix);
		}

		newRoot.toFile().mkdirs();
		context.setRootSuffix(suffix);
	}

	private void configureLogFile(Context context) {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
		fileAppender.setContext(loggerContext);
		fileAppender.setName("FILE");
		fileAppender.setFile(context.toPath().getName(0).resolve("experiment.log").toString());

		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(loggerContext);
		encoder.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{60} - %msg%n");
		encoder.start();

		fileAppender.setEncoder(encoder);
		fileAppender.start();

		loggerContext.getLogger("ROOT").addAppender(fileAppender);
	}

	private void saveSummary(Context context) {
		Path path = context.toPath().getName(0).resolve("experiment.summary");
		try {
			Files.write(path, Collections.singleton(toString()), StandardOpenOption.CREATE);
		} catch (IOException e) {
			LOGGER.error("Could not save experiment summary!", e);
		}
	}

}
