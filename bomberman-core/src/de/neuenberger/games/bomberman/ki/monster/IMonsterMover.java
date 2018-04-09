package de.neuenberger.games.bomberman.ki.monster;

import de.neuenberger.games.bomberman.model.Monster;

public interface IMonsterMover {

	boolean move(float delta, Monster m);

}
