package de.neuenberger.games.bomberman.presenter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.neuenberger.games.bomberman.BombermanGame;
import de.neuenberger.games.core.resource.ResourceManager;
import de.neuenberger.games.core.resource.ResourceType;

public class LoadScreen extends DefaultScreen {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	private BombermanPresenter bombermanPresenter;
	private boolean pushed;
	private BitmapFont font;
	private Color textColor;

	public LoadScreen(BombermanGame game) {
		super(game);
	}

	
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(1, h/w);
		batch = new SpriteBatch();
		
		texture = new Texture(Gdx.files.internal("BOMBlaster.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(texture, 0, 0, 800, 600);
		
		sprite = new Sprite(region);
		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
		font = (BitmapFont) ResourceManager.getInstance().addLoadRequest("arial-15", ResourceType.FONT).getResource();
		bombermanPresenter=new BombermanPresenter(game);
		bombermanPresenter.create();
		textColor = new Color(0.7f,0.7f,0.7f,1f);
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void render(float delta) {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.draw(batch);
		font.setColor(textColor);
		font.draw(batch, "u", (int)(Gdx.graphics.getWidth()*0.5), (int)(Gdx.graphics.getHeight()*0.6));
		batch.end();
		
		
		if (ResourceManager.getInstance().isAllLoaded()) {
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				if (!pushed) {
					bombermanPresenter.postAsyncCreate();
					bombermanPresenter.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				}
				pushed=true;
			} else if (pushed==true) {
				game.setScreen(bombermanPresenter);
			}
		}
	}
	

}
