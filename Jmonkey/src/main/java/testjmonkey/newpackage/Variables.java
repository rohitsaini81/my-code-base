package testjmonkey.newpackage;

import com.jme3.anim.AnimComposer;
import com.jme3.app.state.RootNodeAppState;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import jme3utilities.mesh.CapsuleMesh;
import testjmonkey.Test;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class Variables {
    public static float PlayerSpeed = 50f;
    public static Vector3f walkDirection=new Vector3f();

    public static CollisionShape collisionShape;
    public static Spatial House;
    public static RigidBodyControl HouseControl;



    public static Box box;
    public static Geometry box_geom;
    public static Geometry capsuleMesh_geom;

    public static Spatial playerModel;

    public static CharacterControl playerControl;
    public static GhostControl ghostControl;


    public static BulletAppState bulletAppState;
    public static Vector3f playerPosition;
    public static Spatial terrain;
    public static Material material;


    public static AssetManager assetManager;
    public static Node node;


    public static void constructor(AssetManager assetManager1,Node rn){
        playerPosition = new Vector3f(5,40,0);
        assetManager = assetManager1;
        node = rn;
        bulletAppState = new BulletAppState();
        setModels();
        setShapes();
        shapes_physics();




//        tcpClient = new TCPClient("127.0.0.1", 65432);


        setFile();
        isFileUpdated();
    }







    public static void setModels(){
        playerModel = assetManager.loadModel("Models/player/player.gltf");
        House = assetManager.loadModel("Models/housemodel/joint/untitled.glb");
        collisionShape = CollisionShapeFactory.createMeshShape(House);
        HouseControl= new RigidBodyControl(collisionShape,0);
        House.addControl(HouseControl);

        HouseControl.setPhysicsLocation(Position);
        node.attachChild(House);
        node.attachChild(playerModel);




    }
    public static String materia_String="Common/MatDefs/Misc/Unshaded.j3md";
    public static void setShapes(){
        box = new Box(1, 1, 1);
        box_geom = new Geometry("box", box);
        material = new Material(assetManager,materia_String );
        material.setColor("Color", ColorRGBA.Blue);
        box_geom.setMaterial(material);
        box_geom.setLocalTranslation(playerPosition);


        CapsuleMesh capsuleMesh= new CapsuleMesh(1, 1f, 1.5f);
        capsuleMesh_geom = new Geometry("capsule",capsuleMesh);
        material.setColor("Color", ColorRGBA.Red);
        capsuleMesh_geom.setMaterial(new Material(assetManager,materia_String));
        capsuleMesh_geom.getMaterial().setColor("Color",ColorRGBA.fromRGBA255(15,10,0,100));

    }
    public static RigidBodyControl box1;
    public static void shapes_physics(){
//        CollisionShape boxCollisionShape;
//        boxCollisionShape =  CollisionShapeFactory.createMeshShape(box_geom);
        BoxCollisionShape boxCollisionShape;
       boxCollisionShape = new BoxCollisionShape(1,1f,1f);


        box1 = new RigidBodyControl(boxCollisionShape,1);

    }


    public static void setAnimation(){
//        AnimComposer animComposer = model.getControl(AnimComposer.class);
//        animComposer.setCurrentAction("AnimationName");

    }
    public static String path = "Scripts/Position.txt";
    public static long lastModifiedTime = 0;
    public static URL resourceUrl;
    public static void setFile(){
        resourceUrl = Test.class.getClassLoader().getResource(path);
    }
    public static Path Path;
    public static void isFileUpdated(){

        if (resourceUrl != null) {
            try {
                Path path = Paths.get(resourceUrl.toURI());

                // Check if file is modified
                long lastModified = Files.getLastModifiedTime(path).toMillis();

                if (lastModified > lastModifiedTime) {
                    lastModifiedTime = lastModified;
                    readFile();
                    System.out.println(lastModifiedTime+" "+ lastModified);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    public static Vector3f Position = new Vector3f(300,7,0);
    public static void readFile(){
        try {
            assert resourceUrl != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceUrl.openStream()))) {
                String line;
                ArrayList<String> dataset=new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    dataset.add(line);
                    String []data = line.split(",");
                    Position.set(Float.parseFloat(data[0]),Float.parseFloat(data[1]),Float.parseFloat(data[2]));

                }
                System.out.println(dataset);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return new ArrayList<>();
    }


    public static void Update(){
        isFileUpdated();
        HouseControl.setPhysicsLocation(Position);

    }
}
