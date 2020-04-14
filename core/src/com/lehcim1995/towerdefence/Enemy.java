package com.lehcim1995.towerdefence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class Enemy
{
    private List<Vector2> path;
    private float pathComplete = 0; // 0 - 100
    private Texture texture;

    public Enemy(
            List<Vector2> path,
            Texture texture)
    {
        this.path = path;
        this.texture = texture;
    }

    public void Draw(Batch batch)
    {
        Vector2 pathVec = positionFromPath(path, pathComplete);
        System.out.println(pathVec + " " + pathComplete + "%");
        pathComplete += 5f * Gdx.graphics.getDeltaTime();
        if (pathComplete >= 100)
        {
            pathComplete = 0;
        }
        batch.draw(texture, pathVec.x, pathVec.y);
    }

    private Vector2 positionFromPath(
            List<Vector2> path,
            float complete)
    {
        float length = 0;

        for (Vector2 step : path)
        {
            length += step.len();
        }

        Vector2 result = new Vector2(0,0);

        float completeLength = (length / 100) * complete;

        for (Vector2 step : path)
        {
            if (completeLength > step.len())
            {
                completeLength -= step.len();
                //result = result.add(step);
            }
            else
            {
                Vector2 endResult = step.cpy();
                endResult.setLength(completeLength);
                result = result.add(endResult);
                break;
            }
        }

        return result;
    }
}
