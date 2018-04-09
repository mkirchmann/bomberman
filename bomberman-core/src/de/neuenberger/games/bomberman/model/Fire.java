package de.neuenberger.games.bomberman.model;

import de.neuenberger.games.core.model.MapPosition;

public class Fire extends DynamicCellContent implements IKilling {
	private long ceaseTime;
	public Fire(BombermanModel model, MapPosition position, long ceaseTime) {
		super(model);
		setPosition(position);
		this.ceaseTime = ceaseTime;
	}
	public long getCeaseTime() {
		return ceaseTime;
	}
	
	@Override
	public void hitBy(IDynamicCellContent dc) {
		if (dc instanceof Wall) {
			dc.hitBy(this);
		} else if (dc instanceof ILivecontent) {
			dc.hitBy(this);
		} else if (dc instanceof Bomb) {
			dc.hitBy(this);
		} else if (dc instanceof Item) {
			dc.hitBy(this);
		}
	}
	public void setCeaseTime(long max) {
		this.ceaseTime = max;
	}

}
