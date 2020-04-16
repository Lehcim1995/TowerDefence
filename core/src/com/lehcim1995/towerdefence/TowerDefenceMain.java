package com.lehcim1995.towerdefence;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TowerDefenceMain extends ApplicationAdapter {
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	Texture img;
	TiledMapTileSet tileset;
	Enemy enemy;
    List<Vector2> path = new ArrayList<>();
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    Camera camera;
    float timer = 0;
    float spawnrate = 1f/1f; // 1 per seconden
    String pathText = "TowerDefence/PNG/Default size/";
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
		img = new Texture(pathText+"towerDefense_tile271.png");
		camera = new OrthographicCamera();
        ((OrthographicCamera) camera).setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ((OrthographicCamera) camera).zoom = 5f;
        camera.position.set(new Vector2(1024, 1024), 0);
		camera.update();

		tileset = new TiledMapTileSet();
        map = new TmxMapLoader().load("Map_test.tmx");
        MapLayer layer = map.getLayers().get("Object Layer 1");
        MapObjects objects = layer.getObjects();
        objects.forEach(obj -> {
            Vector2 v = new Vector2(((RectangleMapObject)obj).getRectangle().x, ((RectangleMapObject)obj).getRectangle().y);
            path.add(v);
        });

		enemy = new Enemy(path, img);
        ObjectList.getInstance().getEnemies().add(enemy);
//towerDefense_tile203.png
        Tower tower = new Tower(new Vector2(1024, 1024), new Texture(pathText+"towerDefense_tile203.png"), new Texture(pathText+"towerDefense_tile203.png"));
        ObjectList.getInstance().getTowers().add(tower);
        tower = new Tower(new Vector2(1024, 1100), new Texture(pathText+"towerDefense_tile203.png"), new Texture(pathText+"towerDefense_tile203.png"));
        ObjectList.getInstance().getTowers().add(tower);
        tower = new Tower(new Vector2(1024, 1200), new Texture(pathText+"towerDefense_tile203.png"), new Texture(pathText+"towerDefense_tile203.png"));
        ObjectList.getInstance().getTowers().add(tower);

        float unitScale = 1f;
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
           SpawnEnemy();
        }

        timer += Gdx.graphics.getDeltaTime();

		if (timer >= spawnrate)
        {
            timer -= spawnrate;
            SpawnEnemy();
        }

		updateCamera();

        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        renderer.setView((OrthographicCamera) camera);
        renderer.render();

        ObjectList.getInstance().getEnemies().removeAll(ObjectList.getInstance().getEnemies().stream().filter(e -> e.isToDelete()).collect(Collectors.toList()));

		batch.begin();
        ObjectList.getInstance().getEnemies().forEach(e -> e.Draw(batch));
        ObjectList.getInstance().getTowers().forEach(t -> {
		    //t.addRotation(10* Gdx.graphics.getDeltaTime());
            t.Draw(batch);
		});
		batch.end();

		shapeRenderer.begin();
        ObjectList.getInstance().getTowers().forEach(t -> {
            t.Draw(shapeRenderer);
        });
		shapeRenderer.end();


	}

	private void SpawnEnemy()
    {
        Enemy enemy;
        enemy = new Enemy(path, img);
        ObjectList.getInstance().getEnemies().add(enemy);
    }

	private void updateCamera()
    {
        //camera.position.set(enemies.get(0).getPosition(), 0);

        camera.update();
    }
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
