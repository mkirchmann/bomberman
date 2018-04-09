package de.neuenberger.games.core.model;

import de.neuenberger.games.bomberman.model.LiveContent;

public interface NCellContent {

	/**
	 * Check whether the content is dangerous (not killing) for the given live
	 * content.
	 * 
	 * @param liveContent
	 *            given live content
	 * @return true if it is dangerous, false otherwise.
	 */
	boolean isDangerousFor(LiveContent liveContent);

}
