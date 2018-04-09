package de.neuenberger.games.core.ki;

import java.util.List;

import de.neuenberger.games.core.model.MapContext;
import de.neuenberger.games.core.model.MapPosition;
import de.neuenberger.games.core.model.NCell;

public interface SearchableMap {
	List<NCell> getAllNonBlockedNextPositions(MapPosition position, MapContext context);

	double getMoveCost(MapPosition positionStart, MapPosition position, MapContext context);
	
}
