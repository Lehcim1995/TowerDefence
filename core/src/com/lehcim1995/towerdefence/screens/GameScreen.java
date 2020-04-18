package com.lehcim1995.towerdefence.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.lehcim1995.towerdefence.ObjectList;
import com.lehcim1995.towerdefence.TowerDefenceMain;
import com.lehcim1995.towerdefence.classes.Enemy;
import com.lehcim1995.towerdefence.classes.Tower;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameScreen extends AbstractScreen
{
    private SpriteBatch batch;
    private SpriteBatch textBatch;
    private ShapeRenderer shapeRenderer;
    private Texture img;
    private TiledMapTileSet tileset;
    private List<Vector2> path = new ArrayList<>();
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Camera camera;
    private float timer = 0;
    private float spawnrate = 1f / 1f; // 1 per seconden
    public static String pathText = "TowerDefence/PNG/Default size/";

    public GameScreen(TowerDefenceMain main) {
        super(main);
    }

    @Override
    public void show() {
        super.show();

        batch = new SpriteBatch();
        textBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        img = new Texture(pathText+"towerDefense_tile271.png");
        camera = new OrthographicCamera();
        ((OrthographicCamera) camera).setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ((OrthographicCamera) camera).zoom = 5f;
//        camera.position.set(new Vector2(1536, 1536), 0);
        camera.position.set(new Vector2(1024, 1024), 0);
        camera.update();

        tileset = new TiledMapTileSet();
        map = new TmxMapLoader().load("Map_test.tmx");
        float unitScale = 1f;
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        MapLayer pathLayer = map.getLayers().get("PathLayer");
        MapObjects objs = pathLayer.getObjects();
        PolylineMapObject paths = (PolylineMapObject)objs.get("Path");
        float[] vertices = paths.getPolyline()
                                .getTransformedVertices();
        for (int i = 0; i < vertices.length; i+=2)
        {
            Vector2 v = new Vector2(vertices[i], vertices[i+1]);
            path.add(v);
        }

        spawnEnemy();

        spawnTower(new Vector2(1024, 1024));
        spawnTower(new Vector2(1024, 1100));
        spawnTower(new Vector2(1024, 1200));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
            spawnEnemy();
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT))
        {
            Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            Vector3 worldSpace = camera.unproject(new Vector3(mousePos, 0));

            spawnTower(new Vector2(worldSpace.x, worldSpace.y));
        }

        timer += Gdx.graphics.getDeltaTime();

        if (timer >= spawnrate)
        {
            timer -= spawnrate;
            spawnEnemy();
        }

        updateCamera();

        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        renderer.setView((OrthographicCamera) camera);
        renderer.render();

        ObjectList.getInstance().getEnemies().removeAll(ObjectList.getInstance().getEnemies().stream().filter(Enemy::isToDelete).collect(Collectors.toList()));

        batch.begin();
        ObjectList.getInstance().getEnemies().forEach(e -> e.Draw(batch));
        ObjectList.getInstance().getTowers().forEach(t -> {
            t.Draw(batch);
        });
        ObjectList.getInstance().getProjectiles().forEach(p -> p.Draw(batch));
        batch.end();

        // For shape like lines.
        shapeRenderer.begin();
        ObjectList.getInstance().getTowers().forEach(t -> {
            t.Draw(shapeRenderer);
        });
        shapeRenderer.end();
    }

    private void spawnEnemy()
    {
        Enemy enemy;
        enemy = new Enemy(path, new Sprite(img));
        ObjectList.getInstance().getEnemies().add(enemy);
    }

    private void spawnTower(Vector2 position)
    {
        final Texture base = new Texture(pathText+"towerDefense_tile181.png");
        final Texture turret = new Texture(pathText+"towerDefense_tile203.png");
        Tower tower = new Tower(position, base, turret);
        ObjectList.getInstance().getTowers().add(tower);
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
