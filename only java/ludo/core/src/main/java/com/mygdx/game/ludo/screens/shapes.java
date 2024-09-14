package com.mygdx.game.ludo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.ludo.Players.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import static com.mygdx.game.ludo.screens.FirstScreen.centerx;
import static com.mygdx.game.ludo.screens.FirstScreen.centery;

public class shapes {
    public ShapeRenderer shapeRenderer;
    float x,y;
    boolean[][] matrix;
    boolean[][] playermove;
    int[]playerposition;


    int arrayy=15;
    int arrayx=15;
    shapes(){
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        box1 = new squeares();
        matrix = new boolean[arrayy][arrayx];
        playermove = new boolean[arrayy][arrayx];
        playerposition =new int[2];
        playerposition[0]=14;playerposition[1]=6;
        x= centerx+10;
        y = centery+10;
        int counter=0;
        for (int i = 0; i < arrayy; i++) {
            for (int j = 0; j < arrayx; j++) {
                playermove[i][j] = false;

//                i == y
                if ((j>5 && j<9 )|| (i >5 && i <9)){
//                    j == x
                    matrix[i][j] = true;
                    counter++;
                    if ((j== 6 || j==8) && (i==8 || i==6 ) ){
                        matrix[i][j] = false;
                    }
                }
            }
        }
        int x=0;

        for (int i = 0; i <arrayy; i++) {
            for (int j = 0; j < arrayx; j++) {
                if (matrix[i][j]) {
                    box1.table[i].add(i);
                    box1.table[i].add(j);
                }
//                System.out.println(box1.table[i].get(0)+", "+box1.table[i].get(1));
            }
        }
        System.out.println(matrix[(int) box1.table[0].get(0)][(int)box1.table[14].get(1)]);
//        matrix[(int) box1.table[0].get(0)][(int)box1.table[14].get(1)]

        show();
    }



public class squeares {
    public ArrayList[] table = new ArrayList[80];

    squeares(){
        for (int i = 0; i < table.length; i++) {
            table[i]=new ArrayList<>(2);
        }
        }
}
    public void show(){
        th= new player.thread();
        th.start();

    }
    player.thread th;

    squeares box1;
    public void update(){
        shapeRenderer.begin();
        shapeRenderer.setColor(Color.RED);

        for (int i = 0; i < arrayy; i++) {
            for (int j = 0; j < arrayx; j++) {
                if (i==playerposition[0] && j== playerposition[1]){
                    playermove[i][j] = true;
                }else{
                playermove[i][j] = false;
                }
            }
        }

        for (float i = centery; i <= 380; i+=20) {
            for (float j = centerx; j <= 450; j+=20) {
                shapeRenderer.rect(j,i,20,20);
//                shapeRenderer.circle(x,y,10,10);
                x=centerx+10;
                y=centery+10;
//                System.out.println(playerposition[1]);

                for (int yz = 0; yz < arrayy; yz++) {
                    for (int xz = 0; xz < arrayx; xz++) {
                        if (matrix[yz][xz]){


                        shapeRenderer.circle(x,y,10,10);
                            if (playermove[yz][xz]){
                                shapeRenderer.rect(x,y,10,10);

                            }
                        }

                        x+=20;
                    }
                    x=centerx+10;
                    y+=20;
                }
            }
        }
        shapeRenderer.end();
    }

    public boolean checkCollision(float x1,float y1,float x2, float y2){
        return x1<x2+20 && x1+20>x2 && y1<y2+20 && y1+20>y2;
    }
}
