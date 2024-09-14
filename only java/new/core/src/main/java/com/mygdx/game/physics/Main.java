package com.mygdx.game.physics;

import com.badlogic.gdx.Game;
import com.mygdx.game.physics.screens.basicbullet;

public class Main extends Game {
    @Override
    public void create() {
        setScreen(new basicbullet());
    }
}
