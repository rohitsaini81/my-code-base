package com.mygdx.game.ludo.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;


public class controller {
    controller(){}

    public static void update(float delta){
//        if (Gdx.input.isKeyPressed(Input.Keys.W)){}
        if (Gdx.input.isKeyPressed(Input.Keys.Q)){Gdx.app.exit();}
    }
}
