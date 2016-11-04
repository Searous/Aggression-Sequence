package com.game.stage;

import java.util.ArrayList;

import com.game.action.ActionAI;
import com.game.enums.ID;

public class StageTest extends Stage {

	public StageTest() {
		this.waves = new ArrayList<>();
		this.name = "Test Stage";
		this.level = 1;
		this.waves.add(new Wave(0,new Enemy[]{
				new Enemy(60, ID.Fighter, ActionAI.class, -20, -20, 0, 0), 
				new Enemy(60, ID.Fighter, ActionAI.class, -40, -40, 0, 0),
				new Enemy(60, ID.Fighter, ActionAI.class, -60, -60, 0, 0),
				new Enemy(60, ID.Fighter, ActionAI.class, -80, -80, 0, 0),
				new Enemy(60, ID.Fighter, ActionAI.class, -100, -100, 0, 0)}));
		this.waves.add(new Wave(0,new Enemy[]{new Enemy(60, ID.Fighter, ActionAI.class, -20, -20, 0, 0)}));
	}

}
