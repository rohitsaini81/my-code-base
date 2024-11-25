package testjmonkey;

import com.jme3.anim.AnimComposer;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.*;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.TouchInput;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.input.controls.*;
import com.jme3.system.AppSettings;
import jme3utilities.mesh.CapsuleMesh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

import static testjmonkey.newpackage.Variables.*;

/**
 * This is the Main Class of your Game. It should boot up your game and do initial initialisation
 * Move your Logic into AppStates or Controls or other java classes
 */
public class Test extends SimpleApplication{
    public static Socket socket;
    public static BufferedReader in;
    public static boolean Interrupted;
    public static Thread thread;
    public static void main(String[] args) {
        try {
            socket = new Socket("localhost",65432);
            Interrupted = false;
//            socket.setSoTimeout(5000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            thread = new Thread(() -> {
        try {
            String msg;
            while (!Interrupted){
                    msg=in.readLine();
                    System.out.println(msg);
            }

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    });
            thread.start();



        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }


        Test app = new Test();
        app.settings= new AppSettings(true);
        app.settings.setWidth(1000);
        app.settings.setHeight(690);

//        app.settings.setFullscreen(true);

        app.start();

        try {
            Interrupted = true;
            thread.interrupt();
            if (!thread.isAlive()){
                socket.shutdownInput();
                socket.shutdownOutput();
                System.out.println("closed");
                socket.setSoTimeout(1000);
                in.reset();
                in.close();
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }



    }







    public ChaseCamera chaseCamera;

    @Override
    public void simpleInitApp() {

        constructor(assetManager,rootNode);





        terrain = assetManager.loadModel("Scenes/my_terrain.gltf");
//        terrain = assetManager.loadModel("Models/terrain/terrain.gltf");

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.5f, -1f, -0.5f).normalizeLocal());
        flyCam.setMoveSpeed(20);  // Increase the movement speed to 50 units per second

        rootNode.addLight(sun);
        rootNode.attachChild(terrain);
        rootNode.attachChild(box_geom);



// Use the new AnimComposer to handle animations
//AnimComposer animComposer = playerModel.getControl(AnimComposer.class);
//if (animComposer != null) {
    // Get and play an animation from the AnimComposer
//    animComposer.setCurrentAction("idle");  // Replace "Run" with your animation name
//} else {
//    System.out.println("AnimComposer not found!");
//}

        initKeys();


        capsuleMesh_geom.setLocalTransform(new Transform(playerPosition));

        physics_init();
        flyCam.setEnabled(false);
        chaseCamera= new ChaseCamera(cam,capsuleMesh_geom,inputManager);
        chaseCamera.setDefaultDistance(1);

//        chaseCamera.setDragToRotate(false);
//        inputManager.setCursorVisible(false);
        flyCam.setEnabled(false);

        cam.setLocation(playerPosition);

    }

    private CharacterControl player;
    void physics_init(){
        RigidBodyControl landscape;
        stateManager.attach(bulletAppState);
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(terrain);
        landscape = new RigidBodyControl(sceneShape, 0);
        terrain.addControl(landscape);
        bulletAppState.getPhysicsSpace().add(landscape);
        bulletAppState.getPhysicsSpace().add(HouseControl);

        player_init();

    }


    void player_init(){
        CapsuleCollisionShape capsuleCollisionShape;

        capsuleCollisionShape = new CapsuleCollisionShape(0.5f, 1.5f, 1);
        playerControl =  new CharacterControl(capsuleCollisionShape,1f);
        playerControl.setJumpSpeed(20);
        playerControl.setFallSpeed(30);
        playerControl.setGravity(30);
        playerControl.setPhysicsLocation(playerPosition); // Initial player position
        ghostControl = new GhostControl(capsuleCollisionShape);
        ghostControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        ghostControl.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        bulletAppState.getPhysicsSpace().add(playerControl);
        bulletAppState.getPhysicsSpace().add(ghostControl);
        ghostControl.setPhysicsLocation(playerControl.getPhysicsLocation());



        rootNode.attachChild(capsuleMesh_geom);
        capsuleMesh_geom.addControl(playerControl);



        bulletAppState.getPhysicsSpace().add(playerControl);

        bulletAppState.getPhysicsSpace().add(box1);
        box_geom.addControl(box1);


//        bulletAppState.getPhysicsSpace().addCollisionListener(this);

    }


    @Override
    public void simpleUpdate(float delta) {
        playerControl.setWalkDirection(walkDirection);
        ghostControl.setPhysicsLocation(playerControl.getPhysicsLocation());
        playerPosition.y=playerControl.getPhysicsLocation().y;

        checkCollisions();
        chaseCamera.setLookAtOffset(Vector3f.UNIT_Y);


        cam_update();
        Update();
    }
    void cam_update(){

    }
    @Override
    public void simpleRender(RenderManager rm) {
        Update();
    }



    private Boolean isRunning = true;
    private void initKeys() {
        /* You can map one or several inputs to one named mapping. */
        inputManager.addMapping("W",  new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("A",  new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("S",  new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("D",  new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Shift", new KeyTrigger(KeyInput.KEY_LSHIFT));

        inputManager.addListener(actionListener,"Space", "W","A", "S", "D","Shift");
        inputManager.addListener(analogListener, "Shift","Space","W","A", "S", "D");


    }


    boolean isHolding=false;

    final private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {


            if ()


                if (name.equals("Space") && !keyPressed) {
                playerControl.jump();
                }

                if (name.equals("Shift")) {
                isHolding=false;
                    System.out.println("key up");
                }

                if (!keyPressed) {
//                System.out.println("will it work"+Math.random()*10);
                walkDirection.set(0,0,0);
                playerControl.setWalkDirection(walkDirection);

                }
        }
    };



    /** Use this listener for continuous events */
    final private AnalogListener analogListener = (name, value, tpf) -> {
        if (isRunning) {

            //
            if (name.equals("W")) {
                walkDirection.set(cam.getDirection().clone().multLocal(PlayerSpeed*tpf));
            }
            if (name.equals("A")) {
                walkDirection.set(cam.getLeft().clone().multLocal(PlayerSpeed*tpf));
            }
            if (name.equals("S")) {
                walkDirection.set(cam.getDirection().negate().clone().multLocal(PlayerSpeed*tpf));

            }
            if (name.equals("D")) {
                walkDirection.set(cam.getLeft().negate().clone().multLocal(PlayerSpeed*tpf));
            }

            //


            if (name.equals("Shift")) {
                System.out.println("key down");

                box1.setPhysicsLocation(playerControl.getPhysicsLocation().clone().multLocal(1f));
                isHolding=true;
            }
        }

    };


    private void checkCollisions() {
        // Get the list of objects that are overlapping with the GhostControl
        List<PhysicsCollisionObject> overlappingObjects = ghostControl.getOverlappingObjects();

        // Iterate through the overlapping objects and process collisions
        for (PhysicsCollisionObject obj : overlappingObjects) {
//            if (obj!=null){System.out.println(obj.nativeId());}
            if (obj == box1) {
                // Example: Check if the player is colliding with box1
//                System.out.println("Collision detected with box1!");
                if (!isHolding) {
//                    box1.applyCentralImpulse(cam.getDirection().clone().multLocal(0.3f));

                }

            }
            // You can add more checks for other objects here
        }
    }



}
