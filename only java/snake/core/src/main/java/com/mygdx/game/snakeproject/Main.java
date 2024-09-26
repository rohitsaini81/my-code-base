package com.mygdx.game.snakeproject;

import com.badlogic.gdx.Game;
import com.mygdx.game.snakeproject.screens.game3d;
import com.mygdx.game.snakeproject.screens.snake;
import com.mygdx.game.snakeproject.screens.tenis;

public class Main extends Game {
    @Override
    public void create() {
        setScreen(new game3d());
    }
}
