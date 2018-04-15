package de.neuenberger.games.bomberman.presenter;

import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.neuenberger.games.bomberman.model.BombermanModel;
import de.neuenberger.games.bomberman.model.Player;

public class OverlayPresenter {
	private final BombermanModel model;

	private OrthographicCamera overlayCamera;

	private SpriteBatch batch;

	private BitmapFont font;
	private BitmapFont symbols;

	OverlayPresenter(BombermanModel theModel) {
		model = theModel;
	}

	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		overlayCamera = new OrthographicCamera();
		overlayCamera.setToOrtho(true, w, h);

		// font = new BitmapFont(Gdx.files.internal("eurostile.fnt"),
		// Gdx.files.internal("eurostile.tga"),
		// true);
		font = new BitmapFont(Gdx.files.internal("arial-15.fnt"), Gdx.files.internal("arial-15.png"), true);
		symbols = new BitmapFont(Gdx.files.internal("symbols.fnt"), Gdx.files.internal("symbols.png"), true);
	}

	public void resize(int width, int height) {
		overlayCamera.setToOrtho(true, width, height);
	}

	public void render() {
		batch.setProjectionMatrix(overlayCamera.combined);
		batch.begin();
		List<Player> players = model.getPlayers();
		String lives = players.stream().map(x -> "" + x.getLives()).collect(Collectors.joining(", "));

		int panelCount = model.getPanelCount();
		int monsterCount = model.getMonsterCount();
		int antennas = model.getAntennas();

		symbols.draw(batch, "L", 10.0f, getStatusPosition());
		font.draw(batch, "" + model.getLevel(), 25.0f, getStatusPosition());

		symbols.draw(batch, "M", 90.0f, getStatusPosition());
		font.draw(batch, "" + monsterCount, 105.0f, getStatusPosition());

		symbols.draw(batch, "P", 50.0f, getStatusPosition());
		font.draw(batch, "" + panelCount, 67.0f, getStatusPosition());

		symbols.draw(batch, "A", 130.0f, getStatusPosition());
		font.draw(batch, "" + antennas, 140.0f, getStatusPosition());

		symbols.draw(batch, "H", 160.0f, getStatusPosition()); // heart
		font.draw(batch, "" + lives, 179.0f, getStatusPosition());

		batch.end();
	}

	float getStatusPosition() {
		return 20.0f;
	}

	public void dispose() {
		font.dispose();
		symbols.dispose();
	}
}
