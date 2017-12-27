package org.continuity.experimentation.action;

/**
 * Restarts the DVDstore.
 * 
 * @author Tobias Angerstein
 *
 */
public class RestartDVDStore extends AbstractRestAction {

	public RestartDVDStore() {
		super("letslx036", "8765");
	}

	@Override
	public void execute() {
		get("/restart/dvdstore", String.class);
	}

}
