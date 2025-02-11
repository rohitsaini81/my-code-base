package com.mygdx.game.betkiing.Screens.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mygdx.game.betkiing.Main.Batch;
import static com.mygdx.game.betkiing.Main.viewport;

public class Stake implements Screen {
    ArrayList<Vector4> matrix;
    HashMap<Integer, Rectangle> boxeRect;
    Texture texture;
    Sprite boximage;
    Sprite bombimage;
    Sprite emptybox;
    Stage stage;
    public Stake(){
        stage = new Stage(viewport);
        matrix = new ArrayList<>(15);
        texture = new Texture("box1.png");
        boximage = new Sprite(texture);
        boximage.setSize(80,80);


        texture = new Texture("bomb.jpg");
        bombimage = new Sprite(texture);
        bombimage.setSize(80,80);

        texture = new Texture("box2.png");
        emptybox = new Sprite(texture);
        emptybox.setSize(80,80);
//        boxeRect = new ArrayList<>();
        boxeRect = new HashMap<>();
        int T=0;
        for (int i = 125; i < 525; i+=100) {
            for (int j = 50; j < 450; j+=100) {
//                Batch.draw(boximage, i, j,80,80);
                Rectangle box = new Rectangle(i,j,80,80);
                    boxeRect.put(T,box);
                    matrix.add(new Vector4(T,1,0,1));
                    T++;


            }
        }
        randomNumber= (int) Math.floor(Math.random()*15);
        matrix.get(randomNumber).z=-1;
        randomNumber= (int) Math.floor(Math.random()*15);
        matrix.get(randomNumber).z=-1;
        randomNumber= (int) Math.floor(Math.random()*15);
        matrix.get(randomNumber).z=-1;
        randomNumber= (int) Math.floor(Math.random()*15);
        matrix.get(randomNumber).z=-1;


    }


    @Override
    public void show() {

    }
    float x=10,y=10;
    float posX=0,posY=0;
    int T=0;
    float cursorX,cursorY;
    int randomNumber= (int) Math.floor(Math.random()*15);
    boolean GameOver=false;

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen before rendering
        ScreenUtils.clear(0f, 0f, 0.2f, 1f);

        Batch.begin();


            for (int i = 0; i < boxeRect.size(); i++) {

                Rectangle rectangle = boxeRect.get(i);            // Do something with rect
                float X = rectangle.getX();
                float Y = rectangle.getY();
                if (rectangle.contains(cursorX, cursorY)) {
                    System.out.println(matrix.get(i));

                    if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !GameOver) {
                        boxeRect.get(randomNumber).setSize(79, 79);
                        matrix.get(i).y = randomNumber == i ? -1 : 0;
                        GameOver = randomNumber == i || matrix.get(i).z==-1;

                    }
                    if (GameOver){
                        matrix.forEach(mat -> {
                            mat.y=mat.z;
                        });
                    }



                }
                if (matrix.get(i).y == (float) 1) {
                    boximage.setPosition(rectangle.x, rectangle.y);  // Use setPosition for Sprite
                    boximage.draw(Batch);
                } else if (matrix.get(i).y == (float) 0) {
                    emptybox.setPosition(rectangle.x, rectangle.y);  // Use setPosition for Sprite
                    emptybox.draw(Batch);
                } else {
                    bombimage.setPosition(rectangle.x, rectangle.y);  // Use setPosition for Sprite
                    bombimage.draw(Batch);
                }
                if (!boxeRect.isEmpty()) {
                    posX = boxeRect.get(T).getX();
                    posY = boxeRect.get(T).getY();
                }
            }

        Batch.end();





        cursorX=Gdx.input.getX();
        cursorY=Gdx.input.getY();
        float screenHeight = Gdx.graphics.getHeight();
        cursorY = screenHeight - cursorY;



    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);

    }

    @Override
    public void dispose() {

    }



    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
}
