package org.continuity.experimentation.action;

import org.continuity.experimentation.Context;

/**
 * Restarts the DVDstore.
 *
 * TODO: Generalize it - e.g., send an arbitrary command to the satellite.
 *
 * @author Tobias Angerstein
 *
 */
public class RestartDVDStore extends AbstractRestAction {

	public RestartDVDStore() {
		super("letslx036", "8765");
	}

	@Override
	public void execute(Context context) {
		get("/restart/dvdstore", String.class);
	}

}
