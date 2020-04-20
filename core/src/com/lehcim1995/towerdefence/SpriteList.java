package com.lehcim1995.towerdefence;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteList
{
    public static String pathText = "TowerDefence/PNG/Default size/";

    private final Texture enemy = new Texture(pathText+"towerDefense_tile271.png");
    private final Texture base = new Texture(pathText+"towerDefense_tile181.png");
    private final Texture turret = new Texture(pathText+"towerDefense_tile203.png");
    private final Texture projectile = new Texture(pathText + "towerDefense_tile275.png");

    public Sprite getBaseEnemy()
    {
        return new Sprite(enemy);
    }

    public Sprite getBaseTurretBase()
    {
        return new Sprite(base);
    }

    public Sprite getBaseTurretTurret()
    {
        return new Sprite(turret);
    }

    public Sprite getProjectile()
    {
        return new Sprite(projectile);
    }

    public void dispose()
    {
        enemy.dispose();
        base.dispose();
        turret.dispose();
        projectile.dispose();
    }
}
