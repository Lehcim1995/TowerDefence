package com.lehcim1995.towerdefence;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.lehcim1995.towerdefence.classes.Enemy;
import com.lehcim1995.towerdefence.classes.Projectile;
import com.lehcim1995.towerdefence.classes.Stats;
import com.lehcim1995.towerdefence.classes.Tower;
import com.lehcim1995.towerdefence.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class ObjectList
{
    private static ObjectList instance;
    private final List<Enemy> enemies;
    private final List<Tower> towers;
    private final List<Projectile> projectiles;
    private Stats stats;
    private SpriteList spriteList;

    private ObjectList()
    {
        enemies = new ArrayList<>();
        towers = new ArrayList<>();
        stats = new Stats();
        projectiles = new ArrayList<>();
        spriteList = new SpriteList();
    }

    public static ObjectList getInstance()
    {
        if (instance == null)
        {
            instance = new ObjectList();
        }

        return instance;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public Stats getStats() {
        return stats;
    }

    public Enemy spawnDefaultEnemy(List<Vector2> path)
    {
        Enemy enemy = new Enemy(path, spriteList.getBaseEnemy());
        getEnemies().add(enemy);
        return enemy;
    }

    public Tower spawnDefaultTower(Vector2 position)
    {
        Tower tower = new Tower(position, spriteList.getBaseTurretBase(), spriteList.getBaseTurretTurret());
        getTowers().add(tower);
        return tower;
    }

    public Projectile spawnProjectile(Vector2 position, float rotation, float range)
    {
        Projectile projectile = new Projectile(position, rotation, range, 10, spriteList.getProjectile());
        getProjectiles().add(projectile);
        return projectile;
    }

    public void dispose()
    {
        spriteList.dispose();
    }

    public void reset()
    {
        spriteList.dispose();
        instance = null;
    }
}

