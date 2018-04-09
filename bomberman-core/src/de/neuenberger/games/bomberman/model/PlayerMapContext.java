package de.neuenberger.games.bomberman.model;

import de.neuenberger.games.core.model.DefaultMapContext;
import de.neuenberger.games.core.model.MapContext;
import de.neuenberger.games.core.model.NCellContent;

public class PlayerMapContext extends DefaultMapContext {
	private static final MapContext instance = new PlayerMapContext();
	@Override
	public boolean isBlockingContent(NCellContent content) {
		boolean result = super.isBlockingContent(content);
		
		if (result && content instanceof Exit) {
			result = !((Exit)content).isOpen();
		}
		
		return result;
	}
	
	public static MapContext getInstance() {
		return instance;
	}
}
