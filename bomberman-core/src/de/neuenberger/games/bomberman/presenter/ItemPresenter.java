package de.neuenberger.games.bomberman.presenter;

import java.util.List;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;

import de.neuenberger.games.bomberman.model.BombermanModel;
import de.neuenberger.games.bomberman.model.Item;
import de.neuenberger.games.bomberman.model.Player;
import de.neuenberger.games.core.mesh.BoxCreator;
import de.neuenberger.games.core.resource.Resource;
import de.neuenberger.games.core.resource.ResourceManager;
import de.neuenberger.games.core.resource.ResourceType;

public class ItemPresenter extends BaseSubPresenter<Item, Player>{

	private Resource<Texture> textureBomb;
	private Resource<Texture> textureFlame;
	private Mesh itemModel;
	private Resource<Texture> textureHeart;

	ItemPresenter(BombermanModel model) {
		super(Item.class, model);
	}
	
	@Override
	public void create() {
		super.create();
		
		itemModel = new BoxCreator().create(1);
		transformation.idt();
		transformation.translate(0, 0.05f, 0);
		itemModel.transform(transformation);
		ResourceManager rm = ResourceManager.getInstance();
		textureBomb = rm.addLoadRequest("item_bomb.png", ResourceType.TEXTURE);
		textureFlame = rm.addLoadRequest("item_flame.png", ResourceType.TEXTURE);
		textureHeart = rm.addLoadRequest("item_heart.png", ResourceType.TEXTURE);
	}

	@Override
	protected void render(Item m) {
		getShader().setUniformf("u_alpha", 1f);
		switch (m.getType()) {
		case BOMB:
			textureBomb.getResource().bind();
			break;
		case FLAME:
			textureFlame.getResource().bind();
			break;
		}
		itemModel.render(getShader(), GL20.GL_TRIANGLES);
	}
	
	@Override
	protected List<Player> getHitTestList() {
		return getModel().getDynamicContent(Player.class);
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
}
