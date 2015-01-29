package de.neuenberger.game.bomberman;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import de.neuenberger.games.bomberman.BombermanGame;

public class MainActivity extends AndroidApplication {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = true;
		cfg.useWakelock = true;
		try {
			AlertDialog dialog = new AlertDialog.Builder(this).create();
			dialog.setMessage("Die Applikation wird jetzt gestartet!");
			dialog.show();
			initialize(new BombermanGame(), cfg);
		} catch (Error e) {
			AlertDialog dialog = new AlertDialog.Builder(this).create();
			dialog.setMessage("An error occured: "+e.toString());
			dialog.show();
			throw e;
		}
	}
}