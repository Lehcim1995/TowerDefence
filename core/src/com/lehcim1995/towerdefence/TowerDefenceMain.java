package com.lehcim1995.towerdefence;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class TowerDefenceMain extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	TiledMapTileSet tileset;
	Enemy enemy;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("TowerDefence/PNG/Default size/towerDefense_tile271.png");
		tileset = new TiledMapTileSet();
		List<Vector2> path = new ArrayList<>();
		path.add(new Vector2(100, 0));
		path.add(new Vector2(100, 100));
		path.add(new Vector2(0, 100));
		path.add(new Vector2(0, 0));
		enemy = new Enemy(path, img);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		enemy.Draw(batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
