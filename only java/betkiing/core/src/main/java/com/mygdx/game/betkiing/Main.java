package com.mygdx.game.betkiing;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.betkiing.Screens.Menu;
import com.mygdx.game.betkiing.Screens.games.Stake;

public class Main extends Game {
    Menu MenuScreen;
    Stake stakeScreen;
    public static Viewport viewport;
    public static float Height;
    public static float Width;
    public static SpriteBatch Batch;

    @Override
    public void create() {
        Height=Gdx.graphics.getHeight();
        Width=Gdx.graphics.getWidth();
        viewport = new FitViewport(Width,Height);

        MenuScreen = new Menu(this);
        stakeScreen = new Stake();

        Batch = new SpriteBatch();
        this.screen = MenuScreen;

    }

    @Override
    public void render() {
//        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
//        Gdx.gl.glClearColor(1, 1, 1, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.screen.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        Batch.dispose();

    }


    public void changeScreen(int i){
        if (i==1){
            this.screen=stakeScreen;
            MenuScreen.dispose();
        }
    }
}
