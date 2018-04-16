package de.neuenberger.games.bomberman.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.neuenberger.games.bomberman.model.Item.Type;
import de.neuenberger.games.bomberman.model.Monster.KiLevel;
import de.neuenberger.games.core.model.MapPosition;
import de.neuenberger.games.core.model.NCell;
import de.neuenberger.games.core.model.SimpleMap;

/**
 * Meaning of tiles: F: Free P: Player main start position p: Mulitplayer start
 * position X: Concrete Block -: Empty field, fillable with tiles or monster E:
 * Exit point
 * 
 * @author Michael Kirchmann
 * 
 */
public class LevelMapFactory {
	interface Generation<G> {

		String getKeyword();

		Class<G> getClazz();

		G getValue();
	}

	enum ItemGeneration implements Generation<Item.Type> {
		FLAME("item-flame", Item.Type.FLAME), //
		BOMB("item-bomb", Item.Type.BOMB), //
		HEART("item-heart", Item.Type.HEART), //
		POISON("item-poison", Item.Type.POISON); //
		String keyword;
		private Type value;

		ItemGeneration(String keyword, Item.Type itemType) {
			this.keyword = keyword;
			this.value = itemType;
		}

		public String getKeyword() {
			return keyword;
		}

		public Type getValue() {
			return value;
		}

		public Class<Item.Type> getClazz() {
			return Item.Type.class;
		}
	}

	enum WallGeneration implements Generation<Wall> {
		WALL("walls");
		String keyword;

		private WallGeneration(String keyWord) {
			this.keyword = keyWord;
		}

		@Override
		public String getKeyword() {
			return keyword;
		}

		@Override
		public Class getClazz() {
			return Wall.class;
		}

		@Override
		public Wall getValue() {
			return null;
		}

	}

	enum MonsterGeneration implements Generation<KiLevel> {
		MONSTER_NOOB(Monster.KiLevel.Noob, "monster-noob"), MONSTER_EASY(
				Monster.KiLevel.Easy, "monster-easy"), MONSTER_MEDIUM(
				Monster.KiLevel.Medium, "monster-medium"), MONSTER_HARD(
				Monster.KiLevel.Hard, "monster-hard"), MONSTER_HARDER(
				Monster.KiLevel.Harder, "monster-harder"), MONSTER_HARDEST(
				Monster.KiLevel.Hardest, "monster-harder"), ;
		Monster.KiLevel value;
		String keyWord;

		private MonsterGeneration(KiLevel kiLevel, String keyWord) {
			this.value = kiLevel;
			this.keyWord = keyWord;
		}

		@Override
		public String getKeyword() {
			return keyWord;
		}

		@Override
		public Class<KiLevel> getClazz() {
			return KiLevel.class;
		}

		public Monster.KiLevel getValue() {
			return value;
		}

	}

	enum Context {
		MAP, LEVEL;
	}

	static List<Generation> allGenerationValues = new ArrayList<Generation>();
	{
		allGenerationValues.addAll(Arrays.asList(ItemGeneration.values()));
		allGenerationValues.addAll(Arrays.asList(MonsterGeneration.values()));
		allGenerationValues.addAll(Arrays.asList(WallGeneration.values()));
	}

	static Generation getGenerationForKeyWord(String keyWord) {
		Generation result = null;
		for (Generation generation : allGenerationValues) {
			if (generation.getKeyword().equals(keyWord)) {
				result = generation;
				break;
			}
		}
		return result;
	}

	public LevelMapFactory(InputStream stream) {
		try {
			readConfiguration(stream);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void readConfiguration(InputStream stream) throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					stream));
			String line = "";
			Context context = null;
			Map<String, List<String>> mapNameToMap = new HashMap<String, List<String>>();
			List<String> currentLevelStringForContext = null;
			LevelGenerationInfo generationInfo = null;
			levels = new ArrayList<LevelGenerationInfo>();

			while (line != null) {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				String read = line.trim();
				if (read.isEmpty()) {
					continue;
				} else if (read.startsWith("#")) {
					continue;
				} else if (read.startsWith("!")) {
					if (read.startsWith("!define map")) {
						context = Context.MAP;
						String mapName = read.split(":")[1].trim();
						currentLevelStringForContext = new LinkedList<String>();
						mapNameToMap.put(mapName, currentLevelStringForContext);
					} else if (read.startsWith("!define level")) {
						context = Context.LEVEL;
						String levelName = read.split(":")[1].trim();
						generationInfo = new LevelGenerationInfo();
						generationInfo.setLevelName(levelName);
						levels.add(generationInfo);
					}
					continue;
				}
				if (context == Context.MAP) {
					if (!read.isEmpty()) {
						currentLevelStringForContext.add(read);
					}
				} else if (context == Context.LEVEL) {
					String[] split = read.split(":");
					String variable = split[0].trim();
					String value = split[1].trim();
					Generation generation = getGenerationForKeyWord(variable);
					if (variable.equals("map")) {
						List<String> levelMap = mapNameToMap.get(value);
						if (levelMap == null) {
							try {
								reader.close();
								stream.close();
							} catch (Exception ignore) {
								// ignore
							}
							throw new IllegalArgumentException(
									"Level not found: '" + value + "'");
						}
						generationInfo.setLevelMap(levelMap);
					} else if (generation != null) {
						generationInfo.getMapItemsToGenerate().put(generation,
								Integer.parseInt(value));
					}
				}
			}
		} finally {
			// close res.
			try {
				stream.close();
			} catch (Exception ignore) {
				// ignore
			}
		}
	}

	private List<LevelGenerationInfo> levels;

	public void updateGame(BombermanModel model) {
		model.setExit(null);
		LevelGenerationInfo generationInfo = getLevelInfo(model);

		List<String> levelMap = generationInfo.getLevelMap();
		
		List<IDynamicCellContent> dContent = model.getDynamicContent();
		for (int i = dContent.size() - 1; i >= 0; i--) {
			IDynamicCellContent cellContent = dContent.get(i);
			if (!(cellContent instanceof Player)) {
				dContent.remove(i);
			}
		}
		MapPositionInfo mapInfo = createMapPositionInfo(generationInfo, model, true);
		List<MapPosition> playerPositions = mapInfo.playerPositions;
		List<MapPosition> freePositions = mapInfo.freePositions;
		SimpleMap map = mapInfo.map;
		
		model.setMap(map);
		resetPlayerPositions(model, playerPositions);
		List<Wall> walls = new ArrayList<Wall>(freePositions.size());
		Set<Entry<Generation<Wall>, Integer>> wallEntries = getEntriesForClass(
				Wall.class, generationInfo.getMapItemsToGenerate());
		for (Entry<Generation<Wall>, Integer> entry : wallEntries) {
			Generation generation = entry.getKey();
			Integer value = entry.getValue();
			for (int i = 0; i < value; i++) {
				MapPosition position = drawPosition(freePositions);
				NCell cell = map.getCell(position);
				Wall wall = new Wall(model, cell);
				wall.setBombable(true);
				cell.setContent(wall);
				model.getDynamicContent().add(wall);
				walls.add(wall);
			}
		}
		Set<Entry<Generation<Monster.KiLevel>, Integer>> monsterEntries = getEntriesForClass(
				KiLevel.class, generationInfo.getMapItemsToGenerate());
		for (Entry<Generation<Monster.KiLevel>, Integer> entry : monsterEntries) {
			Generation<Monster.KiLevel> generation = entry.getKey();
			Integer value = entry.getValue();
			for (int i = 0; i < value; i++) {
				MapPosition position = drawPosition(freePositions);
				map.getCell(position);
				Monster monster = new Monster(model, position, generation.getValue());
				monster.setPosition(position);
				model.getDynamicContent().add(monster);
			}
		}

		Set<Entry<Generation<Item.Type>, Integer>> itemEntries = getEntriesForClass(
				Item.Type.class, generationInfo.getMapItemsToGenerate());
		for (Entry<Generation<Item.Type>, Integer> entry : itemEntries) {
			Generation<Item.Type> generation = entry.getKey();
			Integer value = entry.getValue();
			for (int i = 0; i < value; i++) {
				Wall wall = drawWall(walls);
				wall.setItemType(generation.getValue());
			}
		}
	}

	private void resetPlayerPositions(BombermanModel model,
			List<MapPosition> playerPositions) {
		List<Player> players = model.getDynamicContent(Player.class);
		for (int idx = 0; idx < players.size(); idx++) {
			players.get(idx).setPosition(drawPosition(playerPositions));
			players.get(idx).setOffsetX(0);
			players.get(idx).setOffsetY(0);
		}
	}

	<G> Set<Entry<Generation<G>, Integer>>  getEntriesForClass(Class<G> clazz,
			Map<Generation, Integer> mapItemsToGenerate) {
		Set<Entry<Generation, Integer>> set = mapItemsToGenerate.entrySet();
		Set<Entry<Generation<G>, Integer>> resultSet = new HashSet<Entry<Generation<G>, Integer>>();
		for (Entry entry : set) {
			if (((Entry<Generation, ?>)entry).getKey().getClazz().equals(clazz)) {
				resultSet.add(entry);
			}
		}
		return resultSet;
	}

	private Wall drawWall(List<Wall> freePositions) {
		int rndPos = (int) (Math.random() * (double) freePositions.size());
		Wall wall = freePositions.get(rndPos);
		freePositions.remove(rndPos);
		return wall;
	}

	private MapPosition drawPosition(List<MapPosition> freePositions) {
		int rndPos = (int) (Math.random() * (double) freePositions.size());
		MapPosition position = freePositions.get(rndPos);
		freePositions.remove(rndPos);
		return position;
	}

	public void resetDynamics(BombermanModel model) {
		LevelGenerationInfo generationInfo = getLevelInfo(model);
		MapPositionInfo mapPositionInfo = createMapPositionInfo(generationInfo, model, false);
		List<Wall> dynamicContent = model.getDynamicContent(Wall.class);
		for (Wall wall : dynamicContent) {
			mapPositionInfo.freePositions.remove(wall.getPosition());
		}
		resetPlayerPositions(model, mapPositionInfo.playerPositions);
		
		List<Monster> monsters = model.getDynamicContent(Monster.class);
		for (Monster monster : monsters) {
			monster.setPosition(drawPosition(mapPositionInfo.freePositions));
			monster.setOrientation(null);
			monster.setTargetPosition(null);
			monster.setOffsetX(0);
			monster.setOffsetY(0);
		}
		
		List<Bomb> bombs = model.getDynamicContent(Bomb.class);
		for (Bomb bomb : bombs) {
			NCell cell = bomb.getCell();
			if (cell!=null) {
				cell.setContent(null);
			}
			model.getDynamicContent().remove(bomb);
		}
		
		List<Fire> fires = model.getDynamicContent(Fire.class);
		for (Fire fire : fires) {
			model.getDynamicContent().remove(fire);
		}
		
	}
	
	private LevelGenerationInfo getLevelInfo(BombermanModel model) {
		return levels.get(model.getLevel()%levels.size());
	}

	MapPositionInfo createMapPositionInfo(LevelGenerationInfo generationInfo, BombermanModel model, boolean create) {
		List<String> levelMap = generationInfo.getLevelMap();
		int idxY = 0;
		int height = levelMap.size();
		MapPositionInfo result = new MapPositionInfo();
		
		for (String string : levelMap) {
			char[] array = string.toCharArray();
			int width = array.length;
			if (result.map == null) {
				result.map = new SimpleMap(width, height);
			}
			int idx = 0;
			for (char c : array) {
				MapPosition position = MapPosition.valueOf(idx, idxY);
				NCell cell = result.map.getCell(position);
				switch (c) {
				case 'X':
					Wall content = new Wall(model, cell);
					content.setBombable(false);
					cell.setContent(content);
					break;
				case 'C':
					Wall breakableWall = new Wall(model, cell);
					breakableWall.setBombable(true);
					cell.setContent(breakableWall);
					model.getDynamicContent().add(breakableWall);
					break;
				case '-':
					result.freePositions.add(position);
					break;
				case 'p':
					result.playerPositions.add(position);
					break;
				case 'P':
					result.playerPositions.add(0, position);
					break;
				case 'E': // Exit
					if (create && model.getExit()==null) {
						Exit exit = new Exit(model, position);
						model.setExit(exit);
						cell.setContent(exit);
					}
					break;
				case 'D': // Destroyable object i.e. Antenna
					if (create) {
						Antenna antenna = new Antenna(model, cell);
						cell.setContent(antenna);
						model.getDynamicContent().add(antenna);
					}
					
					break;
				case 'T': // Transparent
					cell.setContent(Transparent.getInstance());
				}
				idx++;
			}
			idxY++;
		}
		return result;
	}
	
	static class MapPositionInfo {
		List<MapPosition> playerPositions = new ArrayList<MapPosition>();
		List<MapPosition> freePositions = new ArrayList<MapPosition>();
		SimpleMap map;
	}


}
