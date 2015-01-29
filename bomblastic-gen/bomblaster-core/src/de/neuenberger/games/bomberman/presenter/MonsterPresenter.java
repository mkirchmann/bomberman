package de.neuenberger.games.bomberman.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g3d.model.still.StillModel;

import de.neuenberger.games.bomberman.BombermanGame;
import de.neuenberger.games.bomberman.ki.monster.BaseMonsterMover;
import de.neuenberger.games.bomberman.ki.monster.IMonsterMover;
import de.neuenberger.games.bomberman.ki.monster.MonsterMoverEasy;
import de.neuenberger.games.bomberman.ki.monster.MonsterMoverHard;
import de.neuenberger.games.bomberman.ki.monster.MonsterMoverHarder;
import de.neuenberger.games.bomberman.ki.monster.MonsterMoverMedium;
import de.neuenberger.games.bomberman.model.BombermanModel;
import de.neuenberger.games.bomberman.model.Monster;
import de.neuenberger.games.bomberman.model.Player;

public class MonsterPresenter extends BaseSubPresenter<Monster, Player> {
	private StillModel monsterModel;

	Map<Monster.KiLevel,IMonsterMover> mapMonsterMover = new HashMap<Monster.KiLevel,IMonsterMover>();

	public MonsterPresenter(BombermanModel model) {
		super(Monster.class, model);
	}

	public void create() {
		super.create();
		monsterModel = BombermanGame.loadObjFile("ghost.obj");

		mapMonsterMover.put(Monster.KiLevel.Noob, new BaseMonsterMover(getModel()));
		mapMonsterMover.put(Monster.KiLevel.Easy, new MonsterMoverEasy(getModel()));
		mapMonsterMover.put(Monster.KiLevel.Medium, new MonsterMoverMedium(getModel()));
		mapMonsterMover.put(Monster.KiLevel.Hard, new MonsterMoverHard(getModel()));
		mapMonsterMover.put(Monster.KiLevel.Harder, new MonsterMoverHarder(getModel()));
		mapMonsterMover.put(Monster.KiLevel.Hardest, new MonsterMoverHarder(getModel()));
	}

	@Override
	protected List<Player> getHitTestList() {
		return getModel().getDynamicContent(Player.class);
	}

	public void dispose() {
		super.dispose();
		monsterModel.dispose();
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
		monsterModel.render(getShader());
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
