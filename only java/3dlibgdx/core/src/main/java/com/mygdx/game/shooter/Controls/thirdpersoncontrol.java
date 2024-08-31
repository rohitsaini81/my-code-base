package com.mygdx.game.shooter.Controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;

public class thirdpersoncontrol extends CameraInputController {
    public thirdpersoncontrol(Camera camera) {
        super(camera);
    }
    private final Vector3 tmpV1 = new Vector3();
    private final Vector3 tmpV2 = new Vector3();

    @Override
    public void update(){
        System.out.println("this :"+super.rotateRightPressed);
        if (rotateRightPressed || rotateLeftPressed || forwardPressed || backwardPressed) {
            System.out.println("here");
            final float delta = Gdx.graphics.getDeltaTime();
            if (rotateRightPressed) camera.rotate(camera.up, -delta * rotateAngle);
            if (rotateLeftPressed) camera.rotate(camera.up, delta * rotateAngle);
            if (forwardPressed) {
                camera.translate(tmpV1.set(camera.direction).scl(delta * translateUnits));
                if (forwardTarget) target.add(tmpV1);
            }
            if (backwardPressed) {
                camera.translate(tmpV1.set(camera.direction).scl(-delta * translateUnits));
                if (forwardTarget) target.add(tmpV1);
            }
            if (autoUpdate) camera.update();
        }
    }

}
