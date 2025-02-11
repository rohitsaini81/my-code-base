package com.mygdx.game.betkiing.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.betkiing.Main;

import static com.mygdx.game.betkiing.Main.*;

public class Menu implements Screen {
    private Texture image;
    private BitmapFont myFont;
    private Stage stage;
    private Main Game;
    public Menu(Main main){
        Game = main;
        image = new Texture("libgdx.png");
        stage = new Stage(viewport);
        myFont = new BitmapFont(Gdx.files.internal("bitmapfont/Amble-Regular-26.fnt"));


        Label.LabelStyle label1Style = new Label.LabelStyle();
        label1Style.font = myFont;
        label1Style.fontColor = Color.RED;
        float y=400;
        Label label1 = new Label("Play Free Game only on BetKing", label1Style);
//        label1.setSize(Width, Height / 12);
        label1.setPosition((Width - label1.getWidth()) / 2, y);
//        label1.setAlignment(Align.center);
        stage.addActor(label1);

        Texture texture = new Texture("menu/play.png");
        Image image1 = new Image(texture);
        image1.setPosition((Width - label1.getWidth()) / 2, y-70);
        image1.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.enabled);




        image1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Play clicked!");
                Game.changeScreen(1);
            }
        });
        stage.addActor(image1);


        texture = new Texture("menu/games.png");
        Image image2 = new Image(texture);
        image2.setPosition((Width - label1.getWidth()) / 2, y-120);
        stage.addActor(image2);

        texture = new Texture("menu/settings.png");
        Image image3 = new Image(texture);
        image3.setPosition((Width - label1.getWidth()) / 2, y-195);
        stage.addActor(image3);

        texture = new Texture("menu/sign-in.png");
        Image image4 = new Image(texture);
        image4.setPosition((Width - label1.getWidth()) / 2, y-255);
        stage.addActor(image4);

        texture = new Texture("menu/about-us.png");
        Image image5 = new Image(texture);
        image5.setPosition((Width - label1.getWidth()) / 2, y-295);
        stage.addActor(image5);

        Gdx.input.setInputProcessor(stage);





    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen before rendering

//        Batch.begin();
//        Batch.draw(image, 0, 0);
//        Batch.end();

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    @Override
    public void dispose() {
        image.dispose();
        stage.dispose();
        myFont.dispose();
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

}
