package de.neuenberger.games.core.ki;

import de.neuenberger.games.core.model.MapContext;
import de.neuenberger.games.core.model.MapPosition;

public class PathFinder {
	public static Path findPath(SearchableMap map, MapPosition source, MapPosition target, MapContext context) {
		return findPath(map, source, target, null, context);
	}
	
	public static Path findPath(SearchableMap map, MapPosition source, MapPosition target, Double maxCost, MapContext context) {
		if (source==target) {
			return null;
		}
		PathFindingInstance instance = new PathFindingInstance(map, target, context);
		if (maxCost!=null) {
			instance.setInitialMaxCost(maxCost);
		}
		// System.out.println("Looking for path between "+source+" and "+target+" on map="+map);
		long time = System.currentTimeMillis();
		Path path = instance.findPath(source);
		System.out.println("Found path "+path+" in "+(System.currentTimeMillis()-time)+" ms.");
		return path;
	}
}
