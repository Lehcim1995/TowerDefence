package com.lehcim1995.towerdefence;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Tower
{
    private Vector2 position;
    private float rotation;
    private Texture base;
    private Texture turret;
    private Enemy enemy;

    public Tower(
            Vector2 position,
            Texture base,
            Texture turret)
    {
        this.position = position;
        this.base = base;
        this.turret = turret;
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
        if (enemy == null)
        {
            this.rotation = 0;
        }
        else
        {
            rotation = enemy.getPosition().cpy().sub(this.position).angle() - 90;
        }

        batch.draw(turret,
                position.x, position.y,
                turret.getWidth()/2f, turret.getHeight()/2f,
                turret.getWidth(),turret.getHeight(),
                1,1,rotation,
                0,0,
                turret.getWidth(),turret.getHeight(),
                false, false);
    }

    public void Draw(ShapeRenderer shapeRenderer) {
        if (enemy != null)
        {
            shapeRenderer.line(this.position, enemy.getPosition());
        }
    }

    private Enemy findClosest()
    {
        final Enemy[] closest = {null};

        ObjectList.getInstance().getEnemies().forEach(o -> {
                if (closest[0] == null)
                {
                    closest[0] = o;
                }else {
                    // pos - o.pos -> len
                    float l = this.position.cpy().sub(o.getPosition()).len();
                    float l2 = this.position.cpy().sub(closest[0].getPosition()).len();

                    if (l < l2)
                    {
                        closest[0] = o;
                    }
                }
        });

        return closest[0];
    }
}
