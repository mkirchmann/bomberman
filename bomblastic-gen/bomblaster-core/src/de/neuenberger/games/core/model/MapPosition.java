package de.neuenberger.games.core.model;

import java.util.HashMap;
import java.util.Map;

public class MapPosition {
	private int x, y;

	static Map<Integer, Map<Integer, MapPosition>> map = new HashMap<Integer, Map<Integer, MapPosition>>();

	public MapPosition(int x2, int y2) {
		this.x = x2;
		this.y = y2;
	}

	public static MapPosition valueOf(int x, int y) {
		Map<Integer, MapPosition> map2 = map.get(x);
		if (map2 == null) {
			map2 = new HashMap<Integer, MapPosition>();
			map.put(x, map2);
		}
		MapPosition position = map2.get(y);
		if (position == null) {
			position = new MapPosition(x, y);
			map2.put(y, position);
		}

		return position;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "MapPosition [x=" + x + ", y=" + y + "]";
	}
	
	public double distance(MapPosition pos) {
		double distance;
		if (pos.x == x) {
			distance = Math.abs(pos.y-y);
		} else if (pos.y == y) {
			distance = Math.abs(pos.x-x);
		} else {
			distance = Math.sqrt(distanceSquared(pos));
		}
		return distance;
	}

	public double distanceSquared(MapPosition pos) {
		return Math.pow(pos.getX()-x,2)+Math.pow(pos.getY()-y,2);
	}
}
