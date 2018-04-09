package de.neuenberger.games.bomberman.logic;

import de.neuenberger.games.bomberman.model.BombermanModel;
import de.neuenberger.games.bomberman.model.Exit;
import de.neuenberger.games.bomberman.model.ILivecontent;
import de.neuenberger.games.bomberman.model.Player;
import de.neuenberger.games.core.model.MapContext;
import de.neuenberger.games.core.model.MapPosition;
import de.neuenberger.games.core.model.NCell;
import de.neuenberger.games.core.model.NCellContent;

public class MoveLogic {
	
	private ILivecontent liveContent;
	private BombermanModel model;
	private MapContext mapContext;
	
	public MoveLogic(ILivecontent player, BombermanModel model, MapContext  mapContext) {
		super();
		this.liveContent = player;
		this.model = model;
		this.mapContext = mapContext;
	}
	
	
	public void setOffsets(float offsetX, float offsetY) {
		setOffsets(liveContent, offsetX, offsetY);
	}
	
	public void setOffsets(ILivecontent liveContent, float offsetX, float offsetY) {
		int posX = liveContent.getPosition().getX();
		int posY = liveContent.getPosition().getY();
		int targetX = posX;
		int targetY = posY;
		
		
		if (offsetX > 0) {
			targetX++;
			if (offsetX>1) {
				offsetX = 1;
			}
		} else if (offsetX < 0) {
			targetX--;
			if (offsetX<-1) {
				offsetX = -1;
			}
		}
		if (offsetY > 0) {
			targetY++;
			if (offsetY>1) {
				offsetY = 1;
			}
		} else if (offsetY < 0) {
			targetY--;
			if (offsetY<-1) {
				offsetY = -1;
			}
		}
		if (targetX != posX) {
			NCell tcell = model.getMap().getCell(
					MapPosition.valueOf(targetX, posY));
			if (isBlocked(tcell)) {
				offsetX = 0;
			}
		}
		if (targetY != posY) {
			NCell tcell = model.getMap().getCell(
					MapPosition.valueOf(posX, targetY));
			if (isBlocked(tcell)) {
				offsetY = 0;
			}
		}

		if (offsetX > 0.5) {
			offsetX -= 1;
			posX += 1;
		}
		if (offsetX < -0.5) {
			offsetX += 1;
			posX -= 1;
		}
		if (offsetY > 0.5) {
			offsetY -= 1;
			posY += 1;
		}
		if (offsetY < -0.5) {
			offsetY += 1;
			posY -= 1;
		}
		liveContent.setOffsetX(offsetX);
		liveContent.setOffsetY(offsetY);
		liveContent.setPosition(MapPosition.valueOf(posX, posY));
	}


	private boolean isBlocked(NCell tcell) {
		boolean result = false;
		if (tcell != null) {
			NCellContent content = tcell.getContent();
			result = mapContext.isBlockingContent(content);
		}
		return result;
	}
}
