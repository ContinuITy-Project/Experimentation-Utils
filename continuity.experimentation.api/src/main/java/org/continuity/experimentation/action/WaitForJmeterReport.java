package org.continuity.experimentation.action;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

public class WaitForJmeterReport extends AbstractRestAction {

	/**
	 * The maximum number of attempts to retrieve the report.
	 */
	private static final int MAX_ATTEMPTS = 200;

	/**
	 * run count
	 */
	private static int runCount = 0;

	/**
	 * The desired report destination.
	 */
	private String reportDestination;

	/**
	 * Is running loadtest automatically generated.
	 */
	private boolean generatedLoadtest;

	/**
	 * Constructor.
	 * 
	 * @param reportDestination
	 *            the destination, where the report should be stored (including the filename).
	 * @param host
	 *            The host of the continuITy frontend.
	 * @param port
	 *            The port of the continuITy frontend.
	 * @param reportDestination
	 *            the report destination.
	 */
	public WaitForJmeterReport(String host, String port, String reportDestination, boolean generatedLoadtest) {
		super(host, port);
		this.reportDestination = reportDestination;
		this.generatedLoadtest = generatedLoadtest;
	}

	@Override
	public void execute() {
		String report = "";
		for (int i = 0; i < MAX_ATTEMPTS; i++) {
			report = get("/loadtest/report?timeout=20000", String.class);
			if (!report.isEmpty()) {
				break;
			}
		}
		if (!report.isEmpty()) {
			try {
				File file = new File(reportDestination);
				File manipulatedFile = null;
				if(generatedLoadtest) {
					manipulatedFile = new File("run#" + runCount + "/generatedLoadtest/" + file.getName());
				} else {
					manipulatedFile = new File("run#" + runCount + "/referenceLoadtest/" + file.getName());
				}
				FileUtils.writeStringToFile(manipulatedFile, report, Charset.defaultCharset());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
