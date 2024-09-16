package com.mygdx.game.physics;

import com.badlogic.gdx.Game;
import com.mygdx.game.physics.screens.basicbullet;
import com.mygdx.game.physics.screens.btTerrain;
import com.mygdx.game.physics.screens.terrainCollision;

public class Main extends Game {
    @Override
    public void create() {
        setScreen(new btTerrain());
    }
}
