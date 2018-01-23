package org.continuity.experimentation.action.inspectit;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.continuity.experimentation.Context;
import org.continuity.experimentation.IExperimentAction;
import org.continuity.experimentation.data.IDataHolder;
import org.continuity.experimentation.exception.AbortInnerException;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Retrieves results from an InfluxDB.
 *
 * @author Henning Schulz
 *
 */
public class GetInfluxResults implements IExperimentAction {

	private static final Logger LOGGER = LoggerFactory.getLogger(GetInfluxResults.class);

	private static final String BUSINESS_TRANSACTIONS_MEASUREMENT = "businessTransactions";
	private static final String MEMORY_MEASUREMENT = "memory";
	private static final String CPU_MEASUREMENT = "cpu";
	private static final String INFLUX_USER = "inspectit";
	private static final String INFLUX_PASSWORD = "inspectit";

	private static final String DB_NAME = INFLUX_USER;

	private static final String FILE_EXT = ".csv";
	private static final String SEPARATOR = ";";

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

	public GetInfluxResults(String host, String port, IDataHolder<Date> startTime, IDataHolder<Date> stopTime) {
		influxDB = InfluxDBFactory.connect("http://" + host + ':' + port, INFLUX_USER, INFLUX_PASSWORD);
		influxDB.setDatabase(DB_NAME);
		this.startTime = startTime;
		this.stopTime = stopTime;
	}

	@Override
	public void execute(Context context) throws IOException, AbortInnerException {
		Path basePath = context.toPath();

		FileUtils.writeStringToFile(basePath.resolve(CPU_MEASUREMENT + FILE_EXT).toFile(), getMeasurementResults(CPU_MEASUREMENT), Charset.defaultCharset());
		FileUtils.writeStringToFile(basePath.resolve(MEMORY_MEASUREMENT + FILE_EXT).toFile(), getMeasurementResults(MEMORY_MEASUREMENT), Charset.defaultCharset());
		FileUtils.writeStringToFile(basePath.resolve(BUSINESS_TRANSACTIONS_MEASUREMENT + FILE_EXT).toFile(), getMeasurementResults(BUSINESS_TRANSACTIONS_MEASUREMENT), Charset.defaultCharset());
	}

	private String getMeasurementResults(String measurement) throws AbortInnerException {
		LOGGER.info("Retrieving measurement {} from InfluxDB between {} and {}.", measurement, startTime.get(), stopTime.get());

		String queryString = String.format("SELECT * FROM %s WHERE time >= %d AND time <= %d", measurement, startTime.get().getTime() * 1000000, stopTime.get().getTime() * 1000000);
		Query query = new Query(queryString, DB_NAME);
		QueryResult queryResult = influxDB.query(query);
		List<Result> result = queryResult.getResults();
		Series series = result.get(0).getSeries().get(0);

		StringBuilder builder = new StringBuilder();

		boolean first = true;
		for (String header : series.getColumns()) {
			if (first) {
				first = false;
			} else {
				builder.append(SEPARATOR);
			}

			builder.append(header);
		}

		builder.append("\n");

		for (List<Object> row : series.getValues()) {
			first = true;

			for (Object value : row) {
				if (first) {
					first = false;
				} else {
					builder.append(SEPARATOR);
				}

				builder.append(value);
			}

			builder.append("\n");
		}

		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Get the results from InfluxDB from time range (\"" + startTime + "\", \"" + stopTime + "\")";
	}

}
