package de.neuenberger.games.bomberman.ki.monster;

import de.neuenberger.games.bomberman.model.BombermanModel;
import de.neuenberger.games.bomberman.model.Monster;
import de.neuenberger.games.core.model.MapPosition;
import de.neuenberger.games.core.model.SimpleMap;

public class MonsterMoverHardest extends MonsterMoverHarder {

	public MonsterMoverHardest(BombermanModel model) {
		super(model);
	}
	
	@Override
	protected boolean isMoveToTargetSafe(Monster monster) {
		boolean moveToTargetSafe = super.isMoveToTargetSafe(monster);
		if (moveToTargetSafe) {
			MapPosition position = monster.getPosition();
			boolean doesPositionHaveNearbyBomb = doesPositionHaveNearbyBomb(position, 5, monster);
			if (!doesPositionHaveNearbyBomb) {
				MapPosition targetPosition = monster.getTargetPosition();
				moveToTargetSafe = doesPositionHaveNearbyBomb(targetPosition, 5, monster);
			}
		}
		return moveToTargetSafe;
	}

	/**
	 * Calculates whether at the given position or within the area defined by the given distance
	 * @param position given position
	 * @param distance given distance
	 * @return Returns true if a bomb is found, false otherwise.
	 */
	protected boolean doesPositionHaveNearbyBomb(MapPosition position, int distance, Monster monster) {
		boolean foundBomb = false;
		SimpleMap map = getModel().getMap();
		for (int i=1; i<distance; i++) {
			// search x
			MapPosition searchMapPositionXPlus = new MapPosition(position.getX()+i, position.getY());
			boolean containsDangerForXPlus = map.containsDangerFor(searchMapPositionXPlus, monster);
			
			MapPosition searchMapPositionXMinus = new MapPosition(position.getX()-i, position.getY());
			boolean containsDangerForXMinus = map.containsDangerFor(searchMapPositionXMinus, monster);
			
			// search y
			MapPosition searchMapPositionYPlus = new MapPosition(position.getX(), position.getY()+i);
			boolean containsDangerForYPlus = map.containsDangerFor(searchMapPositionYPlus, monster);
			
			MapPosition searchMapPositionYMinus = new MapPosition(position.getX(), position.getY()-i);
			boolean containsDangerForYMinus = map.containsDangerFor(searchMapPositionYMinus, monster);
			
			if (containsDangerForXPlus || containsDangerForXMinus || containsDangerForYMinus || containsDangerForYPlus) {
				foundBomb = true;
				break;
			}
		}
		
		return foundBomb;
	}
	

}
