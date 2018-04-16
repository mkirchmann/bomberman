package de.neuenberger.games.bomberman.presenter;

import java.util.List;

import com.badlogic.gdx.graphics.Camera;

import de.neuenberger.games.bomberman.logic.MoveLogic;
import de.neuenberger.games.bomberman.model.Bomb;
import de.neuenberger.games.bomberman.model.BombermanModel;
import de.neuenberger.games.bomberman.model.DynamicCellContent;
import de.neuenberger.games.bomberman.model.ILivecontent;
import de.neuenberger.games.bomberman.model.Player;
import de.neuenberger.games.bomberman.model.PlayerMapContext;
import de.neuenberger.games.core.model.MapPosition;
import de.neuenberger.games.core.model.NCell;

public class PlayerControl {

	private Player player;
	private BombermanModel model;

	MoveLogic logic;

	public PlayerControl(Player p, BombermanModel model) {
		this.player = p;
		this.model = model;
		logic = new MoveLogic(player, model, PlayerMapContext.getInstance());
	}

	public void control(ControlButtonStates controlButtonStates, float delta) {
		float add = delta * 10;
		boolean change = false;
		float offsetX = player.getOffsetX();
		float offsetY = player.getOffsetY();

		if (!player.isDying()) {
			if (controlButtonStates.isUpButtonPressed()) {
				player.setOrientation(DynamicCellContent.Orientation.MIN_Y);
				offsetY -= add;
				change = true;
			}
			if (controlButtonStates.isDownButtonPressed()) {
				player.setOrientation(DynamicCellContent.Orientation.POS_Y);
				offsetY += add;
				change = true;
			}
			if (controlButtonStates.isLeftButtonPressed()) {
				player.setOrientation(DynamicCellContent.Orientation.MIN_X);
				offsetX -= add;
				change = true;
			}
			if (controlButtonStates.isRightButtonPressed()) {
				player.setOrientation(DynamicCellContent.Orientation.POS_X);
				offsetX += add;
				change = true;
			}
			if (controlButtonStates.isFireButtonPressed()) {
				tryLayBomb();
			}
		}
		if (change) {
			logic.setOffsets(offsetX, offsetY);
		}
	}

	public void tryLayBomb() {
		NCell cell = model.getMap().getCell(player.getPosition());
		// lay bomb
		// check if a bomb is in place already
		boolean free = (cell != null && cell.getContent() == null);
		// check if player has bombs left to lay
		if (free) {
			List<Bomb> content = model.getDynamicContent(Bomb.class);
			int count = getCount(content);
			if (count < player.getBombs()) {
				new Bomb(player, cell);
			}
		}
	}

	private int getCount(List<Bomb> content) {
		int count = 0;
		for (Bomb bomb : content) {
			if (player == bomb.getOriginator() && !bomb.isExploding()) {
				count++;
			}
		}
		return count;
	}

	public void apply(Camera camera) {
		float x = BombermanPresenter.SPACE_PER_TILE
				* ((float) player.getPosition().getX() + player.getOffsetX());
		float y = BombermanPresenter.SPACE_PER_TILE
				* ((float) player.getPosition().getY() + player.getOffsetY());
		x = Math.min(100, Math.max(x, 18));
		y = Math.min(60, Math.max(y, 18));
		// System.out.println("x=" + x + ", y=" + y);
		camera.position.set(x + 2.0f * BombermanPresenter.SPACE_PER_TILE, 20, y
				+ 4.0f * BombermanPresenter.SPACE_PER_TILE);
		// camera.direction.set(1,0,1).nor();
		camera.lookAt(x, 1, y);
		camera.up.set(0, 1, 0);
		camera.update();
		camDebug(camera);
	}

	public static void camDebug(Camera camera) {
		// System.out.println("Camera debug: pos="+camera.position+" dir="+camera.direction+" up="+camera.up);
	}

	public MapPosition getPosition() {
		return player.getPosition();
	}

	public ILivecontent getPlayer() {
		return player;
	}

}
