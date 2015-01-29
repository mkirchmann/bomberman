package de.neuenberger.games.bomberman.model;

import de.neuenberger.games.core.model.NCell;

public class Antenna extends Wall {

	public Antenna(BombermanModel model, NCell cell) {
		super(model, cell);
		this.setBombable(true);
	}

}
