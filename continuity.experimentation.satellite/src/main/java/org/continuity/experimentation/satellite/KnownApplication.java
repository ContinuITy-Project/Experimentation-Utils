package org.continuity.experimentation.satellite;

import java.util.HashMap;
import java.util.Map;

public enum KnownApplication {

	CMR("cmr", "docker service scale monitoring_cmr=0; docker service scale monitoring_cmr=1;"), DVD_STORE("dvdstore", "sudo service dvdstore restart"), HEAT_CLINIC("heat-clinic", "restartHeatClinic.sh"), SOCK_SHOP("sock-shop", "docker stack services -q sock-shop   | while read service; do docker service update --force $service; done;");

	private static final Map<String, KnownApplication> APP_PER_KEY = new HashMap<>();

	static {
		for (KnownApplication app : values()) {
			APP_PER_KEY.put(app.key, app);
		}
	}

	public static KnownApplication forKey(String key) {
		return APP_PER_KEY.get(key);
	}

	private final String key;
	private final String restartCommand;

	private KnownApplication(String key, String restartCommand) {
		this.key = key;
		this.restartCommand = restartCommand;
	}

	public String getRestartCommand() {
		return restartCommand;
	}

}
