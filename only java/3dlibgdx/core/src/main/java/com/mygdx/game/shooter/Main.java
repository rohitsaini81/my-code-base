package com.mygdx.game.shooter;
import com.badlogic.gdx.Game;


import com.badlogic.gdx.Gdx;
import com.mygdx.game.shooter.Controls.movement;
import com.mygdx.game.shooter.PLAYER.player;
import com.mygdx.game.shooter.SCREENS.*;
import com.mygdx.game.shooter.SCREENS.Bullet.bullet1;

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
    public static animation animation;
    public static something something;
    public static CollisionTerrain collisionTerrain;
//    public static MundusLoading mundusLoading;
    public static bullet1 bullet_1;




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
        animation = new animation();
        something = new something();
        collisionTerrain = new CollisionTerrain();
//        mundusLoading = new MundusLoading(); // not workin project needs to export again
        bullet_1 = new bullet1();
        this.setScreen (bullet_1);




    }

    @Override
    public void render(){
        super.render();
    }

}
