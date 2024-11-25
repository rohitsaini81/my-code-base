package testjmonkey.newpackage;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;

public class playerControl1 extends BetterCharacterControl {
    Vector3f positionLocation;
    public playerControl1(float radius, float height, float mass){
        super(radius,height,mass);
        positionLocation= new Vector3f();
    }

    public void setPhysicsLocation(Vector3f vector3f){
        super.setPhysicsLocation(vector3f);
    }
    public Vector3f getPhysicsLocation(){
        return positionLocation;
    }
}
