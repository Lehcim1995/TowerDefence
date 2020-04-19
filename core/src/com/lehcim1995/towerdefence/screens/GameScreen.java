package com.lehcim1995.towerdefence.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.lehcim1995.towerdefence.ObjectList;
import com.lehcim1995.towerdefence.TowerDefenceMain;
import com.lehcim1995.towerdefence.classes.Enemy;
import com.lehcim1995.towerdefence.classes.Projectile;
import com.lehcim1995.towerdefence.classes.Tower;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameScreen extends AbstractScreen implements InputProcessor
{
    private final InputMultiplexer inputMultiplexer;
    private SpriteBatch batch;
    private SpriteBatch textBatch;
    private ShapeRenderer shapeRenderer;
    private Texture img;
    private Texture base;
    private Texture turret;
    private TiledMapTileSet tileset;
    private List<Vector2> path;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Camera camera;
    private float timer = 0;
    private final float spawnRate = 1f / 1f; // 1 per seconden

    private int mapPixelWidth;
    private int mapPixelHeight;

    public static String pathText = "TowerDefence/PNG/Default size/";

    //UI elements
    private VisLabel fps;

    public GameScreen(TowerDefenceMain main) {
        super(main);

        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);
    }

    @Override
    public void show() {
        super.show();

        batch = new SpriteBatch();
        textBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        img = new Texture(pathText+"towerDefense_tile271.png");
        base = new Texture(pathText+"towerDefense_tile181.png");
        turret = new Texture(pathText+"towerDefense_tile203.png");

        camera = new OrthographicCamera();
        ((OrthographicCamera) camera).setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ((OrthographicCamera) camera).zoom = 1f;

        tileset = new TiledMapTileSet();
        map = new TmxMapLoader().load("Map_test.tmx");
        float unitScale = 1f;
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        MapProperties prop = map.getProperties();

        int mapWidth = prop.get("width", Integer.class);
        int mapHeight = prop.get("height", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        int tilePixelHeight = prop.get("tileheight", Integer.class);

        mapPixelWidth = mapWidth * tilePixelWidth;
        mapPixelHeight = mapHeight * tilePixelHeight;

        camera.position.set(new Vector2(mapPixelWidth/2f, mapPixelHeight/2f), 0);
        ((OrthographicCamera) camera).zoom = mapPixelHeight / Gdx.graphics.getHeight();
        camera.update();

        MapLayer pathLayer = map.getLayers().get("PathLayer");
        MapObjects objs = pathLayer.getObjects();
        PolylineMapObject paths = (PolylineMapObject)objs.get("Path");
        path = new ArrayList<>();
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

        VisTextButton button = new VisTextButton("Click");
        button.addListener(new ChangeListener()
        {
            @Override
            public void changed(
                    ChangeEvent event,
                    Actor actor)
            {

            }
        });
        button.setPosition(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f);

        fps = new VisLabel("Fps:");

        stage.addActor(button);
        stage.addActor(fps);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int fpsCount = (int) (1 / delta);
        fps.setText("Fps: " + fpsCount);

        timer += Gdx.graphics.getDeltaTime();

        if (timer >= spawnRate)
        {
            timer -= spawnRate;
            spawnEnemy();
        }

        updateCamera();

        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        renderer.setView((OrthographicCamera) camera);
        renderer.render();

        ObjectList.getInstance().getEnemies().removeAll(ObjectList.getInstance().getEnemies().stream().filter(Enemy::isToDelete).collect(Collectors.toList()));
        ObjectList.getInstance().getProjectiles().removeAll(ObjectList.getInstance().getProjectiles().stream().filter(Projectile::isToDelete).collect(Collectors.toList()));

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
        shapeRenderer.line(new Vector2(), new Vector2(0, 100000));
        shapeRenderer.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(
            int width,
            int height)
    {
        super.resize(width, height);


        float zoom = mapPixelHeight/height;


        camera.position.set(new Vector2((width/2f)*zoom, mapPixelHeight/2f), 0);
//        camera.position.set(new Vector2((mapPixelWidth * zoom)/2f, mapPixelHeight/2f), 0);
        ((OrthographicCamera) camera).viewportHeight = height;
        ((OrthographicCamera) camera).viewportWidth = width;
        ((OrthographicCamera) camera).zoom = zoom;
//        ((OrthographicCamera) camera).zoom = mapPixelWidth/Gdx.graphics.getWidth();
        camera.update();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(
            int screenX,
            int screenY,
            int pointer,
            int button)
    {
        if (button == Input.Buttons.LEFT)
        {
            Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            Vector3 worldSpace = camera.unproject(new Vector3(mousePos, 0));

            spawnTower(new Vector2(worldSpace.x, worldSpace.y));
        }

        return false;
    }

    @Override
    public boolean touchUp(
            int screenX,
            int screenY,
            int pointer,
            int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged(
            int screenX,
            int screenY,
            int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(
            int screenX,
            int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private void spawnEnemy()
    {
        Enemy enemy;
        enemy = new Enemy(path, new Sprite(img));
        ObjectList.getInstance().getEnemies().add(enemy);
    }

    private void spawnTower(Vector2 position)
    {

        Tower tower = new Tower(position, new Sprite(base), new Sprite(turret));
        ObjectList.getInstance().getTowers().add(tower);
    }

    private void updateCamera()
    {
        //camera.position.set(enemies.get(0).getPosition(), 0);

        camera.update();
    }

    @Override
    public void dispose () {
        super.dispose();
        shapeRenderer.dispose();
        batch.dispose();
        img.dispose();
        base.dispose();
        turret.dispose();
    }
}
