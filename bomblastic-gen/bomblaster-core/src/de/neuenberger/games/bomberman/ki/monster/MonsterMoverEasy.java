package de.neuenberger.games.bomberman.ki.monster;

import de.neuenberger.games.bomberman.model.BombermanModel;
import de.neuenberger.games.bomberman.model.Fire;
import de.neuenberger.games.bomberman.model.Monster;
import de.neuenberger.games.core.model.MapPosition;

public class MonsterMoverEasy extends BaseMonsterMover {

	public MonsterMoverEasy(BombermanModel model) {
		super(model);
	}
	
	@Override
	protected boolean isMoveToTargetSafe(Monster monster) {
		MapPosition targetPosition = monster.getTargetPosition();
		Fire fire = getModel().getDynamicContentByPosition(targetPosition, Fire.class);
		return fire==null;
	}
	
}
