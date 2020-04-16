package com.lehcim1995.towerdefence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class Enemy
{
    private List<Vector2> path;
    private float pathLength = 0;
    private float pathComplete = 0; // 0 - 100
    private boolean toDelete;
    private Texture texture;

    public Enemy(
            List<Vector2> path,
            Texture texture)
    {
        this.path = path;

        Vector2 previous = new Vector2();
        for (Vector2 step : path)
        {
            pathLength += step.cpy().sub(previous).len();
            previous = step;
        }
        System.out.println(pathLength);

        this.texture = texture;
    }

    public void Draw(Batch batch)
    {
        Vector2 pathVec = positionFromPath(path, pathComplete);
//        System.out.println(pathVec + " " + pathComplete + "%");
        pathComplete += 15f * Gdx.graphics.getDeltaTime();
        if (pathComplete >= 100)
        {
//            pathComplete = 0;
            toDelete = true;
        }
        batch.draw(texture, pathVec.x, pathVec.y);
    }

    private Vector2 positionFromPath(
            List<Vector2> path,
            float complete)
    {

        Vector2 result = new Vector2(0,0);

        float completeLength = (pathLength / 100) * complete;
        Vector2 previous = new Vector2();

        for (Vector2 step : path)
        {
            Vector2 steplen = step.cpy().sub(previous);
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

    public Vector2 getPosition()
    {
        return positionFromPath(path, pathComplete);
    }

    public boolean isToDelete() {
        return toDelete;
    }
}
