package de.neuenberger.games.bomberman.model;

import de.neuenberger.games.core.model.NCell;
import de.neuenberger.games.core.model.NCellContent;


public class Bomb extends DynamicCellContent implements NCellContent {
	private long deploymenttime = System.currentTimeMillis();
	private long explosiontime = deploymenttime + 3000;
	private Player originator;
	private int flame;
	private NCell cell;
	private boolean exploding;
	
	
	public Bomb(Player player, NCell cell) {
		super(player.getModel());
		originator = player;
		flame = player.getFlames();
		this.cell = cell;
		setPosition(player.getPosition());
		setOrientation(player.getOrientation());
		cell.setContent(this);
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
	public Player getOriginator() {
		return originator;
	}
	public void setOriginator(Player originator) {
		this.originator = originator;
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
	
}
