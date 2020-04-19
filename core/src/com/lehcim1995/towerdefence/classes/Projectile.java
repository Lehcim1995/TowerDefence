package com.lehcim1995.towerdefence.classes;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Projectile
{
    private Vector2 position;
    private Vector2 origin;
    private float direction;
    private float speed;
    private Sprite sprite;
    private float distance;
    private boolean toDelete;

    public Projectile(
            Vector2 position,
            float direction,
            float distance,
            float speed,
            Sprite sprite)
    {
        this.position = position.cpy();
        this.origin = position.cpy();
        this.direction = direction;
        this.distance = distance;
        this.speed = speed;
        this.sprite = sprite;
    }

    public void Draw(Batch batch)
    {
        // unit vector up -> set dor; scale speed.
        Vector2 up = Vector2.Y.cpy();
        up.setAngle(direction);
        up.rotate90(1);
        up.scl(speed);
        position.add(up.cpy());

        toDelete = position.cpy().sub(origin).len() > distance;

        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);

    }

    public boolean isToDelete() {
        return toDelete;
    }
}
