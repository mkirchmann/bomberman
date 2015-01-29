package de.neuenberger.games.bomberman.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.neuenberger.games.core.model.MapPosition;
import de.neuenberger.games.core.model.SimpleMap;

public class BombermanModel {
	List<IDynamicCellContent> dynamicContent = new ArrayList<IDynamicCellContent>();
	SimpleMap map;

	int level=-1;

	Exit exit;
	private Long levelCompleteTime;
	private Long gameOverTime;
	public SimpleMap getMap() {
		return map;
	}

	public void setMap(SimpleMap map) {
		this.map = map;
	}

	public List<IDynamicCellContent> getDynamicContent() {
		return dynamicContent;
	}

	public <M extends IDynamicCellContent> List<M> getDynamicContent(
			Class<M> class1) {
		List<M> list = new LinkedList<M>();
		for (IDynamicCellContent m : dynamicContent) {
			if (class1.isInstance(m)) {
				list.add(class1.cast(m));
			}
		}
		return list;
	}

	public void resetGame() {
		// TODO Game Over
	}

	public void resetLevel() {

	}

	public <M extends IDynamicCellContent> M getDynamicContentByPosition(
			MapPosition position, Class<M> class1) {
		List<M> content = getDynamicContent(class1);
		M result = null;
		for (M dc : content) {
			if (dc.getPosition() == position) {
				result = dc;
				break;
			}
		}
		return result;
	}
	
	public <M extends IDynamicCellContent> M getDynamicContentByXCoordinate(
			int x, Class<M> class1) {
		List<M> content = getDynamicContent(class1);
		M result = null;
		for (M dc : content) {
			if (dc.getPosition().getX() == x) {
				result = dc;
				break;
			}
		}
		return result;
	}
	
	public <M extends IDynamicCellContent> M getDynamicContentByYCoordinate(
			int y, Class<M> class1) {
		List<M> content = getDynamicContent(class1);
		M result = null;
		for (M dc : content) {
			if (dc.getPosition().getY() == y) {
				result = dc;
				break;
			}
		}
		return result;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Player getClosestPlayer(MapPosition position) {
		List<Player> dynamicContent = getDynamicContent(Player.class);
		Player player = SimpleMap.getClosest(dynamicContent, position);

		return player;
	}

	public Exit getExit() {
		return exit;
	}

	public void setExit(Exit exit) {
		this.exit = exit;
	}

	public void setLevelCompleteTime(Long currentTimeMillis) {
		this.levelCompleteTime = currentTimeMillis;
	}

	public Long getLevelCompleteTime() {
		return levelCompleteTime;
	}
	
	public boolean isLevelComplete() {
		return levelCompleteTime!=null;
	}
	
	public boolean isGameOver() {
		return gameOverTime!=null;
	}

	public Long getGameOverTime() {
		return gameOverTime;
	}

	public void setGameOverTime(Long gameOverTime) {
		this.gameOverTime = gameOverTime;
	}
}
