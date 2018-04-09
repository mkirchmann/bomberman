package de.neuenberger.games.bomberman.ki.monster;

import java.util.List;

import de.neuenberger.games.bomberman.model.BombermanModel;
import de.neuenberger.games.bomberman.model.Fire;
import de.neuenberger.games.bomberman.model.Monster;
import de.neuenberger.games.bomberman.model.Player;
import de.neuenberger.games.core.model.MapPosition;
import de.neuenberger.games.core.model.NCell;
import de.neuenberger.games.core.model.SimpleMap;

public class MonsterMoverMedium extends MonsterMoverEasy {

	public MonsterMoverMedium(BombermanModel model) {
		super(model);
	}

	@Override
	protected MapPosition selectTargetPositionFromList(List<NCell> list, Monster monster) {
		Player player = getModel().getClosestPlayer(monster.getPosition());
		NCell cell = SimpleMap.getClosest(list, player.getPosition());
		return cell.getPosition();
	}
	
	@Override
	protected boolean isMoveToTargetSafe(Monster monster) {
		MapPosition targetPosition = monster.getTargetPosition();
		Fire fire = getModel().getDynamicContentByPosition(targetPosition, Fire.class);
		return fire==null;
	}

}
