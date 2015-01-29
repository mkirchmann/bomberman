package de.neuenberger.games.bomberman.ki.monster;

import java.util.Collections;
import java.util.List;

import de.neuenberger.games.bomberman.model.BombermanModel;
import de.neuenberger.games.bomberman.model.Monster;
import de.neuenberger.games.bomberman.model.Player;
import de.neuenberger.games.core.ki.Path;
import de.neuenberger.games.core.model.MapPosition;
import de.neuenberger.games.core.model.NCell;
import de.neuenberger.games.core.model.SimpleMap;

public class MonsterMoverHarder extends MonsterMoverHard {

	public MonsterMoverHarder(BombermanModel model) {
		super(model);
	}
	
	@Override
	protected MapPosition selectTargetPositionFromList(List<NCell> list,
			Monster monster) {
		Player player = getModel().getClosestPlayer(monster.getPosition());
		
		// find closest path
		MapPosition result = findBestNextPositionPath(list, monster, player);
		if (result==null) {
			result = super.selectTargetPositionFromList(list, monster);
		}
		return result;
	}

	private MapPosition findBestNextPositionPath(List<NCell> list, Monster monster,
			Player player) {
		Path path = monster.getLastPath();
		MapPosition result;
		if (path==null) {
			boolean seekIsLocked = false;
			if (monster.getLockPathSeekingUntil()!=null) {
				if (System.currentTimeMillis()<monster.getLockPathSeekingUntil()) {
					seekIsLocked = true;
				} else {
					monster.setLockPathSeekingUntil(null);
				}
			}
			
			// check if distance is close, if this is the case find path, otherwise only heuristic
			if (!seekIsLocked && monster.getPosition().distanceSquared(player.getPosition())<25) {
				new Thread(new PathFinderRunnable(monster, getModel().getMap(), player.getPosition())).start();
			}
			result = null;
		} else {
			result = popNextPosition(monster, path);
			if (result!=null) {
				NCell closest = SimpleMap.getClosest(list, result);
				result = closest.getPosition();
			}
		}
		return result;
	}

	private MapPosition popNextPosition(Monster monster, Path path) {
		List<MapPosition> positions;
		if (path!=null) {
			positions = path.getPositions();
		} else {
			positions = Collections.emptyList();
		}
		MapPosition result = null;
		if (!positions.isEmpty()) {
			result = positions.remove(0);
			if (positions.isEmpty()) {
				monster.setLastPath(null);
			} else {
				monster.setLastPath(path);
			}
		}
		return result;
	}
}
