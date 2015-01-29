package de.neuenberger.games.core.ki;

import junit.framework.Assert;

import org.junit.Test;

import de.neuenberger.games.core.model.DefaultMapContext;
import de.neuenberger.games.core.model.MapPosition;
import de.neuenberger.games.core.model.SimpleMap;

public class TestPathFinder {
	@Test
	public void testShortPathConnection() {
		SimpleMap map = new SimpleMap(3, 3);
		Path path = PathFinder.findPath(map, MapPosition.valueOf(0, 0), MapPosition.valueOf(2, 2), DefaultMapContext.getInstance());
		Assert.assertNotNull(path);
		System.out.println("Path found "+path);
	}
	
	@Test
	public void testNotConnectable() {
		SimpleMap map = new SimpleMap(4, 4);
		map.getCell(MapPosition.valueOf(2, 3)).setContent(new Obstacle());
		map.getCell(MapPosition.valueOf(3, 2)).setContent(new Obstacle());
		
		Path path = PathFinder.findPath(map, MapPosition.valueOf(0, 0), MapPosition.valueOf(3, 3), DefaultMapContext.getInstance());
		Assert.assertNull(path);
		System.out.println("Path found "+path);
	}
	
	@Test
	public void testMaze() {
		SimpleMap map = new SimpleMap(4, 4);
		map.getCell(MapPosition.valueOf(2, 3)).setContent(new Obstacle());
		map.getCell(MapPosition.valueOf(3, 1)).setContent(new Obstacle());
		map.getCell(MapPosition.valueOf(2, 1)).setContent(new Obstacle());
		map.getCell(MapPosition.valueOf(1, 1)).setContent(new Obstacle());
		System.out.println(map);
		Path path = PathFinder.findPath(map, MapPosition.valueOf(3, 0), MapPosition.valueOf(3, 3), DefaultMapContext.getInstance());
		Assert.assertNotNull(path);
		System.out.println("Path found "+path);
	}
}
