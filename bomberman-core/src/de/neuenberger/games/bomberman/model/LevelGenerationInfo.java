package de.neuenberger.games.bomberman.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.neuenberger.games.bomberman.model.LevelMapFactory.Generation;

public class LevelGenerationInfo {
	List<String> levelMap;

	String levelName;
	
	Map<Generation, Integer> mapItemsToGenerate = new HashMap<Generation, Integer>();
	
	public List<String> getLevelMap() {
		return levelMap;
	}

	public void setLevelMap(List<String> levelMap) {
		this.levelMap = levelMap;
	}

	public Map<Generation, Integer> getMapItemsToGenerate() {
		return mapItemsToGenerate;
	}

	public void setLevelName(String levelName2) {
		this.levelName = levelName2;
	}

	public String getLevelName() {
		return levelName;
	}
}
