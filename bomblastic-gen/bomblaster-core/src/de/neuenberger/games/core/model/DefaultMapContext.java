package de.neuenberger.games.core.model;

public class DefaultMapContext implements MapContext {
	private static final DefaultMapContext instance = new DefaultMapContext();
	@Override
	public boolean isBlockingContent(NCellContent content) {
		return content!=null;
	}
	public static DefaultMapContext getInstance() {
		return instance;
	}
	 
}
