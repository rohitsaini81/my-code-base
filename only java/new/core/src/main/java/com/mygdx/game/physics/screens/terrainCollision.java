package com.mygdx.game.physics.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.g3d.model.Node;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.physics.etc.HeightMapTerrain;
import com.mygdx.game.physics.etc.Terrain;
import net.mgsx.gltf.scene3d.scene.Scene;

public class terrainCollision implements Screen, Disposable {
    SpriteBatch batch;
    ModelBatch modelBatch;
    Array<ModelInstance> instances;

    PerspectiveCamera Camera;
    CameraInputController CameraInputController;
    FirstPersonCameraController firstPersonCameraController;
    Environment environment;

    Model model;
    ModelInstance ground;
    ModelInstance ball;

    Terrain terrain;
    ModelInstance TerrainInstance;

    btDiscreteDynamicsWorld dynamicsWorld;
    btRigidBody groundBody;
    btRigidBody ballBody;
    btCollisionShape groundShape;
    btCollisionShape ballShape;

    @Override
    public void show() {
        Bullet.init();
        initGraphics();
        terrain = new HeightMapTerrain(new Pixmap(Gdx.files.internal("textures/heightmap.png")), 3f);
        TerrainInstance=terrain.getModelInstance();
        instances.add(TerrainInstance);
        initPhysics();

    }

    public terrainCollision() {
        init();
        firstPersonCameraController = new FirstPersonCameraController(Camera);
        Gdx.input.setInputProcessor(firstPersonCameraController);
    }

    private void init() {
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
        CameraInputController = new CameraInputController(Camera);
        Gdx.input.setInputProcessor(firstPersonCameraController);
    }

    private void initGraphics() {
        ModelBuilder mb = new ModelBuilder();
        mb.begin();
        mb.node().id = "ground";
        mb.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                new Material(ColorAttribute.createDiffuse(Color.RED)))
            .box(5f, 0.5f, 5f);
        mb.node().id = "ball";
        mb.part("sphere", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)))
            .sphere(1f, 1f, 1f, 10, 10);
        model = mb.end();

        ground = new ModelInstance(model, "ground");
        ball = new ModelInstance(model, "ball");
        ball.transform.setToTranslation(0, 9f, 0);

        instances = new Array<ModelInstance>();
        instances.add(ground);
        instances.add(ball);
    }

    private void initPhysics() {
        dynamicsWorld = createPhysicsWorld();

        // Ground collision shape
//        groundShape = createTerrainShape(ground);
        groundShape = createTerrainShape(TerrainInstance);

        // Ball collision shape
        ballShape = new com.badlogic.gdx.physics.bullet.collision.btSphereShape(0.5f);

        // Rigid bodies for ground and ball
        groundBody = createRigidBody(groundShape, 0, ground);
        ballBody = createRigidBody(ballShape, 1, ball);

        // Add them to the physics world
        dynamicsWorld.addRigidBody(groundBody);
        dynamicsWorld.addRigidBody(ballBody);
    }

    private btDiscreteDynamicsWorld createPhysicsWorld() {
        btCollisionConfiguration collisionConfig = new com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration();
        btDispatcher dispatcher = new com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher(collisionConfig);
        btBroadphaseInterface broadphase = new com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase();
        btConstraintSolver solver = new com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver();
        btDiscreteDynamicsWorld world = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfig);
        world.setGravity(new Vector3(0, -9.8f, 0));
        return world;
    }

    private btCollisionShape createTerrainShape(ModelInstance terrainInstance) {
        Mesh mesh = terrainInstance.model.meshes.first();
        float[] vertices = new float[mesh.getNumVertices() * mesh.getVertexSize() / 4];
        mesh.getVertices(vertices);
        short[] indices = new short[mesh.getNumIndices()];
        mesh.getIndices(indices);

        btTriangleMesh triangleMesh = new btTriangleMesh();
        for (int i = 0; i < indices.length; i += 3) {
            Vector3 v0 = new Vector3();
            Vector3 v1 = new Vector3();
            Vector3 v2 = new Vector3();

            int idx0 = indices[i] * (mesh.getVertexSize() / 4);
            int idx1 = indices[i + 1] * (mesh.getVertexSize() / 4);
            int idx2 = indices[i + 2] * (mesh.getVertexSize() / 4);

            v0.set(vertices[idx0], vertices[idx0 + 1], vertices[idx0 + 2]);
            v1.set(vertices[idx1], vertices[idx1 + 1], vertices[idx1 + 2]);
            v2.set(vertices[idx2], vertices[idx2 + 1], vertices[idx2 + 2]);

            triangleMesh.addTriangle(v0, v1, v2);
        }

        return new btBvhTriangleMeshShape(triangleMesh, true);
    }

    private btRigidBody createRigidBody(btCollisionShape shape, float mass, ModelInstance instance) {
        Vector3 localInertia = new Vector3(0, 0, 0);
        if (mass != 0f) shape.calculateLocalInertia(mass, localInertia);
        btRigidBody.btRigidBodyConstructionInfo info = new btRigidBody.btRigidBodyConstructionInfo(mass, null, shape, localInertia);
        btRigidBody body = new btRigidBody(info);
        body.setWorldTransform(instance.transform);
        return body;
    }

    Vector3 ballPosition = new Vector3();
    final Vector3 currentPosition = new Vector3();


    boolean isColliding = false;
    @Override
    public void render (float delta){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

    // Update Camera controls
        firstPersonCameraController.update();

    // Ball gravity movement (falls down)
        ball.transform.getTranslation(ballPosition); // Get current ball position
    ballPosition.y -= delta; // Simulate gravity (fall down)

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            ballPosition.y +=10*delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            ballPosition.x +=delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            ballPosition.x -=delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            ballPosition.z -=delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            ballPosition.z +=delta;
        }
        if (!Colliding()) {
        ballPosition.y -=5*delta;
        ball.transform.setTranslation(ballPosition);

        }

    // Render all instances
        modelBatch.begin(Camera);
        modelBatch.render(instances, environment);
        modelBatch.end();
}

boolean Colliding(){
    float height = terrain.getHeightAtWorldCoord(currentPosition.x, currentPosition.z);
    ball.transform.getTranslation(currentPosition);
    System.out.println(height+" "+ currentPosition.y + " "+ (currentPosition.y<=height));
    return currentPosition.y<=height;
}

@Override
public void resize(int width, int height) {
    Camera.viewportWidth = width;
    Camera.viewportHeight = height;
    Camera.update();
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
    batch.dispose();
}
}
