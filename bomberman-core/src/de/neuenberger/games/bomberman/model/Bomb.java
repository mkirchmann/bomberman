package de.neuenberger.games.bomberman.model;

import de.neuenberger.games.core.model.MapPosition;
import de.neuenberger.games.core.model.NCell;
import de.neuenberger.games.core.model.NCellContent;


public class Bomb extends DynamicCellContent implements NCellContent {
	private long deploymenttime = System.currentTimeMillis();
	private long explosiontime = deploymenttime + 3000;
	private int flame;
	private NCell cell;
	private boolean exploding;
	private Player originator;
	
	
	public Bomb(Player player, NCell cell) {
		this(player.getModel(), player.getPosition(), player.getOrientation(), player.getFlames(), cell);
		this.originator = player;
	}

	public Bomb(BombermanModel model, MapPosition position, Orientation orientation, int flames, NCell cell) {
		super(model);
		flame = flames;
		this.cell = cell;
		setPosition(position);
		setOrientation(orientation);
		cell.setContent(this);
		model.getDynamicContent().add(this);
	}

	public Bomb(BombermanModel model, MapPosition position, Orientation orientation, int flames, NCell cell,
			long delay) {
		this(model, position, orientation, flames, cell);
		explosiontime = deploymenttime + delay;
	}

	public long getDeploymenttime() {
		return deploymenttime;
	}
	public void setDeploymenttime(long deploymenttime) {
		this.deploymenttime = deploymenttime;
	}
	public long getExplosiontime() {
		return explosiontime;
	}
	public void setExplosiontime(long explosiontime) {
		this.explosiontime = explosiontime;
	}
	public int getFlame() {
		return flame;
	}
	public void setFlame(int flame) {
		this.flame = flame;
	}
	public NCell getCell() {
		return cell;
	}
	public void setExploding(boolean b) {
		exploding = b;
	}
	public boolean isExploding() {
		return exploding;
	}
	
	@Override
	public void hitBy(IDynamicCellContent bomb) {
		if (bomb instanceof Fire && !exploding) {
			long timefrozen = System.currentTimeMillis();
			//if (timefrozen>((Fire)bomb).getCeaseTime()-800l) {
				explosiontime = timefrozen;
				exploding = true;
			//}
		}
	}
	@Override
	public boolean isDangerousFor(LiveContent monster) {
		return true;
	}

	public Player getOriginator() {
		return originator;
	}
	
}
