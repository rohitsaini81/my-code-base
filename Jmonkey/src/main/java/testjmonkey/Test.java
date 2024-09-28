package testjmonkey;

import com.jme3.anim.AnimComposer;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.input.controls.*;
import com.jme3.math.Plane;

/**
 * This is the Main Class of your Game. It should boot up your game and do initial initialisation
 * Move your Logic into AppStates or Controls or other java classes
 */
public class Test extends SimpleApplication {

    public static void main(String[] args) {
        Test app = new Test();
        app.start();
    }


    Box b;
    Geometry geom;
    Spatial playerModel;
    CharacterControl playerControl;
    RigidBodyControl landscape;


    Vector3f playerPosition;
    Spatial terrain;

    BoxCollisionShape boxCollisionShape;


    @Override
    public void simpleInitApp() {
        b = new Box(1, 1, 1);
        geom = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        
        terrain = assetManager.loadModel("Scenes/my_terrain.gltf");

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.5f, -1f, -0.5f).normalizeLocal());
        flyCam.setMoveSpeed(20);  // Increase the movement speed to 50 units per second

        rootNode.addLight(sun);
        rootNode.attachChild(terrain);
        rootNode.attachChild(geom);

        playerModel = assetManager.loadModel("Models/player.gltf");
        playerPosition = new Vector3f(5,40,0);
        rootNode.attachChild(playerModel);
        playerModel.setLocalTranslation(playerPosition); // Set the new position


// Use the new AnimComposer to handle animations
AnimComposer animComposer = playerModel.getControl(AnimComposer.class);
if (animComposer != null) {
    // Get and play an animation from the AnimComposer
    animComposer.setCurrentAction("running");  // Replace "Run" with your animation name
} else {
    System.out.println("AnimComposer not found!");
}


initKeys();

//        flyCam.setEnabled(false);
//        chaseCam = new ChaseCamera(cam,geom, inputManager);
//        chaseCam.setSmoothMotion(true);



        physics_init();
    }
//    ChaseCamera chaseCam;
    
    private CharacterControl player;
    void physics_init(){
        
        BulletAppState bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(terrain);
        landscape = new RigidBodyControl(sceneShape, 0);    
        terrain.addControl(landscape);
        bulletAppState.getPhysicsSpace().add(landscape);


        boxCollisionShape = new BoxCollisionShape(1, 1, 1);  // (radius, height, axis)

        playerControl = new CharacterControl(boxCollisionShape, 1);
        playerControl.setJumpSpeed(20);
        playerControl.setFallSpeed(30);
        playerControl.setGravity(30);
        playerControl.setPhysicsLocation(playerPosition); // Initial player position
        bulletAppState.getPhysicsSpace().add(playerControl);



    }

//    float velocity = 0f;
//    float gravity = 0.3f;
//    float jumpForce = 10;
    float playerSpeed = 0.1f;
    Vector3f camPosition=new Vector3f();
    @Override
    public void simpleUpdate(float delta) {

        playerModel.setLocalTranslation(playerPosition);
        playerPosition.y=playerControl.getPhysicsLocation().y;
        camupdate();
        playerControl.setPhysicsLocation(playerPosition);

        if (playerPosition.y<=32){
            playerPosition.y=32;
        }
//        playerPosition.y+=velocity*delta;
//        if (velocity<=-10){velocity=0;}else {velocity-=delta+gravity;}

        
        
        rootNode.getChild("Box").setLocalTranslation(playerControl.getPhysicsLocation());





    }

    void camupdate(){
        camPosition.setX(playerPosition.getX()-5f);
        camPosition.setZ(playerPosition.getZ()-5f);
        camPosition.setY(playerPosition.getY()+5f);
        cam.setLocation(camPosition);
    }
    @Override
    public void simpleRender(RenderManager rm) {

    }



    private Boolean isRunning = true;
    private void initKeys() {
        /* You can map one or several inputs to one named mapping. */
        inputManager.addMapping("W",  new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("A",  new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("S",  new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("D",  new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addListener(actionListener,"Space");
        inputManager.addListener(analogListener, "W","A", "S", "D");


    }


  
    final private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Space") && !keyPressed) {
                System.out.println("Space");
//                velocity=jumpForce;
                playerControl.jump();

            }
        }
    };




    /** Use this listener for continuous events */
    final private AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            if (isRunning) {
                if (name.equals("W")) {
                    System.out.println("W");
                    playerPosition.z+=playerSpeed;

                }
                if (name.equals("A")) {
                    System.out.println("A");
                    playerPosition.x+=playerSpeed;
                    geom.rotate(0, tpf * 2, 0);
//                    chaseCam.set



                }
                if (name.equals("S")) {
                    System.out.println("S");
                    playerPosition.z-=playerSpeed;

                }
                if (name.equals("D")) {
                    System.out.println("D");
                    playerPosition.x-=playerSpeed;
                    geom.rotate(0, -tpf * 2, 0);
//                    chaseCam.s;

                }

            }

        }
    };






}
