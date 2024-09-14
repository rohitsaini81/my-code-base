package com.mygdx.game.ludo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.ludo.controls.controller;

import static com.mygdx.game.ludo.controls.controller.update;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {
    SpriteBatch batch;
    Sprite board;
    shapes shapes;
    public FirstScreen(){
        batch = new SpriteBatch();
        board =  new Sprite(new Texture("board.jpg"));
        shapes = new shapes();
    }
    @Override
    public void show() {
        // Prepare your screen here.
    }
    public static float centerx = (Gdx.graphics.getWidth()/2f)-150;
    public static float centery = (Gdx.graphics.getHeight()/2f)-150;

    @Override
    public void render(float delta) {
        controller.update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.begin();
        batch.draw(board,centerx,centery,300,300);
        batch.end();

        shapes.update();

    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        batch.dispose();
        board.getTexture().dispose();
        shapes.shapeRenderer.dispose();
    }
}
