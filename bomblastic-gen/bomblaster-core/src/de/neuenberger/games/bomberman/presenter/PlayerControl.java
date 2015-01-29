package de.neuenberger.games.bomberman.presenter;

import java.util.List;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;

import de.neuenberger.games.bomberman.ki.monster.LiveContentMover;
import de.neuenberger.games.bomberman.logic.MoveLogic;
import de.neuenberger.games.bomberman.model.Bomb;
import de.neuenberger.games.bomberman.model.BombermanModel;
import de.neuenberger.games.bomberman.model.DynamicCellContent;
import de.neuenberger.games.bomberman.model.ILivecontent;
import de.neuenberger.games.bomberman.model.Player;
import de.neuenberger.games.core.ki.Path;
import de.neuenberger.games.core.ki.PathFinder;
import de.neuenberger.games.core.model.DefaultMapContext;
import de.neuenberger.games.core.model.MapPosition;
import de.neuenberger.games.core.model.NCell;

public class PlayerControl {

	private Player player;
	private BombermanModel model;

	MoveLogic logic;

	LiveContentMover mover;
	
	public PlayerControl(Player p, BombermanModel model) {
		this.player = p;
		this.model = model;
		logic = new MoveLogic(player, model);
		mover = new LiveContentMover(model);
	}

	public void control(Input input, float delta) {
		if (mover.move(delta, player)) {
			return;
		}
		float add = delta * 10;
		boolean change = false;
		float offsetX = player.getOffsetX();
		float offsetY = player.getOffsetY();

		if (!player.isDying()) {
			if (input.isKeyPressed(Input.Keys.UP)) {
				player.setOrientation(DynamicCellContent.Orientation.MIN_Y);
				offsetY -= add;
				change = true;
			}
			if (input.isKeyPressed(Input.Keys.DOWN)) {
				player.setOrientation(DynamicCellContent.Orientation.POS_Y);
				offsetY += add;
				change = true;
			}
			if (input.isKeyPressed(Input.Keys.LEFT)) {
				player.setOrientation(DynamicCellContent.Orientation.MIN_X);
				offsetX -= add;
				change = true;
			}
			if (input.isKeyPressed(Input.Keys.RIGHT)) {
				player.setOrientation(DynamicCellContent.Orientation.POS_X);
				offsetX += add;
				change = true;
			}
			if (input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
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
				Bomb bomb = new Bomb(player, cell);
				model.getDynamicContent().add(bomb);

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

	public void handleLeftClick(MapPosition position, float deltaTime) {
		boolean locked = false;
		if (player.getLockPathSeekingUntil()!=null) {
			if (System.currentTimeMillis()<player.getLockPathSeekingUntil()) {
				locked = true;
			} else {
				player.setLockPathSeekingUntil(null);
			}
		}
		if (!locked) {
			Path path = PathFinder.findPath(model.getMap(), getPosition(), position, 16., DefaultMapContext.getInstance());
			if (path!=null) {
				player.setLastPath(path);
				player.setTargetPosition(null);
				player.setLockPathSeekingUntil(System.currentTimeMillis()+100);
			}
		}
	}

	public ILivecontent getPlayer() {
		return player;
	}

}
