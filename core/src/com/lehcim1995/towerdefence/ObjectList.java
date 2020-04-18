package com.lehcim1995.towerdefence;

import com.lehcim1995.towerdefence.classes.Enemy;
import com.lehcim1995.towerdefence.classes.Projectile;
import com.lehcim1995.towerdefence.classes.Stats;
import com.lehcim1995.towerdefence.classes.Tower;

import java.util.ArrayList;
import java.util.List;

public class ObjectList
{
    private static ObjectList instance;
    private final List<Enemy> enemies;
    private final List<Tower> towers;
    private final List<Projectile> projectiles;
    private Stats stats;

    private ObjectList()
    {
        enemies = new ArrayList<>();
        towers = new ArrayList<>();
        stats = new Stats();
        projectiles = new ArrayList<>();
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

    public void reset()
    {
        instance = null;
    }
}

