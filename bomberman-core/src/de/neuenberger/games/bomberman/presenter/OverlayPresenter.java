package de.neuenberger.games.bomberman.presenter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.neuenberger.games.bomberman.model.BombermanModel;
import de.neuenberger.games.bomberman.model.Player;
import de.neuenberger.games.core.resource.FontResource;
import de.neuenberger.games.core.resource.ResourceManager;
import de.neuenberger.games.core.resource.ResourceType;

public class OverlayPresenter {
	private final BombermanModel model;

	private OrthographicCamera overlayCamera;

	private SpriteBatch batch;

	private FontResource textResource;

	private FontResource symbolsResource;

	private FontResource controlsResource;

	List<OverlayButton> buttons;

	private OverlayButton fireButton;

	private OverlayButton upButton;

	private OverlayButton downButton;

	private OverlayButton leftButton;

	private OverlayButton rightButton;

	ControlButtonStates controlButtonStates = new ControlButtonStates();

	OverlayPresenter(BombermanModel theModel) {
		model = theModel;
	}

	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		batch = new SpriteBatch();
		overlayCamera = new OrthographicCamera();
		overlayCamera.setToOrtho(true, w, h);

		textResource = (FontResource) ResourceManager.getInstance().addLoadRequest("arial-15", ResourceType.FONT);
		symbolsResource = (FontResource) ResourceManager.getInstance().addLoadRequest("symbols", ResourceType.FONT);
		controlsResource = (FontResource) ResourceManager.getInstance().addLoadRequest("controls", ResourceType.FONT);

		fireButton = new OverlayButton(controlsResource.getResource());
		upButton = new OverlayButton(controlsResource.getResource());
		downButton = new OverlayButton(controlsResource.getResource());
		leftButton = new OverlayButton(controlsResource.getResource());
		rightButton = new OverlayButton(controlsResource.getResource());
		buttons = Arrays.asList(fireButton, upButton, downButton, leftButton, rightButton);
		layout(w, h);
	}

	public void resize(int width, int height) {
		overlayCamera.setToOrtho(true, width, height);
		layout(width, height);
	}

	private void layout(float width, float height) {
		// arrange buttons.
		int margin = 10;
		int buttonWidth = 214;
		int buttonHeight = 224;
		fireButton.setXYWH(margin, height - buttonHeight - margin, buttonWidth, buttonHeight);

		upButton.setXYWH(width - 1.8f * buttonWidth - margin, height - 2.6f * buttonHeight - margin, buttonWidth,
				buttonHeight);
		downButton.setXYWH(width - 1.8f * buttonWidth - margin, height - buttonHeight - margin, buttonWidth,
				buttonHeight);
		leftButton.setXYWH(width - 2.6f * buttonWidth - margin, height - 1.8f * buttonHeight - margin, buttonWidth,
				buttonHeight);
		rightButton.setXYWH(width - buttonWidth - margin, height - 1.8f * buttonHeight - margin, buttonWidth,
				buttonHeight);

	}

	public void render() {
		batch.setProjectionMatrix(overlayCamera.combined);
		batch.begin();
		List<Player> players = model.getPlayers();
		String lives = players.stream().map(x -> "" + x.getLives()).collect(Collectors.joining(", "));

		int panelCount = model.getPanelCount();
		int monsterCount = model.getMonsterCount();
		int antennas = model.getAntennas();

		BitmapFont font = textResource.getResource();
		BitmapFont symbols = symbolsResource.getResource();
		BitmapFont controls = controlsResource.getResource();

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

		for (OverlayButton button : buttons) {
			button.render(batch);
		}

		batch.end();
	}

	float getStatusPosition() {
		return 20.0f;
	}

	public void dispose() {
	}

	public ControlButtonStates control(Input input) {
		for (OverlayButton overlayButton : buttons) {
			overlayButton.setPressed(false);
		}
		for (int i = 0; i < 10; i++) {
			if (input.isTouched(i)) {
				for (OverlayButton button : buttons) {
					button.testHit(input.getX(i), input.getY(i));
				}
			}
		}
		controlButtonStates.setLeftButtonPressed(leftButton.isPressed());
		controlButtonStates.setRightButtonPressed(rightButton.isPressed());
		controlButtonStates.setUpButtonPressed(upButton.isPressed());
		controlButtonStates.setDownButtonPressed(downButton.isPressed());
		controlButtonStates.setFireButtonPressed(fireButton.isPressed());
		return controlButtonStates;
	}

	class OverlayButton {
		private final String pressedString;
		private final String releasedString;
		private final BitmapFont controlsFont;
		private float x;
		private float y;
		private float w;
		private float h;

		private boolean pressed;

		OverlayButton(BitmapFont theControlsFont) {
			this(theControlsFont, "b", "B");
		}

		public boolean isPressed() {
			return pressed;
		}

		public void setPressed(boolean b) {
			pressed = b;
		}

		OverlayButton(BitmapFont theControlsFont, String thePressed, String theReleased) {
			controlsFont = theControlsFont;
			pressedString = thePressed;
			releasedString = theReleased;
		}

		public void setXYWH(float theX, float theY, float theWidth, float theHeight) {
			x = theX;
			y = theY;
			w = theWidth;
			h = theHeight;
		}

		public boolean testHit(float hitX, float hitY) {
			pressed = x < hitX && hitX < x + w && y < hitY && hitY < y + h;
			return pressed;
		}

		public void render(SpriteBatch batch) {
			String string;
			if (pressed) {
				string = pressedString;
			} else {
				string = releasedString;
			}
			controlsFont.draw(batch, string, x, y);
		}
	}
}
