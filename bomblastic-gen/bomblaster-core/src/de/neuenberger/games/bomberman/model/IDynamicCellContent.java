package de.neuenberger.games.bomberman.model;

import de.neuenberger.games.bomberman.model.DynamicCellContent.Orientation;
import de.neuenberger.games.core.model.MapPosition;

public interface IDynamicCellContent {

	public abstract MapPosition getPosition();

	public abstract void setPosition(MapPosition position);

	public abstract Orientation getOrientation();

	public abstract void setOrientation(Orientation orientation);

	public abstract void hitBy(IDynamicCellContent dc);

	public abstract BombermanModel getModel();

}