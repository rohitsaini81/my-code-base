package com.mygdx.game.physics.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.physics.etc.*;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.awt.*;

import static com.badlogic.gdx.physics.bullet.collision.ebtDispatcherQueryType.BT_CONTACT_POINT_ALGORITHMS;
import static com.mygdx.game.physics.etc.HeightMapTerrain.terrainShape;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
    import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
    import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
    import com.badlogic.gdx.utils.Array;
import com.mygdx.game.physics.etc.HeightMapTerrain;
import com.mygdx.game.physics.etc.Terrain;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.awt.*;

    import static com.mygdx.game.physics.etc.HeightMapTerrain.terrainShape;

public class btTerrain implements Screen {
    SpriteBatch batch;
    Font logs;



    ModelBatch modelBatch;
    Array<ModelInstance> instances;

    PerspectiveCamera Camera;
    CameraInputController cameraInputController;
    FirstPersonCameraController firstPersonCameraController;
    Environment environment;


    Model model;
    ModelInstance ground;
    ModelInstance ball;
    public btTerrain(){
        init();
        create();
        init__Bullet();
    }
    private void init(){
        modelBatch = new ModelBatch();
        batch = new SpriteBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        Camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Camera.position.set(3f, 7f, 10f);
        Camera.lookAt(0, 4f, 0);
        Camera.update();
        firstPersonCameraController = new FirstPersonCameraController(Camera);
        cameraInputController = new CameraInputController(Camera);
        Gdx.input.setInputProcessor(firstPersonCameraController);


    }


    private void create(){
        ModelBuilder mb = new ModelBuilder();
        mb.begin();
        mb.node().id = "ground";
        mb.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(com.badlogic.gdx.graphics.Color.RED)))
            .box(5f, 0.5f, 5f);
        mb.node().id = "ball";
        mb.part("sphere", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN)))
            .sphere(1f, 1f, 1f, 10, 10);
        model = mb.end();

        ground = new ModelInstance(model, "ground");
        ball = new ModelInstance(model, "ball");
        ball.transform.setToTranslation(0, 9f, 0);

        instances = new Array<ModelInstance>();
        instances.add(ground);
        instances.add(ball);
    }
    Terrain terrain;
    Scene terrainScene;
    SceneManager sceneManager = new SceneManager();
    GltfTerrainToHeightfield gltfTerrainToHeightfield;
    @Override
    public void show() {
        terrain = new HeightMapTerrain(new Pixmap(Gdx.files.internal("textures/heightmap.png")), 3f);
        instances.add(terrain.getModelInstance());
        instances.add(ball);
//        sceneManager.setCamera(Camera);
//        sceneManager.addScene(terrainScene);
    }
    btCollisionShape btterrain;
    btCollisionShape groundShape;
    btCollisionShape ballShape;
    btCollisionObject groundObject;
    btCollisionObject ballObject;
    btCollisionObject btTerrainObject;


    btCollisionConfiguration collisionConfig;
    btDispatcher dispatcher;
    private void init__Bullet(){
        Bullet.init();
        gltfTerrainToHeightfield = new GltfTerrainToHeightfield("",100);
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);



        ballShape = new btSphereShape(0.5f);
        groundShape = new btBoxShape(new Vector3(2.5f, 0.5f, 2.5f));
        btterrain = gltfTerrainToHeightfield.getTerrainShape();


        groundObject = new btCollisionObject();
        groundObject.setCollisionShape(groundShape);
        groundObject.setWorldTransform(ground.transform);

        ballObject = new btCollisionObject();
        ballObject.setCollisionShape(ballShape);
        ballObject.setWorldTransform(ball.transform);


        btTerrainObject = new btCollisionObject();
        btTerrainObject.setCollisionShape(btterrain);
        btTerrainObject.setWorldTransform(instances.get(instances.size-1).transform);
    }
    boolean checkCollision(){
        CollisionObjectWrapper co0 = new CollisionObjectWrapper(ballObject);
        CollisionObjectWrapper co1 = new CollisionObjectWrapper(groundObject);
        CollisionObjectWrapper co2 = new CollisionObjectWrapper(btTerrainObject);
        btCollisionAlgorithmConstructionInfo ci = new btCollisionAlgorithmConstructionInfo();
        ci.setDispatcher1(dispatcher);
//        btCollisionAlgorithm algorithm = new btSphereBoxCollisionAlgorithm(null,ci,co0.wrapper,co1.wrapper,false);


        btCollisionAlgorithm algorithm;
        algorithm = dispatcher.findAlgorithm(co0.wrapper, co2.wrapper,null, 0);




        btDispatcherInfo info = new btDispatcherInfo();
        btManifoldResult result = new btManifoldResult(co0.wrapper,co2.wrapper);
        algorithm.processCollision(co0.wrapper,co2.wrapper,info,result);
        boolean r = result.getPersistentManifold().getNumContacts()>0;
        result.dispose();
        info.dispose();
        algorithm.dispose();
        ci.dispose();
        co2.dispose();
        co0.dispose();
        return r;
    }

    boolean isCollision=false;
    float deltaTime=0;
    @Override
    public void render(float delta) {
        deltaTime = Math.min(1f/30f,delta);
//        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (!isCollision){
            ball.transform.translate(0f, -delta, 0f);
            ballObject.setWorldTransform(ball.transform);
        }
        isCollision = checkCollision();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            ball.transform.translate(0f, 10*delta, 0f);
            ballObject.setWorldTransform(ball.transform);

        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            ball.transform.translate(0f, 0, 20*delta);
            ballObject.setWorldTransform(ball.transform);

        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            ball.transform.translate(0f, 0, -20*delta);
            ballObject.setWorldTransform(ball.transform);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            ball.transform.translate(-20*delta, 0, 0f);
            ballObject.setWorldTransform(ball.transform);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            ball.transform.translate(20*delta, 0, 0f);
            ballObject.setWorldTransform(ball.transform);
        }
        firstPersonCameraController.update();
//        cameraInputController.update();
        modelBatch.begin(Camera);
        modelBatch.render(instances, environment);
        modelBatch.end();
//        sceneManager.render();
//        sceneManager.update(delta);
        instances.get(0).transform.rotate(0,1,0,1);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        model.dispose();
        groundObject.dispose();
        groundShape.dispose();

        ballObject.dispose();
        ballShape.dispose();

        dispatcher.dispose();
        collisionConfig.dispose();

        modelBatch.dispose();
    }
}

