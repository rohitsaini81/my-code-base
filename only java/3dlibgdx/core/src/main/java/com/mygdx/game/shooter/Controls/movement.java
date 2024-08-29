package com.mygdx.game.shooter.Controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import static com.mygdx.game.shooter.Main.player;

public class movement {
    private static final float SPEED = 1f;
    movement(){

    }
    public static void render(float delta){
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)){Gdx.app.exit();}
        if (Gdx.input.isKeyPressed(Input.Keys.A)){player.setX(-delta*SPEED);}
        if (Gdx.input.isKeyPressed(Input.Keys.S)){player.setZ(-delta*SPEED);}
        if (Gdx.input.isKeyPressed(Input.Keys.D)){player.setX(delta*SPEED);}
        if (Gdx.input.isKeyPressed(Input.Keys.W)){player.setZ(delta*SPEED);}
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){}


        if (Gdx.input.isKeyPressed(Input.Keys.UP)){}
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){}
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){}
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){}


    }
}
