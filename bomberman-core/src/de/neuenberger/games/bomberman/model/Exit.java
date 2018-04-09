package de.neuenberger.games.bomberman.model;

import de.neuenberger.games.core.model.MapPosition;
import de.neuenberger.games.core.model.NCellContent;

public class Exit extends DynamicCellContent implements NCellContent {

	Long exitOpenTime = null;
	public Exit(BombermanModel model, MapPosition position) {
		super(model);
		this.setPosition(position);
	}

	public boolean isOpen() {
		return exitOpenTime!=null;
	}

	public Long getExitOpenTime() {
		return exitOpenTime;
	}

	public void setExitOpenTime(Long exitOpenTime) {
		this.exitOpenTime = exitOpenTime;
	}

	@Override
	public boolean isDangerousFor(LiveContent liveContent) {
		return false;
	}

}
