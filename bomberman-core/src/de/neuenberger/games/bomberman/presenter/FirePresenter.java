package de.neuenberger.games.bomberman.presenter;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;

import de.neuenberger.games.bomberman.model.Bomb;
import de.neuenberger.games.bomberman.model.BombermanModel;
import de.neuenberger.games.bomberman.model.Fire;
import de.neuenberger.games.bomberman.model.IDynamicCellContent;
import de.neuenberger.games.bomberman.model.ILivecontent;
import de.neuenberger.games.core.mesh.BoxCreator;
import de.neuenberger.games.core.resource.Resource;
import de.neuenberger.games.core.resource.ResourceManager;
import de.neuenberger.games.core.resource.ResourceType;

public class FirePresenter extends BaseSubPresenter<Fire, IDynamicCellContent>{

	private Mesh fire;
	private Resource<Texture> textureFire;

	FirePresenter(BombermanModel model) {
		super(Fire.class, model);
	}

	@Override
	public void create() {
		super.create();
		
		fire = new BoxCreator().create(6);
		fire.scale(1, 1.1f, 1);
		textureFire = ResourceManager.getInstance().addLoadRequest("fire.png", ResourceType.TEXTURE);
	}
	
	@Override
	protected void render(Fire m) {
		if (m.getCeaseTime()<System.currentTimeMillis()) {
			getModel().getDynamicContent().remove(m);
		} else {
			textureFire.getResource().bind();
			getShader().setUniformf("u_alpha", Math.max(0.2f,0.001f*(m.getCeaseTime()-System.currentTimeMillis())));
			fire.render(getShader(), GL20.GL_TRIANGLES);
		}
	}
	
	@Override
	protected List<IDynamicCellContent> getHitTestList() {
		return new ArrayList<IDynamicCellContent>(getModel().getDynamicContent());
	}
	
	@Override
	protected void hitTest(Fire m, List<IDynamicCellContent> hitTestList) {
		long frozenCurrentTime = System.currentTimeMillis();
		for (IDynamicCellContent h : hitTestList) {
			if (h!=m && h.getPosition()==m.getPosition()) {
				if (m.getCeaseTime()<frozenCurrentTime) {
					m.hitBy(h);
				} else if (h instanceof ILivecontent || h instanceof Bomb) {
					m.hitBy(h);
				}
			}
		}
	}

}
