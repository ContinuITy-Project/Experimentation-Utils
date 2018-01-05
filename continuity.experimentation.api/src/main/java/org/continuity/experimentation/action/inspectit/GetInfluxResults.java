package org.continuity.experimentation.action.inspectit;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.continuity.experimentation.IExperimentAction;
import org.continuity.experimentation.data.IDataHolder;
import org.continuity.experimentation.data.SimpleDataHolder;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;

public class GetInfluxResults implements IExperimentAction {
	/**
	 * reference loadtest report path
	 */
	private static final String REFERENCE_LOADTEST_REPORT_PATH = "/referenceLoadtest/";

	/**
	 * generated loadtest report path
	 */

	private static final String GENERATED_LOADTEST_REPORT_PATH = "/generatedLoadtest/";
	private static final String BUSINESS_TRANSACTIONS_MEASUREMENT = "businessTransactions";
	private static final String MEMORY_MEASUREMENT = "memory";
	private static final String CPU_MEASUREMENT = "cpu";
	private static final String INFLUX_USER = "inspectit";
	private static final String INFLUX_PASSWORD = "inspectit";

	private static final String DB_NAME = INFLUX_USER;

	/**
	 * InfluxDB connector.
	 */
	private InfluxDB influxDB;

	/**
	 * StartTime
	 */
	private IDataHolder<Date> startTime;

	/**
	 * StopTime
	 */
	private IDataHolder<Date> stopTime;

	private boolean generatedLoadtest;

	private static int runCount = 0;

	public GetInfluxResults(String host, String port, boolean generatedLoadtest, IDataHolder<Date> startTime, IDataHolder<Date> stopTime) {
		influxDB = InfluxDBFactory.connect("http://" + host + ':' + port, INFLUX_USER, INFLUX_PASSWORD);
		influxDB.setDatabase(DB_NAME);
		this.startTime = startTime;
		this.stopTime = stopTime;
		this.generatedLoadtest = generatedLoadtest;
	}

	@Override
	public void execute() {
		try {
			String path = "";
			if (generatedLoadtest) {
				path = "run#" + runCount + GENERATED_LOADTEST_REPORT_PATH;
				runCount++;
			} else {
				path = "run#" + runCount + REFERENCE_LOADTEST_REPORT_PATH;
			}

			FileUtils.writeStringToFile(new File(path + CPU_MEASUREMENT), getMeasurementResults(CPU_MEASUREMENT), Charset.defaultCharset());
			FileUtils.writeStringToFile(new File(path + MEMORY_MEASUREMENT), getMeasurementResults(MEMORY_MEASUREMENT), Charset.defaultCharset());
			FileUtils.writeStringToFile(new File(path + BUSINESS_TRANSACTIONS_MEASUREMENT), getMeasurementResults(BUSINESS_TRANSACTIONS_MEASUREMENT), Charset.defaultCharset());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		SimpleDataHolder<Date> start = new SimpleDataHolder<Date>("start", Date.class);
		SimpleDataHolder<Date> stop = new SimpleDataHolder<Date>("stop", Date.class);
		Date currentDate = new Date();
		Date pastDate = new Date();
		pastDate.setTime(0);
		start.set(pastDate);
		stop.set(currentDate);
		GetInfluxResults h = new GetInfluxResults("172.16.145.68", "8086", false, start, stop);
		h.execute();
	}

	private String getMeasurementResults(String measurement) {
		String queryString = String.format("SELECT * FROM %s WHERE time >= %d AND time <= %d", measurement, startTime.get().getTime(), stopTime.get().getTime() * 1000000);
		Query query = new Query(queryString, DB_NAME);
		QueryResult queryResult = influxDB.query(query);
		List<Result> result = queryResult.getResults();
		return result.get(0).getSeries().toString();
	}
}
