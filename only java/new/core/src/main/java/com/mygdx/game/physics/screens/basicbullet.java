package com.mygdx.game.physics.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;

import java.awt.*;

public class basicbullet implements Screen {
    SpriteBatch batch;
    Font logs;



    ModelBatch modelBatch;
    ModelInstance modelInstance;

    PerspectiveCamera Camera;
    CameraInputController cameraInputController;
    FirstPersonCameraController firstPersonCameraController;

    public basicbullet(){
        batch = new SpriteBatch();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
