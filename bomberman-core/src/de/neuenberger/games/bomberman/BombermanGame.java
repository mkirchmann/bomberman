package de.neuenberger.games.bomberman;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Mesh;

import de.neuenberger.games.bomberman.presenter.LoadScreen;
import de.neuenberger.games.core.resource.ResourceManager;
import de.neuenberger.games.core.resource.ShaderResource;

public class BombermanGame implements ApplicationListener {
	Mesh floorTile;

	float acc = 50.0f;
	

	enum CamType {

		Ortho(40), Perspective(5);
		float scale;

		CamType(float scale) {
			this.scale = scale;
		}

		public float getScale() {
			return scale;
		}
	}

	CamType camType = CamType.Perspective;

	private Screen screen;

	@Override
	public void create() {
		ResourceManager rm = ResourceManager.getInstance();
		rm.addResource("simple", new ShaderResource(rm, "simple"));
		rm.addResource("spinctex", new ShaderResource(rm, "spinctex"));
		rm.loadResources();
		screen = new LoadScreen(this);
		((LoadScreen)screen).create();
	}

	public static String getAssetsDirectory() {
		return System.getProperty("assets.dir", "");
	}

	public void resize(int width, int height) {
		screen.resize(width,height);
	}

	@Override
	public void render() {
		screen.render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		ResourceManager.getInstance().dispose();
	}

	public Mesh getFloorTile() {
		return floorTile;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

}
