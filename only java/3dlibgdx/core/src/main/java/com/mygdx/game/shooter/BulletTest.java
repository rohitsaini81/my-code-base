package com.mygdx.game.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import static com.badlogic.gdx.Gdx.input;

public class BulletTest implements Screen {

    static class GameObject extends ModelInstance implements Disposable {
        public final btCollisionObject body;
        public boolean moving=true;

        public GameObject(Model model, String node, btCollisionShape shape) {
            super(model, node);
            body = new btCollisionObject();
            body.setCollisionShape(shape);
        }

        @Override
        public void dispose() {
            body.dispose();
        }
    }

    PerspectiveCamera cam;
    FirstPersonCameraController firstPersonCameraController;
    ModelBatch modelBatch;
    Environment environment;
    Model model;
    Array<GameObject> instances;
    GameObject ground;
    GameObject ball;
    GameObject wall;

    btCollisionConfiguration collisionConfig;
    btDispatcher dispatcher;

    BulletTest() {
        create();
    }

    public void create() {
        Bullet.init();

        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(3f, 7f, 10f);
        cam.lookAt(0, 4f, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        firstPersonCameraController = new FirstPersonCameraController(cam);
        Gdx.input.setInputProcessor(firstPersonCameraController);

        ModelBuilder mb = new ModelBuilder();
        mb.begin();
        mb.node().id = "ground";
        mb.part("ground", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
            new Material(ColorAttribute.createDiffuse(Color.RED))).box(10f, 1f, 10f);
        mb.node().id="wall";
        mb.part("wall", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
            new Material(ColorAttribute.createDiffuse(Color.RED))).box(10f, 10f, 2f);

        mb.node().id = "sphere";
        mb.part("sphere", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
            new Material(ColorAttribute.createDiffuse(Color.GREEN))).sphere(1f, 1f, 1f, 10, 10);
        model = mb.end();

        ground = new GameObject(model, "ground", new btBoxShape(new Vector3(2.5f, 0.5f, 2.5f)));
        ball = new GameObject(model, "sphere", new btSphereShape(0.5f));
        wall = new GameObject(model,"wall",new btBoxShape(new Vector3(0f,0f,0f)));
        ball.transform.setTranslation(0, 10f, 0);

        instances = new Array<>();
        instances.add(ground);
        instances.add(ball);
        instances.add(wall);

        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        y=5;

    }

    float SPEED=2;
    float x,y,z=0;
    float x_pre,y_pre,z_pre=0;//previews values
    @Override
    public void render(float delta) {

//        y_pre = y;
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){y_pre=y;y+=delta*SPEED;}
        if (Gdx.input.isKeyPressed(Input.Keys.V)){y_pre=y;y-=delta*SPEED;}
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){z_pre=z;z-=delta*SPEED;}
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){z_pre=z;z+=delta*SPEED;}
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){x_pre=x;x-=delta*SPEED;}
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){x_pre=x;x+=delta*SPEED;}


        final float deltatime = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());

//        if (ball.moving){
            ball.body.setWorldTransform(ball.transform);
            ball.transform.setToTranslation(x, y, z);

//        }


            if (checkCollision(ball.body, ground.body)) {
                System.out.println("Y : "+y+" pre:"+y_pre);
//                x=x_pre;
//                y+=delta*SPEED;
                y=y_pre;
//                z=z_pre;
//                ball.moving = false;
                System.out.println("collision");
            }else {
                ball.moving=true;
            }

        firstPersonCameraController.update();

        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(instances, environment);
        modelBatch.end();
    }

    boolean checkCollision(btCollisionObject obj0, btCollisionObject obj1) {
        btCollisionAlgorithm algorithm = dispatcher.findAlgorithm(new CollisionObjectWrapper(obj0).wrapper,
            new CollisionObjectWrapper(obj1).wrapper,null,0);

        btDispatcherInfo info = new btDispatcherInfo();
        btManifoldResult result = new btManifoldResult(new CollisionObjectWrapper(obj0).wrapper,
            new CollisionObjectWrapper(obj1).wrapper);

        algorithm.processCollision(new CollisionObjectWrapper(obj0).wrapper,
            new CollisionObjectWrapper(obj1).wrapper, info, result);

        boolean r = result.getPersistentManifold().getNumContacts() > 0;

        dispatcher.freeCollisionAlgorithm(algorithm.getCPointer());
        result.dispose();
        info.dispose();

        return r;
    }

    @Override
    public void dispose() {
        for (GameObject obj : instances)
            obj.dispose();
        instances.clear();

        dispatcher.dispose();
        collisionConfig.dispose();

        modelBatch.dispose();
        model.dispose();
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
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }
}
