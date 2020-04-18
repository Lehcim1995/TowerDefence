package com.lehcim1995.towerdefence;

import com.badlogic.gdx.Game;
import com.lehcim1995.towerdefence.screens.GameScreen;

public class TowerDefenceMain extends Game
{
	
	@Override
	public void create () {
        this.setScreen(new GameScreen(this));
	}
	
	@Override
	public void dispose () {
	}
}
