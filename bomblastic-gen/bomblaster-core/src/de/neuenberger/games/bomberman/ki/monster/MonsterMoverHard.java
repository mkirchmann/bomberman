package de.neuenberger.games.bomberman.ki.monster;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.neuenberger.games.bomberman.model.BombermanModel;
import de.neuenberger.games.bomberman.model.Monster;
import de.neuenberger.games.core.model.MapPosition;
import de.neuenberger.games.core.model.NCell;

public class MonsterMoverHard extends MonsterMoverMedium {
	Set<MapPosition> lockPositions = new HashSet<MapPosition>();
	public MonsterMoverHard(BombermanModel model) {
		super(model);
	}
	
	@Override
	protected MapPosition selectTargetPositionFromList(List<NCell> list,
			Monster monster) {
		if (list.size()==1) {
			lockPositions.add(monster.getPosition());
		}
		
		List<NCell> arrayList = new ArrayList<NCell>(list);
		for (NCell nCell : list) {
			if (lockPositions.contains(nCell.getPosition())) {
				arrayList.remove(nCell);
			}
		}
		if (arrayList.isEmpty()) {
			lockPositions.clear();
			arrayList = list;
		} else if (arrayList.size()==1) {
			lockPositions.add(monster.getPosition());
		}
		return super.selectTargetPositionFromList(arrayList, monster);
	}

}
