package de.neuenberger.games.bomberman.model;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import de.neuenberger.games.core.ki.Path;
import de.neuenberger.games.core.model.DefaultMapContext;
import de.neuenberger.games.core.model.MapContext;
import de.neuenberger.games.core.model.MapPosition;

public class Monster extends LiveContent implements ILivecontent, IKilling {

	private MapPosition targetPosition;
	
	private static final long DEFAULT_PATH_LOCK_TIME = 30000;
	public enum KiLevel {
		Noob(1.5f), Easy(1.75f), Medium, Hard(2.25f), Harder(2.5f, 25000l), Hardest(2.75f, 20000l);
		float speed;
		long pathlocktime;
		KiLevel() {
			this(2);
		}
		KiLevel(float speed) {
			this(speed,DEFAULT_PATH_LOCK_TIME);
		}
		KiLevel(float speed, long pathlocktime) {
			this.speed = speed;
			this.pathlocktime = pathlocktime;
		}
		
		public float getSpeed() {
			return speed;
		}
		public long getPathlocktime() {
			return pathlocktime;
		}
	}
	
	private KiLevel kiLevel;
	private MapContext mapContext = DefaultMapContext.getInstance();
	
	ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	public Monster(BombermanModel model, MapPosition position, KiLevel kiLevel) {
		super(model);
		this.setPosition(position);
		this.kiLevel = kiLevel;
	}
	
	@Override
	public void hitBy(IDynamicCellContent dc) {
		if (dc instanceof Player) {
			if (!isDying()) {
				dc.hitBy(this);
			}
		} else if (dc instanceof Item) {
			// do nothing / ignore
		} else {
			setDiedAt(System.currentTimeMillis());
		}
	}

	public MapPosition getTargetPosition() {
		return targetPosition;
	}

	public void setTargetPosition(MapPosition targetPosition) {
		this.targetPosition = targetPosition;
	}

	public KiLevel getKiLevel() {
		return kiLevel;
	}

	public void setKiLevel(KiLevel kiLevel) {
		this.kiLevel = kiLevel;
	}

	

	public float getSpeed() {
		return kiLevel.getSpeed();
	}

	public MapContext getMapContext() {
		return mapContext ;
	}

	public ReentrantReadWriteLock getLock() {
		return lock;
	}

}
