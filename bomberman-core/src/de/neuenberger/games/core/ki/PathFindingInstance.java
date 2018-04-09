package de.neuenberger.games.core.ki;

import java.util.List;

import de.neuenberger.games.core.model.MapContext;
import de.neuenberger.games.core.model.MapPosition;
import de.neuenberger.games.core.model.NCell;
import de.neuenberger.games.core.model.SimpleMap;

public class PathFindingInstance {
	private SearchableMap map;

	private MapPosition positionTarget;

	private Double initialMaxCost;

	private MapContext context;
	
	PathFindingInstance(SearchableMap map, MapPosition target, MapContext context) {
		this.map = map;
		this.positionTarget = target;
		this.context = context;
	}
	
	
	public Path findPath(MapPosition positionStart) {
		double initialMaxCost = 0;
		if (this.initialMaxCost==null) {
			initialMaxCost = 4.0*positionStart.distance(positionTarget);
		} else {
			initialMaxCost = this.initialMaxCost;
		}
		Path path = findPath(null, positionStart, initialMaxCost);
		
		return path;
	}


	private Path findPath(Path path, MapPosition positionStart,
			double maxCost) {
		Path result = null;
		
		List<NCell> positions = map.getAllNonBlockedNextPositions(positionStart, context);
		
		for (NCell nCell=popClosestPosition(positions, positionTarget); nCell!=null; nCell=popClosestPosition(positions, positionTarget)) {
			double moveCost = map.getMoveCost(positionStart, nCell.getPosition(), context);
			if (path!=null && path.getPositions().contains(nCell.getPosition())) {
				continue;
			}
			Path p;
			if (path!=null) {
				p = new Path(path);
			} else {
				p = new Path();
			}
			p.getPositions().add(nCell.getPosition());
			double cost = p.getCost()+moveCost;
			p.setCost(cost);
			if (nCell.getPosition()==positionTarget) {
				result = p;
				break;
			}
			if (cost<maxCost) {
				Path cPath = findPath(p, nCell.getPosition(), maxCost);
				if (cPath!=null && cPath.getPositions().contains(positionTarget)) {
					if (cPath.getCost()<maxCost) {
						result = cPath;
						maxCost=cPath.getCost();
					}
				}
			}
		}
		
		return result;
	}


	private NCell popClosestPosition(List<NCell> positions, MapPosition positionTarget) {
		NCell cell = SimpleMap.getClosest(positions, positionTarget);
		positions.remove(cell);
		return cell;
	}


	public void setInitialMaxCost(Double maxCost) {
		initialMaxCost = maxCost;
	}


	public Double getInitialMaxCost() {
		return initialMaxCost;
	}
}
