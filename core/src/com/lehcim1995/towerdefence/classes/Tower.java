package com.lehcim1995.towerdefence.classes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.lehcim1995.towerdefence.ObjectList;
import com.lehcim1995.towerdefence.screens.GameScreen;

public class Tower
{
    private Vector2 position;
    private float rotation;
    private float range;
    private float shootSpeed; // per second
    private Sprite base;
    private Sprite turret;
    private Enemy enemy;

    public Tower(
            Vector2 position,
            Sprite base,
            Sprite turret)
    {
        this.position = position;
        this.base = base;
        this.turret = turret;
        this.range = 600;
        this.shootSpeed = 1;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void addRotation(float rotation)
    {
        this.rotation += rotation;
    }

    public void Draw(Batch batch)
    {
        //batch.draw(base, position.x, position.y);
        enemy = findClosest();
        if (enemy != null)
        {
            rotation = enemy.getPosition().cpy().sub(this.position).angle() - 90;
            enemy.setToDelete(true);
            // if shooting??
            ObjectList.getInstance().spawnProjectile(this.position, rotation, this.range);
        }

        base.setPosition(position.x, position.y);
        base.draw(batch);

        turret.setPosition(position.x, position.y);
        turret.setOrigin(turret.getWidth()/2, turret.getHeight()/2);
        turret.setRotation(rotation);
        turret.draw(batch);
    }

    public void Draw(ShapeRenderer shapeRenderer) {

        shapeRenderer.circle(position.x, position.y, range);

        if (enemy != null)
        {
            shapeRenderer.line(this.position, enemy.getPosition());
        }
    }

    private Enemy findClosest()
    {
        final Enemy[] closest = {null};

        ObjectList.getInstance().getEnemies().forEach(o -> {
            float l = this.position.cpy().sub(o.getPosition()).len();

            if (l < range)
            {
                if (closest[0] == null)
                {
                    closest[0] = o;
                }
                else
                {
                    // pos - o.pos -> len
                    float l2 = this.position.cpy()
                                            .sub(closest[0].getPosition())
                                            .len();

                    if (l < l2)
                    {
                        closest[0] = o;
                    }
                }
            }
        });

        return closest[0];
    }
}
