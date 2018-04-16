package de.neuenberger.games.bomberman.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;

import de.neuenberger.games.bomberman.ki.monster.BaseMonsterMover;
import de.neuenberger.games.bomberman.ki.monster.IMonsterMover;
import de.neuenberger.games.bomberman.ki.monster.MonsterMoverEasy;
import de.neuenberger.games.bomberman.ki.monster.MonsterMoverHard;
import de.neuenberger.games.bomberman.ki.monster.MonsterMoverHarder;
import de.neuenberger.games.bomberman.ki.monster.MonsterMoverHardest;
import de.neuenberger.games.bomberman.ki.monster.MonsterMoverMedium;
import de.neuenberger.games.bomberman.model.BombermanModel;
import de.neuenberger.games.bomberman.model.Monster;
import de.neuenberger.games.bomberman.model.Player;
import de.neuenberger.games.core.resource.ObjectResource;
import de.neuenberger.games.core.resource.Resource;
import de.neuenberger.games.core.resource.ResourceManager;
import de.neuenberger.games.core.resource.ResourceType;

public class MonsterPresenter extends BaseSubPresenter<Monster, Player> {
	private Resource<StillModel> monsterModel;

	Map<Monster.KiLevel,IMonsterMover> mapMonsterMover = new HashMap<Monster.KiLevel,IMonsterMover>();

	private Resource<Texture> textureGhostGreen;

	private Resource<Texture> textureGhostRed;

	private Resource<Texture> textureGhostPurple;

	private ObjectResource greenMonster;

	private ObjectResource purpleMonster;

	private ObjectResource redMonster;

	private Resource<Texture> textureGhostPurpleBlack;

	private ObjectResource purpleBlackMonster;

	public MonsterPresenter(BombermanModel model) {
		super(Monster.class, model);
	}

	public void create() {
		super.create();
		ResourceManager rm = ResourceManager.getInstance();
		monsterModel = rm.addLoadRequest("ghost.obj", ResourceType.OBJECT);

		textureGhostGreen = rm.addLoadRequest("ghost-green.png", ResourceType.TEXTURE);
		textureGhostRed = rm.addLoadRequest("ghost-red.png", ResourceType.TEXTURE);
		textureGhostPurple = rm.addLoadRequest("ghost-purple.png", ResourceType.TEXTURE);
		textureGhostPurpleBlack = rm.addLoadRequest("ghost-purple-black.png", ResourceType.TEXTURE);

		greenMonster = ((ObjectResource) monsterModel).clone();
		greenMonster.setTexture(textureGhostGreen);

		purpleMonster = ((ObjectResource) monsterModel).clone();
		purpleMonster.setTexture(textureGhostPurple);

		purpleBlackMonster = ((ObjectResource) monsterModel).clone();
		purpleBlackMonster.setTexture(textureGhostPurpleBlack);

		redMonster = ((ObjectResource) monsterModel).clone();
		redMonster.setTexture(textureGhostRed);

		mapMonsterMover.put(Monster.KiLevel.Noob, new BaseMonsterMover(getModel()));
		mapMonsterMover.put(Monster.KiLevel.Easy, new MonsterMoverEasy(getModel()));
		mapMonsterMover.put(Monster.KiLevel.Medium, new MonsterMoverMedium(getModel()));
		mapMonsterMover.put(Monster.KiLevel.Hard, new MonsterMoverHard(getModel()));
		mapMonsterMover.put(Monster.KiLevel.Harder, new MonsterMoverHarder(getModel()));
		mapMonsterMover.put(Monster.KiLevel.Hardest, new MonsterMoverHardest(getModel()));
	}

	@Override
	protected List<Player> getHitTestList() {
		return getModel().getDynamicContent(Player.class);
	}

	public void dispose() {
		super.dispose();
	}

	@Override
	protected void render(Monster m) {
		float alpha = 1f;
		if (m.isDying()) {
			alpha = Math
					.max(0, 1.0f - 0.001f * (System.currentTimeMillis() - m
							.getDiedAt()));
		}
		
		getShader().setUniformf("u_alpha", alpha);
		switch (m.getKiLevel()) {
		case Easy:
			greenMonster.getResource().render(getShader());
			break;
		case Hard:
			redMonster.getResource().render(getShader());
			break;
		case Harder:
			purpleMonster.getResource().render(getShader());
		case Hardest:
			purpleBlackMonster.getResource().render(getShader());
			break;
		default:
			monsterModel.getResource().render(getShader());
		}

		if (alpha==0f) {
			getModel().getDynamicContent().remove(m);
		}
	}

	@Override
	protected void kiLogic(Monster m, float delta) {
		IMonsterMover mover = mapMonsterMover.get(m.getKiLevel());
		mover.move(delta, m);
	}
}
