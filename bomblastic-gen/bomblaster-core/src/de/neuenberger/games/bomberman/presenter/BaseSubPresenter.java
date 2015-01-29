package de.neuenberger.games.bomberman.presenter;

import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import de.neuenberger.games.bomberman.BombermanGame;
import de.neuenberger.games.bomberman.model.BombermanModel;
import de.neuenberger.games.bomberman.model.IDynamicCellContent;
import de.neuenberger.games.bomberman.model.Item;
import de.neuenberger.games.bomberman.model.LiveContent;
import de.neuenberger.games.bomberman.model.Player;
import de.neuenberger.games.core.model.MapPosition;

public abstract class BaseSubPresenter<M extends IDynamicCellContent, H extends IDynamicCellContent> {
	private Class<M> clazz;
	ShaderProgram shader;
	Matrix4 transformation = new Matrix4();
	private BombermanModel model;
	BaseSubPresenter(Class<M> clazz, BombermanModel model) {
		this.clazz = clazz;
		this.model = model;
	}
	
	public void create() {
		shader = createShader();
	}
	
	protected ShaderProgram createShader() {
		ShaderProgram fireShader = BombermanGame.createShader("simple");
		fireShader.setUniformf("u_lightdir", new Vector3());
		fireShader.setUniformf("u_alpha", 1f);
		return fireShader;
	}

	public void dispose() {
		shader.dispose();
		
	}
	public void render(List<M> render, Camera camera) {
		shader.begin();
		List<H> hitTestList = getHitTestList();
		for (M m : render) {
			position(m, camera);
			render(m);
			hitTest(m, hitTestList);
			kiLogic(m, Gdx.graphics.getDeltaTime());
		}
		
		
		shader.end();
	}

	protected void kiLogic(M m, float delta) {
		// do nothing in base impl
		
	}

	protected void hitTest(M m, List<H> hitTestList) {
		for (H h : hitTestList) {
			if (h!=m && h.getPosition()==m.getPosition()) {
				m.hitBy(h);
			}
		}
	}

	protected List<H> getHitTestList() {
		return Collections.emptyList();
	}

	protected abstract void render(M m);

	protected void position(M m, Camera camera) {
		
		float offsetX = 0;
		float offsetY = 0;
		
		if (m instanceof LiveContent) {
			offsetX = ((LiveContent) m).getOffsetX();
			offsetY = ((LiveContent) m).getOffsetY();
		}
		
		MapPosition position = m.getPosition();
		transformation.set(camera.combined);
		transformation.translate((position.getX()+offsetX)*BombermanPresenter.SPACE_PER_TILE, 0, (position.getY()+offsetY)*BombermanPresenter.SPACE_PER_TILE);
		shader.setUniformMatrix("u_projTrans", transformation);
	}

	public Class<M> getClazz() {
		return clazz;
	}

	public BombermanModel getModel() {
		return model;
	}

	public ShaderProgram getShader() {
		return shader;
	}
}
