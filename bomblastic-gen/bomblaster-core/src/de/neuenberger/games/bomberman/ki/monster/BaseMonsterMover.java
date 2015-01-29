package de.neuenberger.games.bomberman.ki.monster;

import java.util.List;

import de.neuenberger.games.bomberman.model.Bomb;
import de.neuenberger.games.bomberman.model.BombermanModel;
import de.neuenberger.games.bomberman.model.Monster;
import de.neuenberger.games.core.model.MapPosition;
import de.neuenberger.games.core.model.NCell;

public class BaseMonsterMover extends LiveContentMover<Monster> implements IMonsterMover {
	

	public BaseMonsterMover(BombermanModel model) {
		super(model);
	}

	@Override
	public void retargetonDemand(Monster monster) {
		if (getModel().getDynamicContentByPosition(monster.getTargetPosition(),
				Bomb.class) != null) {
			acquireNextPosition(monster);
		}
	}

	/**
	 * Calculates a next position and calculates an orientation for that
	 * position
	 * 
	 * @param monster
	 *            the given Monster
	 */
	@Override
	public void acquireNextPosition(Monster monster) {
		MapPosition position = monster.getPosition();
		List<NCell> list = getModel().getMap().getAllNonBlockedNextPositions(
				position, monster.getMapContext());
		MapPosition targetPosition = null;
		if (!list.isEmpty()) {
			targetPosition = selectTargetPositionFromList(list, monster);
			acquireOrientation(monster, targetPosition);
		}
	}

	/**
	 * May return null.
	 * 
	 * @param list
	 *            List of all possible positions, i.e. all that are free (no
	 *            walls, bombs, obstacles)
	 * @param monster
	 * @return One position from the given list
	 */
	protected MapPosition selectTargetPositionFromList(List<NCell> list,
			Monster monster) {
		MapPosition targetPosition = null;
		int index = (int) (Math.random() * list.size());
		if (!list.isEmpty()) {
			targetPosition = list.get(index).getPosition();
		}
		return targetPosition;
	}
}
