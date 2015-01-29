package de.neuenberger.games.bomberman.ki.monster;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import de.neuenberger.games.bomberman.model.Monster;
import de.neuenberger.games.core.ki.Path;
import de.neuenberger.games.core.ki.PathFinder;
import de.neuenberger.games.core.model.MapPosition;
import de.neuenberger.games.core.model.SimpleMap;

public class PathFinderRunnable implements Runnable {

	Monster monster;
	SimpleMap map;
	MapPosition targetPosition;

	public PathFinderRunnable(Monster monster, SimpleMap map,
			MapPosition targetPosition) {
		this.monster = monster;
		this.map = map;
		this.targetPosition = targetPosition;
		ReentrantReadWriteLock lock = monster.getLock();
		
		WriteLock writeLock = lock.writeLock();
		try {
			writeLock.lock();
			long locktime = Long.MAX_VALUE;
			monster.setLockPathSeekingUntil(locktime);
		} finally {
			writeLock.unlock();
		}
	}

	
	@Override
	public void run() {
		WriteLock writeLock = null;
		try {
			Path path = PathFinder.findPath(map, monster.getPosition(), targetPosition, 25.0, monster.getMapContext());
			writeLock = monster.getLock().writeLock();
			writeLock.lock();
			monster.setLastPath(path);
			long locktime = (long) (monster.getKiLevel().getPathlocktime()*(1.0+2.5*Math.random()));
			monster.setLockPathSeekingUntil(locktime);
		} finally {
			if (writeLock!=null) {
				writeLock.unlock();
			}
		}
	}

}
