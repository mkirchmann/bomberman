package de.neuenberger.games.bomberman.presenter;

import java.util.List;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;

import de.neuenberger.games.bomberman.model.BombermanModel;
import de.neuenberger.games.bomberman.model.Item;
import de.neuenberger.games.bomberman.model.Player;
import de.neuenberger.games.core.mesh.BoxCreator;

public class ItemPresenter extends BaseSubPresenter<Item, Player>{

	private Texture textureBomb;
	private Texture textureFlame;
	private Mesh itemModel;
	private Texture textureHeart;

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
		textureBomb = BombermanPresenter.loadTexture("item_bomb.png");
		textureFlame = BombermanPresenter.loadTexture("item_flame.png");
		textureHeart = BombermanPresenter.loadTexture("item_heart.png");
	}

	@Override
	protected void render(Item m) {
		getShader().setUniformf("u_alpha", 1f);
		switch (m.getType()) {
		case BOMB:
			textureBomb.bind();
			break;
		case FLAME:
			textureFlame.bind();
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
		textureBomb.dispose();
		textureFlame.dispose();
		textureHeart.dispose();
	}
}
