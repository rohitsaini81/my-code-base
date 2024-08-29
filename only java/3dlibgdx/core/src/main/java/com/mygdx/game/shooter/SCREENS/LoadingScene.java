package com.mygdx.game.shooter.SCREENS;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.utils.Array;
import net.mgsx.gltf.data.data.GLTFBuffer;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

public class LoadingScene implements Screen {
    private SceneManager sceneManager;
    private SceneAsset sceneAsset;
    private Scene scene;
    private Scene Ground;
    private PerspectiveCamera camera;
    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private float time;
    private SceneSkybox skybox;
    private DirectionalLightEx light;
    private FirstPersonCameraController cameraController;



    Array<ModelInstance> instances;
    Environment environment;
    ModelBatch modelBatch;
    Model model;
    ModelInstance ground;
    ModelInstance ball;




    public LoadingScene(){
        modelBatch = new ModelBatch();
        sceneAsset = new GLTFLoader().load(Gdx.files.internal("models/1/Alien Slime.gltf"));
        scene = new Scene(sceneAsset.scene);
        sceneAsset = new GLTFLoader().load(Gdx.files.internal("models/2/untitled.gltf"));
        Ground = new Scene(sceneAsset.scene);
        sceneManager = new SceneManager();
        sceneManager.addScene(scene);
        sceneManager.addScene(Ground);

        // setup camera (The BoomBox model is very small so you may need to adapt camera settings for your scene)
        camera = new PerspectiveCamera(60f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        float d = .02f;
        camera.near = d / 5000f;
//        camera.near = 500;
        camera.far = 200;
        sceneManager.setCamera(camera);
        camera.position.set(0,0.5f, 4f);

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
        sceneManager.setSkyBox(skybox);


        create();


    }

    @Override
    public void show(){}
    public void create() {
        ModelBuilder mb = new ModelBuilder();
        mb.begin();
        mb.node().id = "ground";
        mb.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED)))
            .box(5f, 1f, 5f);
        mb.node().id = "ball";
        mb.part("sphere", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN)))
            .sphere(1f, 1f, 1f, 10, 10);
        model = mb.end();
//
        ground = new ModelInstance(model, "ground");
        ball = new ModelInstance(model, "ball");
        ball.transform.setToTranslation(0, 3f, 0);
//
        instances = new Array<ModelInstance>();
//        instances.add(ground);
        instances.add(ball);

    }
float x,y,z=0;

    float SPEED=2;
    Vector3 PlayerPosition=new Vector3();
    Vector3 PlayerScale=new Vector3();
    Vector3 PlayerVelocity= new Vector3();



    private float gravity = -9.8f; // Adjust as needed
    private float jumpForce = 10f;
    @Override
    public void render(float delta) {

        time += delta;

        cameraController.update();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            PlayerVelocity.y += jumpForce;
        }

        PlayerVelocity.y += gravity * delta;

        if (PlayerPosition.y + PlayerVelocity.y * delta <= 1) {
            PlayerPosition.y = 1;
            PlayerVelocity.y = 0;
        }


        PlayerPosition.add(PlayerVelocity.scl(delta));

        // Update player model instance position
//        scene.modelInstance.transform.setToTranslation(PlayerPosition.x, PlayerPosition.y, PlayerPosition.z);


        if (Gdx.input.isKeyPressed(Input.Keys.UP)){z+=delta*SPEED;}
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){z-=delta*SPEED;}
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){x-=delta*SPEED;}
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){x+=delta*SPEED;}
//        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){y=2;}


//        scene.modelInstance.transform.rotate(Vector3.Y, 10f * delta);
        scene.modelInstance.transform.setToTranslation(x,y,z);



        scene.modelInstance.transform.getTranslation(PlayerPosition);
        System.out.println(scene.modelInstance.transform.getScale(PlayerScale));
//        System.out.println(PlayerPosition);

        // render
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        sceneManager.update(delta);
        sceneManager.render();
        modelBatch.begin(camera);
        modelBatch.render(instances);
        modelBatch.end();


    }

    @Override
    public void resize(int width, int height) {

        sceneManager.updateViewport(width, height);
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
        sceneManager.dispose();
        sceneAsset.dispose();
        environmentCubemap.dispose();
        diffuseCubemap.dispose();
        specularCubemap.dispose();
        brdfLUT.dispose();
        skybox.dispose();
        modelBatch.dispose();
        model.dispose();
    }

}

