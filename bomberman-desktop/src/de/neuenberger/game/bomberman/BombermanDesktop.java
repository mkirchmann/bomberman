package de.neuenberger.game.bomberman;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.neuenberger.games.bomberman.BombermanGame;

public class BombermanDesktop {
	public static void main(String[] args) {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "Test";
        configuration.useGL20 = true;
        configuration.width = 800;
        configuration.height = 600;

        BombermanGame game = new BombermanGame();
        LwjglApplication application = new LwjglApplication(game, configuration);
    }
}
