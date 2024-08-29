package com.mygdx.game.shooter;
import com.badlogic.gdx.Game;


import com.badlogic.gdx.Gdx;
import com.mygdx.game.shooter.Controls.movement;
import com.mygdx.game.shooter.PLAYER.player;
import com.mygdx.game.shooter.SCREENS.LoadingScene;
import com.mygdx.game.shooter.SCREENS.game;
import com.mygdx.game.shooter.SCREENS.threeDscreen;
import com.mygdx.game.shooter.SCREENS.loadingmodel;

public class Main extends Game {
    public static int WIDTH= 480;
    public static int HEIGHT= 700;
    public static float gravity = -9.8f; // Adjust as needed
    public static float jumpForce = 10f;
    public static player player;


    public static game Game;
    public static threeDscreen threeDgame;
    public static loadingmodel loadingmodel;
    public static LoadingScene loadingScene;
    public static BulletTest bulletTest;









//    public Main(){
//        WIDTH= 480;
//        HEIGHT= 900;
//    }

    @Override
    public void create() {
        Game = new game();
        player=new player();
        threeDgame = new threeDscreen();
        loadingmodel= new loadingmodel();
        loadingScene = new LoadingScene();
        bulletTest = new BulletTest();
        this.setScreen (bulletTest);


//        cameracontroller =  new cameracontroller();


    }

    @Override
    public void render(){
//        movement.render(Gdx.graphics.getDeltaTime());
        super.render();
    }

}
