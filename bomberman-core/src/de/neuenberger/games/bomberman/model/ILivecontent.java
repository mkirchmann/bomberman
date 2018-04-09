package de.neuenberger.games.bomberman.model;

import de.neuenberger.games.core.ki.Path;
import de.neuenberger.games.core.model.MapPosition;

public interface ILivecontent extends IDynamicCellContent {
	public abstract float getOffsetX();

	public abstract void setOffsetX(float offsetX);

	public abstract float getOffsetY();

	public abstract void setOffsetY(float offsetY);

	public boolean isDying();

	public Long getDiedAt();

	public void setDiedAt(Long time);

	MapPosition getTargetPosition();

	public void setTargetPosition(MapPosition pos);
	
	Path getLastPath();
	
	void setLastPath(Path p);
	
	float getSpeed();
}
