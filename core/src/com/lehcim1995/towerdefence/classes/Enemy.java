package com.lehcim1995.towerdefence.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class Enemy
{
    private List<Vector2> path;
    private float pathLength = 0;
    private float pathComplete = 0; // 0 - 100
    private float speed = 10f;
    private boolean toDelete;
    private Sprite spite;

    public Enemy(
            List<Vector2> path,
            Sprite spite)
    {
        this.path = path;

        Vector2 previous = new Vector2();
        for (Vector2 step : path)
        {
            pathLength += step.cpy()
                              .sub(previous)
                              .len();
            previous = step;
        }

        this.spite = spite;
        spite.setOrigin(spite.getWidth() / 2, spite.getHeight() / 2);
    }

    public void Draw(Batch batch)
    {
        Vector2 pathVec = positionFromPathPercent(path, pathComplete);
        pathComplete += speed * Gdx.graphics.getDeltaTime(); // in percentages
        if (pathComplete >= 100)
        {
            toDelete = true;
        }

        spite.rotate(33);
        spite.setPosition(pathVec.x, pathVec.y);
        spite.draw(batch);
    }

    public void dispose() {
    }

    public Vector2 getPosition()
    {
        return positionFromPathPercent(path, pathComplete);
    }

    public boolean isToDelete() {
        return toDelete;
    }

    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    private Vector2 positionFromPathPercent(
            List<Vector2> path,
            float complete)
    {
        Vector2 result = new Vector2(0, 0);

        float completeLength = (pathLength / 100) * complete;
        Vector2 previous = new Vector2();

        for (Vector2 step : path)
        {
            Vector2 steplen = step.cpy()
                                  .sub(previous);
            if (steplen.len() < completeLength)
            {
                completeLength -= steplen.len();
                previous = step.cpy();
            }
            else
            {
                Vector2 ret = step.cpy();
                ret.sub(previous);
                ret.setLength(completeLength);
                ret.add(previous);

                return ret;
            }
        }

        return result;
    }
}
