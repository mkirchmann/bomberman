package de.neuenberger.games.bomberman.ki.monster;

import de.neuenberger.games.bomberman.logic.MoveLogic;
import de.neuenberger.games.bomberman.model.BombermanModel;
import de.neuenberger.games.bomberman.model.DynamicCellContent.Orientation;
import de.neuenberger.games.bomberman.model.ILivecontent;
import de.neuenberger.games.core.model.MapPosition;

public class LiveContentMover<L extends ILivecontent> {
	private BombermanModel model;

	private MoveLogic logic;
	
	public LiveContentMover(BombermanModel model) {
		super();
		this.model = model;
		this.logic = new MoveLogic(null, model);
	}
	
	public boolean move(float delta, L lc) {
		if (lc.isDying()) {
			return false;
		}
		if (lc.getTargetPosition() == null) {
			boolean hasOffsetX = Math.abs(lc.getOffsetX())>0.1f;
			boolean hasOffsetY = Math.abs(lc.getOffsetY())>0.1f;
			if (lc.getOrientation()!=null && (hasOffsetX || hasOffsetY)) {
				moveOffset(delta, lc, true);
			} else {
				acquireNextPosition(lc);
			}
		}
		if (lc.getTargetPosition() == null) {
			return false;
		}
		retargetonDemand(lc);
		if (!isMoveToTargetSafe(lc)) {
			return false;
		}
		moveOffset(delta, lc, false);
		return true;

	}
	protected void acquireNextPosition(L lc) {
		if (lc.getLastPath()!=null) {
			if (!lc.getLastPath().getPositions().isEmpty()) {
				MapPosition position = lc.getLastPath().getPositions().remove(0);
				lc.setTargetPosition(position);
				acquireOrientation(lc, position);
			} else {
				lc.setLastPath(null);
			}
		}
	}
	protected void acquireOrientation(L lc, MapPosition targetPosition) {
		if (targetPosition != null) {
			lc.setTargetPosition(targetPosition);
			int dx = targetPosition.getX() - lc.getPosition().getX();
			int dy = targetPosition.getY() - lc.getPosition().getY();
			if (dx > 0) {
				lc.setOrientation(Orientation.POS_X);
			} else if (dx < 0) {
				lc.setOrientation(Orientation.MIN_X);
			} else if (dy > 0) {
				lc.setOrientation(Orientation.POS_Y);
			} else if (dy < 0) {
				lc.setOrientation(Orientation.MIN_Y);
			} else {
				lc.setOrientation(null);
			}
		}
	}

	/**
	 * 
	 * @param monster
	 * @return true if the target is considered safe for the Ki, false
	 *         otherwise.
	 */
	protected boolean isMoveToTargetSafe(L monster) {
		// do nothing in base impl
		return true;
	}

	protected void retargetonDemand(L monster) {
		// do nothing in base impl.
		
	}

	protected void moveOffset(float delta, L lc, boolean zerolize) {
		Orientation orientation = lc.getOrientation();
		if (orientation != null) {
			float offsetX = lc.getOffsetX();
			float offsetY = lc.getOffsetY();
			float add = delta * lc.getSpeed();
			switch (orientation) {
			case MIN_X:
				offsetX = addOffset(offsetX, -add, zerolize);
				break;
			case POS_X:
				offsetX = addOffset(offsetX, add, zerolize);
				break;
			case MIN_Y:
				offsetY = addOffset(offsetY, -add, zerolize);
				break;
			case POS_Y:
				offsetY = addOffset(offsetY, add, zerolize);
				break;
			default:
				break;
			}
			logic.setOffsets(lc, offsetX, offsetY);
			if (lc.getPosition() == lc.getTargetPosition()) {
				lc.setTargetPosition(null);
			}
		}
	}

	private static float addOffset(float offset, float add, boolean zerolize) {
		float retValue;
		if (zerolize) {
			if (offset>0 && add>0 && add>offset) {
				retValue=0;
			} else if (offset<0 && add<0 && add<offset) {
				retValue=0;
			} else {
				retValue = offset+add;
			}
		} else {
			retValue = offset+add;
		}
		return retValue;
	}

	public BombermanModel getModel() {
		return model;
	}
	
	public MoveLogic getLogic() {
		return logic;
	}
}
