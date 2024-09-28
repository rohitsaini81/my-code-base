package testjmonkey.newpackage;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Plane;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;

public class Test extends SimpleApplication {

    private BulletAppState bulletAppState;
    private CharacterControl playerControl;
    private RigidBodyControl groundControl;

    public static void main(String[] args) {
        Test app = new Test();
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Capsule Collision Example");
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Initialize Bullet physics
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        // Create the ground
        createGround();

        // Create the player (capsule shape)
        createPlayer();

        // Add directional light to the scene
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.5f, -1f, -0.5f).normalizeLocal());
        rootNode.addLight(sun);

        // Disable default flyCam
        flyCam.setEnabled(false);

        // Add basic controls for the player (W, A, S, D)
        initKeys();
    }

    private void createGround() {
        // Create a box for the ground
        Box groundBox = new Box(20f, 0.1f, 20f);
        Geometry groundGeometry = new Geometry("Ground", groundBox);
        Material groundMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        groundMaterial.setColor("Color", ColorRGBA.Green);
        groundGeometry.setMaterial(groundMaterial);
        groundGeometry.setLocalTranslation(0, -0.1f, 0); // Slightly below y = 0

        // Attach the ground to the scene
        rootNode.attachChild(groundGeometry);

        // Create a collision shape for the ground
        PlaneCollisionShape groundShape = new PlaneCollisionShape(new Plane(Vector3f.UNIT_Y, 0));
        groundControl = new RigidBodyControl(groundShape, 0); // mass 0 = static
        groundGeometry.addControl(groundControl);

        // Add the ground to the physics space
        bulletAppState.getPhysicsSpace().add(groundControl);
    }

    private void createPlayer() {
        // Create a capsule shape for the player
//        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 2.5f, 1);
        BoxCollisionShape capsuleShape = new BoxCollisionShape(0,40,0);
        playerControl = new CharacterControl(capsuleShape, 0.05f);
        playerControl.setJumpSpeed(20);
        playerControl.setFallSpeed(30);
        playerControl.setGravity(30);
        playerControl.setPhysicsLocation(new Vector3f(0, 5, 0)); // Initial player position

        // Create a capsule geometry for visualization
        Box capsule = new Box(1, 40, 1);
//        Capsule capsule = new Capsule(10, 20, 1.5f, 2.5f);
        Geometry playerGeometry = new Geometry("Player", capsule);
        Material playerMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        playerMaterial.setColor("Color", ColorRGBA.Blue);
        playerGeometry.setMaterial(playerMaterial);

        // Attach the player geometry to the root node
        rootNode.attachChild(playerGeometry);
        playerGeometry.setLocalTranslation(playerControl.getPhysicsLocation());

        // Add the player to the physics space
        bulletAppState.getPhysicsSpace().add(playerControl);
    }

    private void initKeys() {
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
                playerControl.jump();
            }
        }
    };



     boolean isRunning=false;
    /** Use this listener for continuous events */
    final private AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            if (isRunning) {
                if (name.equals("W")) {
                    System.out.println("W");
                    playerControl.setWalkDirection(new Vector3f(0, 0, -1));

                }
                if (name.equals("A")) {
                    System.out.println("A");
                    playerControl.setWalkDirection(new Vector3f(-1, 0, 0));
                }
                if (name.equals("S")) {
                    System.out.println("S");
                    playerControl.setWalkDirection(new Vector3f(0, 0, 1));

                }
                if (name.equals("D")) {
                    System.out.println("D");
                    playerControl.setWalkDirection(new Vector3f(1, 0, 0));
                }

            }
        }
    };
    
    
    
    
    
    
    
    
    
    

    @Override
    public void simpleUpdate(float tpf) {
        // Update player geometry position to follow the CharacterControl
        rootNode.getChild("Player").setLocalTranslation(playerControl.getPhysicsLocation());
    }

    @Override
    public void simpleRender(com.jme3.renderer.RenderManager rm) {
        // Any additional rendering can go here
    }
}
