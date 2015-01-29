package de.neuenberger.games.bomberman.model;

import de.neuenberger.games.core.model.MapPosition;

public class Item extends DynamicCellContent {
	Type type;
	public enum Type {
		
		FLAME,
		BOMB,
		HEART,
//		FASTER,
//		TIME,
//		MONSTERFREEZE,
//		FREEZE,
//		GHOST,
//		INVINCIBLE
	}
	public Item(BombermanModel model, Type itemType, MapPosition position) {
		super(model);
		setType(itemType);
		setPosition(position);
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
	@Override
	public void hitBy(IDynamicCellContent content) {
		if (content instanceof Player) {
			Player player = (Player) content;
			super.hitBy(content);
			Type type = getType();
			switch (type) {
			case BOMB:
				player.setBombs(player.getBombs()+1);
				break;
			case FLAME:
				player.setFlames(player.getFlames()+1);
				break;
			case HEART:
				player.setLives(player.getLives()+1);
				break;
			}
		} else if (content instanceof Fire) {
			super.hitBy(content);
		}
		
	}
}
