package com.mygdx.game.shooter.SCREENS.Bullet;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
//import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
//import com.badlogic.gdx.graphics.g3d.ModelBuilder;
import com.badlogic.gdx.utils.Array;
//import com.badlogic.gdx.graphics.g3d.environment.Environment;
//import com.badlogic.gdx.graphics.g3d.utils.ModelBatch;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.IntArray;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;


public class bullet1 implements Screen {
    private btDynamicsWorld dynamicsWorld;
    private btCollisionConfiguration collisionConfiguration;
    private btDispatcher dispatcher;
    private btBroadphaseInterface broadphase;
    private btSequentialImpulseConstraintSolver solver;
    SceneAsset sceneAsset;
    Scene terrain;
    SceneManager sceneManager;
    PerspectiveCamera camera;
    FirstPersonCameraController cameraController;
    public bullet1() {
        create();


        sceneAsset =  new GLTFLoader().load(Gdx.files.internal("player/terrains/2/my_terrain.gltf"));
        terrain = new Scene(sceneAsset.scene);
        sceneManager.addScene(terrain);
//        terrainInstance.model.meshes.first();
        Mesh terrainMesh = terrain.modelInstance.model.meshes.first();
        float[] vertices = new float[terrainMesh.getNumVertices() * terrainMesh.getVertexSize() / 4];
        terrainMesh.getVertices(vertices);
//        System.out.println("vertices"+vertices[8]);

        short[] indices = new short[terrainMesh.getNumIndices()];
        terrainMesh.getIndices(indices);
//        System.out.println(indices[0]);
        btTriangleMesh triangleMesh = new btTriangleMesh();




        for (int i = 0; i < indices.length; i += 3) {
            // Log the indices before proceeding
//            System.out.println("Processing indices: " + indices[i] + ", " + indices[i + 1] + ", " + indices[i + 2]);

            // Check for negative or out-of-bounds values
            if (indices[i] < 0 || indices[i + 1] < 0 || indices[i + 2] < 0) {
//                System.out.println("Negative index detected: " + indices[i] + ", " + indices[i + 1] + ", " + indices[i + 2]);
                continue; // Skip this triangle
            }

            int index0 = indices[i];
            int index1 = indices[i + 1];
            int index2 = indices[i + 2];

            int vertexCount = vertices.length / 3; // Total number of vertices

            // Check if the indices are within bounds
            if (index0 < vertexCount && index1 < vertexCount && index2 < vertexCount) {
                Vector3 v0 = new Vector3(vertices[index0 * 3], vertices[index0 * 3 + 1], vertices[index0 * 3 + 2]);
                Vector3 v1 = new Vector3(vertices[index1 * 3], vertices[index1 * 3 + 1], vertices[index1 * 3 + 2]);
                Vector3 v2 = new Vector3(vertices[index2 * 3], vertices[index2 * 3 + 1], vertices[index2 * 3 + 2]);

                // Add the triangle to the mesh
                triangleMesh.addTriangle(v0, v1, v2);
            } else {
                // Log the indices if they're out of bounds
                System.out.println("Invalid index (out of bounds): " + indices[i] + ", " + indices[i + 1] + ", " + indices[i + 2]);
            }
        }






        Bullet.init(); // Initialize Bullet Physics

        // Set up basic components of the physics world
        collisionConfiguration = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfiguration);
        broadphase = new btDbvtBroadphase();
        solver = new btSequentialImpulseConstraintSolver();

        // Create the dynamics world
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
        dynamicsWorld.setGravity(new Vector3(0, -9.81f, 0)); // Set gravity



        btBvhTriangleMeshShape terrainShape = new btBvhTriangleMeshShape(triangleMesh, true);

//        Step 4  Create a RigidBody for the Terrain
        btRigidBody.btRigidBodyConstructionInfo terrainBodyCI = new btRigidBody.btRigidBodyConstructionInfo(0, null, terrainShape, new Vector3(0, 0, 0));
        btRigidBody terrainBody = new btRigidBody(terrainBodyCI);

// Add the terrain to the dynamics world
        dynamicsWorld.addRigidBody(terrainBody);



//        Step 5: Create the Player Collision Shape and Body

        playerinit(dynamicsWorld);


//        Step 6: Update Physics and Collision

    }
    Matrix4 playerTransform = new Matrix4();
    btRigidBody playerBody;

    private ModelInstance playerInstance;


    void playerinit(btDynamicsWorld dynamicsWorld) {
            // Step 1: Create the cone mesh (visual representation of the player)
            ModelBuilder modelBuilder = new ModelBuilder();
            Model coneModel = modelBuilder.createCone(
                1f, 2f, 1f, 20,   // Width, Height, Depth, Divisions
                new Material(ColorAttribute.createDiffuse(Color.RED)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
            );
            playerInstance = new ModelInstance(coneModel);

            // Step 2: Create the collision shape (for physics)
            btCollisionShape playerShape = new btCapsuleShape(0.5f, 1.8f);  // Capsule shape for the player
            Vector3 playerInertia = new Vector3(0, 0, 0);
            playerShape.calculateLocalInertia(1.0f, playerInertia);  // Mass of 1.0f for the player

            // Step 3: Create the rigid body and set its initial position
            btRigidBody.btRigidBodyConstructionInfo playerBodyCI = new btRigidBody.btRigidBodyConstructionInfo(1.0f, null, playerShape, playerInertia);
            playerBody = new btRigidBody(playerBodyCI);
            playerBody.proceedToTransform(new Matrix4().setToTranslation(0, 10, 0));  // Start 10 units above the ground

            // Step 4: Add the rigid body to the dynamics world (physics)
            dynamicsWorld.addRigidBody(playerBody);
        }





        public btDynamicsWorld getDynamicsWorld() {
        return dynamicsWorld;
    }

    @Override
    public void show() {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        for (int x = 0; x < 100; x+=10) {
            for (int z = 0; z < 100; z+=10) {
                Material material = new Material();
                material.set(PBRColorAttribute.createBaseColorFactor(Color.WHITE));
                MeshPartBuilder builder = modelBuilder.part(x+","+z,GL20.GL_TRIANGLES,VertexAttributes.Usage.Position| VertexAttributes.Usage.Normal,material);
                BoxShapeBuilder.build(builder,x,0f,z,1f,1f,1f);
            }
        }
        ModelInstance modelInstance = new ModelInstance(modelBuilder.end());
//        PlayerScene.modelInstance.model.materials.size=50;
        sceneManager.addScene(new Scene(modelInstance));
    }



    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private SceneSkybox skybox;
    private DirectionalLightEx light;

    public void create(){


        // setup camera (The BoomBox model is very small so you may need to adapt camera settings for your scene)
        camera = new PerspectiveCamera(60f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.near = 1f;
        camera.far = 300;
        sceneManager = new SceneManager(80);
        sceneManager.setCamera(camera);
        camera.position.set(0,4f, 4f);
        cameraController = new FirstPersonCameraController(camera);
        Gdx.input.setInputProcessor(cameraController);



        // setup light
        light = new DirectionalLightEx();
        light.direction.set(1, -3, 1).nor();
        light.color.set(Color.WHITE);
        sceneManager.environment.add(light);

        // setup quick IBL (image based lighting)
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        environmentCubemap = iblBuilder.buildEnvMap(1024);
        diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();

        // This texture is provided by the library, no need to have it in your assets.
        brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        sceneManager.setAmbientLight(1f);
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

        // setup skybox
        skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox); // sky or background of all things

    }

    ModelBatch modelBatch = new ModelBatch();
    Vector3 vector = new Vector3();
    float playerspeed=50;

    private void processInput(float delta) {
        Vector3 velocity = new Vector3(); // Initialize movement vector

        // Handle jump (Space key for upward movement)
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            // Apply upward force to simulate jump if player is near the ground
            velocity.y = 5; // Change this value to control jump strength
        }

        // Horizontal movement
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            // Move forward (along the Z axis)
            velocity.z = -playerspeed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            // Move backward (along the Z axis)
            velocity.z = playerspeed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            // Move left (along the X axis)
            velocity.x = -playerspeed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            // Move right (along the X axis)
            velocity.x = playerspeed * delta;
        }

        // Apply the movement velocity to the player body
        playerBody.translate(velocity);

        // Reset the velocity vector for next frame
        velocity.set(0, 0, 0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);



        processInput(delta);
        sceneManager.update(delta);
        sceneManager.render();
        cameraController.update();
        dynamicsWorld.stepSimulation(delta, 5); // Step the physics world
        playerBody.getWorldTransform(playerInstance.transform);  // Sync the physics body to the model instance

        // Step 6: Render the player (use the modelBatch to render)
        modelBatch.begin(camera);  // Begin rendering
        modelBatch.render(playerInstance);  // Render the player
        modelBatch.end();

    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        dynamicsWorld.dispose();
        solver.dispose();
        broadphase.dispose();
        dispatcher.dispose();
        collisionConfiguration.dispose();
//        terrainBody.dispose();
//        terrainShape.dispose();
        playerBody.dispose();
//        playerShape.dispose();
        dynamicsWorld.dispose();
    }
}
