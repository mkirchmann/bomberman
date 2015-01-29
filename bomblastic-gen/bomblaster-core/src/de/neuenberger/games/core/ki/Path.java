package de.neuenberger.games.core.ki;

import java.util.LinkedList;
import java.util.List;

import de.neuenberger.games.core.model.MapPosition;

public class Path {
	double cost;
	List<MapPosition> positions =new LinkedList<MapPosition>();
	
	Path() {
		
	}
	
	Path(Path path) {
		cost = path.getCost();
		positions.addAll(path.getPositions());
	}
	
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public List<MapPosition> getPositions() {
		return positions;
	}
	public void setPositions(List<MapPosition> positions) {
		this.positions = positions;
	}

	@Override
	public String toString() {
		return "Path [cost=" + cost + ", positions=" + positions + "]";
	}
}
