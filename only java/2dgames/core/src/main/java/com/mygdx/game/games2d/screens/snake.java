package com.mygdx.game.games2d.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import static com.badlogic.gdx.math.MathUtils.random;


public class snake implements Screen {
    SpriteBatch batch;
    Texture snake;
    Rectangle snakeRect;

    Texture mango;
    Rectangle fruteRect;
    float WorldHeight=Gdx.graphics.getHeight()-5,WorldWidth=Gdx.graphics.getWidth()-5;

    ShapeRenderer shapeRenderer;
    public snake(){
       batch = new SpriteBatch();
       shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        snake = new Texture("snake.jpg");
       snakeRect = new Rectangle((int) snakeX, (int) snakeY,50,10);
       mango = new Texture("yellow-mango.jpeg");
       fruteRect= new Rectangle(10,10,10,10);
    }
    float snakeX=0,snakeY=0;
    int moveX=0;
    int moveY=0;
    int moveSpeed=50;
    int score = 0;
    void ProcessInput(float deltatime){
        if (snakeRect.overlaps(fruteRect)){
            score+=1;
            moveSpeed+=5;

            fruteRect.setPosition(randomNumber(),randomNumber());
            System.out.println("score is "+ score);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)){
            moveY=moveSpeed;
            moveX=0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)){
            moveY=-moveSpeed;
            moveX=0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)){
            moveX=-moveSpeed;
            moveY=0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)){
            moveX=moveSpeed;
            moveY=0;
        }
        snakeRect.setPosition(snakeX,snakeY);
        if (snakeX<WorldWidth && snakeX>5 && snakeY<WorldHeight && snakeY>5){
            snakeX+=moveX*deltatime;
            snakeY+=moveY*deltatime;
        }

    }


    @Override
    public void show() {
        snakeX=randomNumber();
        snakeY= randomNumber();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Set clear color (black in this case)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ProcessInput(delta);
        batch.begin();
        batch.draw(snake,snakeX,snakeY,50,10);
        batch.draw(mango,  fruteRect.getX(),  fruteRect.y,  fruteRect.getWidth(),  fruteRect.getHeight());
        batch.end();

        shapeRenderer.begin();
        shapeRenderer.setColor(5,40,10,1);
        shapeRenderer.line(5,5,5,WorldHeight);
        shapeRenderer.line(5,5,WorldWidth,5);

        shapeRenderer.line(5,WorldHeight,WorldWidth,WorldHeight);
        shapeRenderer.line(WorldWidth,5,WorldWidth,WorldHeight);
        shapeRenderer.end();
    }



    void fruteMove (){

    }
    int randomNumber(){
        System.out.println(WorldWidth);
        if (WorldWidth<WorldHeight){
            int randomInt = random.nextInt((int) (WorldWidth - 5)) + 5;
            return randomInt;
        }
        else {
            int randomInt = random.nextInt((int) (WorldHeight - 5)) + 5;
            return randomInt;

        }
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

        batch.dispose();
    }
}
