package com.lehcim1995.towerdefence;

import java.util.ArrayList;
import java.util.List;

public class ObjectList
{
    private ObjectList(){}
    private static ObjectList instance;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Tower> towers = new ArrayList<>();

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

    public void reset()
    {

    }
}

