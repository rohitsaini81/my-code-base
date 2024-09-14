package com.mygdx.game.ludo;

import com.badlogic.gdx.Game;
import com.mygdx.game.ludo.screens.FirstScreen;

public class Main extends Game {
    @Override
    public void create() {
        setScreen(new FirstScreen());
    }
}
