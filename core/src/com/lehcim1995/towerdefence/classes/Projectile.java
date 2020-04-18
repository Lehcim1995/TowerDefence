package com.lehcim1995.towerdefence.classes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Projectile
{
    private Vector2 position;
    private float direction;
    private float speed;
    private Sprite spite;

    public Projectile(
            Vector2 position,
            float direction,
            float speed,
            Sprite spite)
    {
        this.position = position.cpy();
        this.direction = direction;
        this.speed = speed;
        this.spite = spite;

//        spite.setRotation(direction);
    }

    public void Draw(Batch batch)
    {
        // unit vector up -> set dor; scale speed.
        Vector2 up = Vector2.Y.cpy();
        up.setAngle(direction);
        up.rotate90(1);
        up.scl(speed);
        position.add(up.cpy());

        spite.setPosition(position.x, position.y);
        spite.draw(batch);
    }
}
