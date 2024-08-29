package com.mygdx.game.shooter.PLAYER;

import com.badlogic.gdx.math.Rectangle;

public class player {
    private float X=0;
    private float Y=0;
    private float Z=0;
    private Rectangle playerRect;
    public float getX(){return X;}
    public float getY(){return Y;}
    public float getZ(){return Y;}
    public void setX(float x){this.X+=x;}
    public void setY(float y){this.Y+=y;}
    public void setZ(float z){this.Y+=z;}

    public player(){}
}
