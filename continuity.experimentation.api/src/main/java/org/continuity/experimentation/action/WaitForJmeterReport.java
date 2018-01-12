package org.continuity.experimentation.action;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.continuity.experimentation.Context;

public class WaitForJmeterReport extends AbstractRestAction {

	/**
	 * The maximum number of attempts to retrieve the report.
	 */
	private static final int MAX_ATTEMPTS = 200;

	/**
	 * The desired report destination.
	 */
	private String reportDestination;

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
	public WaitForJmeterReport(String host, String port, String reportDestination) {
		super(host, port);
		this.reportDestination = reportDestination;
	}

	@Override
	public void execute(Context context) {
		String report = "";
		for (int i = 0; i < MAX_ATTEMPTS; i++) {
			report = get("/loadtest/report?timeout=20000", String.class);
			if (!report.isEmpty()) {
				break;
			}
		}
		if (!report.isEmpty()) {
			try {
				FileUtils.writeStringToFile(new File(reportDestination), report, Charset.defaultCharset());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
