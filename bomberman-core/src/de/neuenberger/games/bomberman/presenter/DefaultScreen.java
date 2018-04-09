package de.neuenberger.games.bomberman.presenter;

import com.badlogic.gdx.Screen;

import de.neuenberger.games.bomberman.BombermanGame;

public abstract class DefaultScreen implements Screen {
	protected BombermanGame game;

	public DefaultScreen(BombermanGame game) {
		this.game = game;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
//		dispose();
	}

	@Override
	public void resume() { 
//		Resources.getInstance().reInit();
	}

	@Override
	public void dispose() {
//		Resources.getInstance().dispose();
	}
	
	@Override
	public void hide() {
		//  Auto-generated method stub
		
	}
	
	@Override
	public void show() {
		// Auto-generated method stub
		
	}
	
}