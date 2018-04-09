package de.neuenberger.games.bomberman.model;

import de.neuenberger.games.core.model.MapPosition;
import de.neuenberger.games.core.model.Positioned;

public class DynamicCellContent implements IDynamicCellContent, Positioned {
	MapPosition position;
	
	public enum Orientation {
		POS_Y(-90), POS_X(0), MIN_X(180), MIN_Y(90);
		
		float rotation;
		
		Orientation(float rotation) {
			this.rotation = rotation;
		}

		public float getRotation() {
			return rotation;
		}
	}
	
	Orientation orientation = Orientation.POS_X;
	private BombermanModel model;
	
	public DynamicCellContent(BombermanModel model) {
		this.model = model;
	}
	
	/* (non-Javadoc)
	 * @see de.neuenberger.games.bomberman.model.IDynamicCellContent#getPosition()
	 */
	@Override
	public MapPosition getPosition() {
		return position;
	}
	/* (non-Javadoc)
	 * @see de.neuenberger.games.bomberman.model.IDynamicCellContent#setPosition(de.neuenberger.games.core.model.MapPosition)
	 */
	@Override
	public void setPosition(MapPosition position) {
		this.position = position;
	}
	/* (non-Javadoc)
	 * @see de.neuenberger.games.bomberman.model.IDynamicCellContent#getOrientation()
	 */
	@Override
	public Orientation getOrientation() {
		return orientation;
	}
	/* (non-Javadoc)
	 * @see de.neuenberger.games.bomberman.model.IDynamicCellContent#setOrientation(de.neuenberger.games.bomberman.model.DynamicCellContent.Orientation)
	 */
	@Override
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}
	/* (non-Javadoc)
	 * @see de.neuenberger.games.bomberman.model.IDynamicCellContent#hitBy(de.neuenberger.games.bomberman.model.IDynamicCellContent)
	 */
	@Override
	public void hitBy(IDynamicCellContent bomb) {
		model.getDynamicContent().remove(this);
	}

	/* (non-Javadoc)
	 * @see de.neuenberger.games.bomberman.model.IDynamicCellContent#getModel()
	 */
	@Override
	public BombermanModel getModel() {
		return model;
	}
}
