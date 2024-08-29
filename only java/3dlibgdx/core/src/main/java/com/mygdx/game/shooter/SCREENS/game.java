package com.mygdx.game.shooter.SCREENS;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;
import com.mygdx.game.shooter.Main;


public class game implements Screen {
    public static SpriteBatch batch;
    public static Sprite sprite;


    public static OrthographicCamera camera;
    Viewport Viewport;

//objects : ----->
//    cameracontroller cameracontroller;

    @Override
    public void show() {
        batch = new SpriteBatch();
        sprite = new Sprite(new Texture("badlogic.jpg"));
        sprite.setSize(480,720);
        sprite.setX(0);
        sprite.setY(0);

        camera = new OrthographicCamera();
        Viewport = new FitViewport(480,720,camera);
        Viewport.apply();
        camera.position.set((float) Main.WIDTH / 2, (float) Main.HEIGHT / 2, 0);


//        objects
//        cameracontroller = new cameracontroller();
    }

    @Override
    public void render(float delta) {
//        cameracontroller.camRenderer();
        camera.update();

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        sprite.draw(batch);
//        sprite.rotate(2f);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        Viewport.update(width,height);
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
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
        batch.dispose();

    }
}
