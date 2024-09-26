package com.mygdx.game.games2d;

import com.badlogic.gdx.Game;
import com.mygdx.game.games2d.screens.game3d;
import com.mygdx.game.games2d.screens.snake;
import com.mygdx.game.games2d.screens.tenis;

public class Main extends Game {
    @Override
    public void create() {
        setScreen(new tenis());
    }
}
