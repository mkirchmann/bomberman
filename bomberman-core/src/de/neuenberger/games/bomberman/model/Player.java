package de.neuenberger.games.bomberman.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Player extends LiveContent implements ILivecontent {
	public static final String PROP_LIVES = "lives";
	public static final int DEFAULT_LIVES = 2;
	int bombs = 2;
	int flames = 3;
	int lives = DEFAULT_LIVES;

	PropertyChangeSupport support = new PropertyChangeSupport(this);

	public Player(BombermanModel model) {
		super(model);
		setSpeed(10);
	}

	public int getBombs() {
		return bombs;
	}

	public void setBombs(int bombs) {
		this.bombs = bombs;
	}

	public int getFlames() {
		return flames;
	}

	public void setFlames(int flames) {
		this.flames = flames;
	}

	@Override
	public void hitBy(IDynamicCellContent bomb) {
		// no call to super.
		if (bomb instanceof IKilling && !isDying()) {
			die();
		}
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		PropertyChangeEvent event = new PropertyChangeEvent(this, PROP_LIVES,
				this.lives, lives);
		this.lives = lives;
		support.firePropertyChange(event);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}

	public void die() {
		setTargetPosition(null);
		setLastPath(null);
		setDiedAt(System.currentTimeMillis());
		setLives(lives - 1);
	}
}
