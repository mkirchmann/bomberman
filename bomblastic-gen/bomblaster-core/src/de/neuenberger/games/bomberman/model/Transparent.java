package de.neuenberger.games.bomberman.model;

import de.neuenberger.games.core.model.NCellContent;

public class Transparent implements NCellContent {
	private static final Transparent instance=new Transparent();
	private Transparent() {
		
	}
	public static NCellContent getInstance() {
		return instance;
	}

}
