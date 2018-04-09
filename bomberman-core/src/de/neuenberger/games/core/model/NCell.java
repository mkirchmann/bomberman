package de.neuenberger.games.core.model;

import de.neuenberger.games.bomberman.model.LiveContent;

public class NCell implements Positioned {
	private NCellContent content;

	private final MapPosition position;

	private SimpleMap map;

	NCell(MapPosition position, SimpleMap map) {
		this.position = position;
		this.map = map;
	}

	public SimpleMap getMap() {
		return map;
	}

	public MapPosition getPosition() {
		return position;
	}

	public NCellContent getContent() {
		return content;
	}

	public void setContent(NCellContent content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "NCell [content=" + content + ", position=" + position
				+ "]";
	}

	public boolean isDangerousFor(LiveContent monster) {
		boolean result;
		if (content==null) {
			result = false;
		} else {
			result = content.isDangerousFor(monster);
		}
		return result;
	}
	
}
