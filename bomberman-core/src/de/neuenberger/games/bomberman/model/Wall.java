package de.neuenberger.games.bomberman.model;

import de.neuenberger.games.core.model.NCell;
import de.neuenberger.games.core.model.NCellContent;

public class Wall extends DynamicCellContent implements NCellContent {
	private NCell cell;
	private Item.Type itemType;
	
	public Wall(BombermanModel model, NCell cell) {
		super(model);
		this.cell = cell;
		setPosition(cell.getPosition());
	}

	private boolean bombable;

	public boolean isBombable() {
		return bombable;
	}

	public void setBombable(boolean bombable) {
		this.bombable = bombable;
	}
	
	@Override
	public void hitBy(IDynamicCellContent bomb) {
		super.hitBy(bomb);
		cell.setContent(null);
		if (itemType!=null) {
			getModel().getDynamicContent().add(new Item(getModel(), itemType, getPosition()));
		}
	}

	public Item.Type getItemType() {
		return itemType;
	}

	public void setItemType(Item.Type itemType) {
		this.itemType = itemType;
	}
	
	@Override
	public boolean isDangerousFor(LiveContent liveContent) {
		return false;
	}
}
