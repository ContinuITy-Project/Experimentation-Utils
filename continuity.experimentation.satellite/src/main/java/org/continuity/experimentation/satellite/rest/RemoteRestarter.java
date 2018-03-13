package org.continuity.experimentation.satellite.rest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.continuity.experimentation.satellite.KnownApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Tobias Angerstein, Henning Schulz
 *
 */
@RestController
@RequestMapping(value = "restart")
public class RemoteRestarter {

	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteRestarter.class);

	@RequestMapping(value = "/{key}", method = GET)
	@ResponseBody
	public ResponseEntity<String> restart(@PathVariable String key) {
		KnownApplication app = KnownApplication.forKey(key);

		if (app == null) {
			LOGGER.error("Could not restart the application {}, because the key is not known!", key);

			return ResponseEntity.badRequest().body("Unknown key: " + key);
		}

		LOGGER.info("Restarting {}...", app);

		Process p;

		try {
			String[] command = { "bash", "-c", app.getRestartCommand() };
			LOGGER.info("Executing the command {}", Arrays.toString(command));

			p = Runtime.getRuntime().exec(command);

			p.waitFor();
			String result = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				result += (line + "\n");
			}

			LOGGER.info("{} restarted. Output: {}", app, result);
			return new ResponseEntity<String>(result, HttpStatus.OK);
		} catch (IOException | InterruptedException e) {
			LOGGER.error("Error during restart!", e);
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Header information for swagger requests.
	 *
	 * @param response
	 *            Response information
	 */
	@ModelAttribute
	public void setVaryResponseHeader(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

	}
}
