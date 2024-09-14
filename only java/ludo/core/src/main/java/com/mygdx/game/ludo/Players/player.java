package com.mygdx.game.ludo.Players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class player {
    public int move; // how many move have player used , total moves are 58 till winn I guess
    public int index;
    public  void update(){
//        if (move<0 || move>20){
//            System.out.println("there is a problem");
//        }

    }
    public static class thread extends Thread {

        int x,y;

        @Override
        public void run(){
            while (true){
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
                    System.out.println("clicked space");
                }
            }
        }
    }
}
