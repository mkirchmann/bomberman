package de.neuenberger.games.bomberman.presenter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import de.neuenberger.games.bomberman.BombermanGame;
import de.neuenberger.games.bomberman.model.Bomb;
import de.neuenberger.games.bomberman.model.BombermanModel;
import de.neuenberger.games.bomberman.model.Fire;
import de.neuenberger.games.bomberman.model.IDynamicCellContent;
import de.neuenberger.games.bomberman.model.ILivecontent;
import de.neuenberger.games.bomberman.model.Wall;
import de.neuenberger.games.core.mesh.BoxCreator;
import de.neuenberger.games.core.model.MapPosition;
import de.neuenberger.games.core.model.NCell;
import de.neuenberger.games.core.resource.Resource;
import de.neuenberger.games.core.resource.ResourceManager;
import de.neuenberger.games.core.resource.ResourceType;

public class BombPresenter extends BaseSubPresenter<Bomb, IDynamicCellContent> {

	private static final long FIRE_TIME = 1000;
	private Resource<StillModel> bombModel;
	Matrix4 transformation = new Matrix4();

	BombPresenter(BombermanModel model) {
		super(Bomb.class, model);
	}

	public void create() {
		super.create();
		bombModel = ResourceManager.getInstance().addLoadRequest("bomb.obj", ResourceType.OBJECT);

	}

	@Override
	public void render(List<Bomb> bombs, Camera camera, float delta) {
		getShader().begin();
		getShader().setUniformi("u_texture", 0);
		getShader().setUniformf("u_alpha", 1f);
		for (Bomb bomb : bombs) {
			if (bomb.getExplosiontime() < System.currentTimeMillis()) {
				// explosion
				bomb.setExploding(true);
				emitExplosion(camera, bomb);
				bomb.getCell().setContent(null);
				getModel().getDynamicContent().remove(bomb);
			} else {
				position(bomb, camera);
				render(bomb);
			}
		}
		getShader().end();
	}

	private <M extends IDynamicCellContent> void emitExplosion(Camera camera,
			Bomb bomb) {
		for (int y = 0; y >= -bomb.getFlame(); y--) {
			int posX = bomb.getPosition().getX();
			int posY = bomb.getPosition().getY() + y;
			boolean isWall = renderAndHitTest(bomb, posX, posY);
			if (isWall) {
				break;
			}
		}
		for (int y = 1; y <= bomb.getFlame(); y++) {
			int posX = bomb.getPosition().getX();
			int posY = bomb.getPosition().getY() + y;
			boolean isWall = renderAndHitTest(bomb, posX, posY);
			if (isWall) {
				break;
			}
		}
		for (int x = -1; x >= -bomb.getFlame(); x--) {
			int posX = bomb.getPosition().getX() + x;
			int y = bomb.getPosition().getY();
			boolean isWall = renderAndHitTest(bomb, posX, y);
			if (isWall) {
				break;
			}
		}

		for (int x = 1; x <= bomb.getFlame(); x++) {
			int posX = bomb.getPosition().getX() + x;
			int y = bomb.getPosition().getY();
			boolean isWall = renderAndHitTest(bomb, posX, y);
			if (isWall) {
				break;
			}
		}
	}

	private <M extends IDynamicCellContent> boolean renderAndHitTest(Bomb bomb,
			int posX, int posY) {
		boolean result = false;
		MapPosition position = MapPosition.valueOf(posX, posY);
		NCell cell = getModel().getMap().getCell(position);
		if (cell != null && cell.getContent() instanceof Wall) {
			result = true;
		}

		Fire fire = getModel()
				.getDynamicContentByPosition(position, Fire.class);
		long ceaseTime = bomb.getExplosiontime() + FIRE_TIME;
		if (fire == null) {
			if (result == false || ((Wall) cell.getContent()).isBombable()) {
				getModel().getDynamicContent().add(
						new Fire(getModel(), position, ceaseTime));
			}
		} else {
			fire.setCeaseTime(Math.max(fire.getCeaseTime(), ceaseTime));
		}
		return result;
	}

	public void dispose() {
		super.dispose();
	}

	@Override
	protected void render(Bomb m) {
		this.bombModel.getResource().render(getShader());
	}

}
