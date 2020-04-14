package com.lehcim1995.towerdefence;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

import java.util.ArrayList;
import java.util.List;

public class TowerDefenceMain extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	TiledMapTileSet tileset;
	Enemy enemy;
	List<Enemy> enemies = new ArrayList<>();
    List<Vector2> path = new ArrayList<>();
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    Camera camera;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("TowerDefence/PNG/Default size/towerDefense_tile271.png");
		camera = new OrthographicCamera();
        ((OrthographicCamera) camera).setToOrtho(false, 600, 600);
		camera.update();
		tileset = new TiledMapTileSet();
		path.add(new Vector2(100, 0));
		path.add(new Vector2(100, 100));
		path.add(new Vector2(0, 100));
		path.add(new Vector2(0, 0));
		enemy = new Enemy(path, img);
        enemies.add(enemy);

        map = new TmxMapLoader().load("Map_test.tmx");
        float unitScale = 1f;
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
            Enemy enemy;
            enemy = new Enemy(path, img);
            enemies.add(enemy);
        }
		updateCamera();



        batch.setProjectionMatrix(camera.combined);

        renderer.setView((OrthographicCamera) camera);
        renderer.render();

		batch.begin();
		enemies.forEach(e -> e.Draw(batch));
		batch.end();


	}

	private void updateCamera()
    {


        camera.update();
    }
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
