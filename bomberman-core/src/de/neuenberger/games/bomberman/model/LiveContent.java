package de.neuenberger.games.bomberman.model;

import de.neuenberger.games.core.ki.Path;
import de.neuenberger.games.core.model.MapPosition;

public class LiveContent extends DynamicCellContent implements ILivecontent{
	float offsetX;
	float offsetY;
	Long diedAt;
	private Path lastPath;
	MapPosition targetPosition;
	float speed;
	private Long lockPathSeekingUntil;
	
	public LiveContent(BombermanModel model) {
		super(model);
	}

	/* (non-Javadoc)
	 * @see de.neuenberger.games.bomberman.model.IDynamicCellContent#getOffsetX()
	 */
	@Override
	public float getOffsetX() {
		return offsetX;
	}
	/* (non-Javadoc)
	 * @see de.neuenberger.games.bomberman.model.IDynamicCellContent#setOffsetX(float)
	 */
	@Override
	public void setOffsetX(float offsetX) {
		this.offsetX = offsetX;
	}
	/* (non-Javadoc)
	 * @see de.neuenberger.games.bomberman.model.IDynamicCellContent#getOffsetY()
	 */
	@Override
	public float getOffsetY() {
		return offsetY;
	}
	/* (non-Javadoc)
	 * @see de.neuenberger.games.bomberman.model.IDynamicCellContent#setOffsetY(float)
	 */
	@Override
	public void setOffsetY(float offsetY) {
		this.offsetY = offsetY;
	}

	public boolean isDying() {
		return diedAt!=null;
	}

	public Long getDiedAt() {
		return diedAt;
	}

	public void setDiedAt(Long diedAt) {
		this.diedAt = diedAt;
	}
	
	public Path getLastPath() {
		return lastPath;
	}

	public void setLastPath(Path lastPath) {
		this.lastPath = lastPath;
	}

	public MapPosition getTargetPosition() {
		return targetPosition;
	}

	public void setTargetPosition(MapPosition targetPosition) {
		this.targetPosition = targetPosition;
	}
	
	public Long getLockPathSeekingUntil() {
		return lockPathSeekingUntil;
	}

	public void setLockPathSeekingUntil(Long lockPathSeekingUntil) {
		this.lockPathSeekingUntil = lockPathSeekingUntil;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

}
