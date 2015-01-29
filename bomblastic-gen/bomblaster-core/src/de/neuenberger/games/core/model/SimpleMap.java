package de.neuenberger.games.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.neuenberger.games.core.ki.SearchableMap;

public class SimpleMap implements SearchableMap {
	/**
	 * 
	 */
	private static final int	DEFAULT_HEIGHT	= 100;
	/**
	 * 
	 */
	private static final int	DEFAULT_WIDTH	= 100;

	Map<MapPosition, NCell>	map	= new HashMap<MapPosition, NCell>();
	private List<NCell> cells;
	private int height;
	private int width;

	public SimpleMap() {
		this(DEFAULT_WIDTH,DEFAULT_HEIGHT);
	}
	
	public SimpleMap(int maxx, int maxy) {
		for (int x = 0; x < maxx; x++) {
			for (int y = 0; y < maxy; y++) {
				MapPosition position = MapPosition.valueOf(x, y);
				map.put(position, new NCell(position, this));
			}
		}
		
		this.width=maxx;
		this.height = maxy;
	}
	
	public void accept(Visitor<NCell> cellVisitor) {
		Set<Entry<MapPosition,NCell>> set = map.entrySet();
		for (Entry<MapPosition, NCell> entry : set) {
			cellVisitor.accept(entry.getValue());
		}
	}
	
	public List<NCell> getArea(final MapPosition position, final int radius) {
        
        final int squareRadius = radius*radius;
        final List<NCell> result = new LinkedList<NCell>();
        Visitor<NCell> visitor = new Visitor<NCell>() {
			@Override
			public void accept(NCell g) {
				int dx=g.getPosition().getX()-position.getX();
				int dy=g.getPosition().getY()-position.getY();
				if (dx*dx+dy*dy<squareRadius) {
					result.add(g);
				}
			}
        };
        this.accept(visitor);
        

        return result;
    }

	public Map<MapPosition, NCell> getMap() {
		return Collections.unmodifiableMap(map);
	}


	public NCell getCell(MapPosition mapPosition) {
		return map.get(mapPosition);
	}

	public List<NCell> getCells() {
		if (cells==null) {
			cells = new ArrayList<NCell>(map.values());
		}
		return cells;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	public static <P extends Positioned> P getClosest(List<? extends P> list, MapPosition position) {
		Double minValue = (double) Long.MAX_VALUE;
		P result = null;
		if (!list.isEmpty()) {
			result = list.get(0);
			if (list.size()>1) {
				minValue = result.getPosition().distanceSquared(position);
			}
			for (int i=1; i<list.size(); i++) {
				P cPlayer = list.get(i);
				double cDistance = cPlayer.getPosition().distanceSquared(position);
				if (cDistance<minValue) {
					minValue = cDistance;
					result = cPlayer;
				}
				if (minValue<0.1) {
					break;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param position
	 *            Position, for which a possible follow-up position is sought
	 * @return Returns a neighbored field to the given position which has no
	 *         obstacles.
	 */
	public List<NCell> getAllNonBlockedNextPositions(MapPosition position, MapContext context) {
		List<NCell> list = getArea(position, 2);
		for (int i = list.size() - 1; i >= 0; i--) {
			NCell cell = list.get(i);
			if (cell.getPosition() == position) {
				list.remove(i);
			} else if (context.isBlockingContent(cell.getContent())) {
				list.remove(i);
			} else if (position.distanceSquared(cell.getPosition()) > 1.0) {
				list.remove(i);
			}
		}
		return list;
	}

	@Override
	public double getMoveCost(MapPosition positionStart, MapPosition position, MapContext context) {
		return positionStart.distance(position);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int y=0; y<height; y++) {
			builder.append("\n");
			for (int x=0; x<width; x++) {
				MapPosition position = MapPosition.valueOf(x, y);
				NCell cell = map.get(position);
				if (cell.getContent()!=null) {
					builder.append("X");
				} else {
					builder.append("-");
				}
			}
		}
		return "SimpleMap "+" height=" + height + ", width="
				+ width + "\n"+builder+"]";
	}
}
