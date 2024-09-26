package com.mygdx.game.games2d.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;
import org.apache.tools.ant.taskdefs.condition.Or;

public class game3d implements Screen {
    SpriteBatch batch;


    OrthographicCamera OrthoCamera;
    private SceneManager sceneManager;
    private SceneAsset sceneAsset;
    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private float time;
    private SceneSkybox skybox;
    private DirectionalLightEx light;


    Texture texture;


    Scene playerScene;
    Scene terrainScene;



    FitViewport fitViewport;
    float W2 = Gdx.graphics.getWidth();
    float H = Gdx.graphics.getHeight();

    PerspectiveCamera PCamera;
    FirstPersonCameraController firstPersonCameraController;
    ModelBatch modelBatch;
    Array<ModelInstance> instances;
    public game3d(){
        batch = new SpriteBatch();
        fitViewport = new FitViewport(500,700);
        OrthoCamera = new OrthographicCamera(W2/2,H/2);
        OrthoCamera.setToOrtho(false, W2/2 ,H/2);
        PCamera = new PerspectiveCamera();
        PCamera.position.set(10, 10, 10); // Set camera position
        PCamera.lookAt(0, 0, 0); // Set camera direction (look at origin)\
        PCamera.near = 10f;
        PCamera.far = 100f;
        firstPersonCameraController = new FirstPersonCameraController(PCamera);
        sceneManager = new SceneManager(50);
        modelBatch =  new ModelBatch();


        sceneAsset =new GLTFLoader().load(Gdx.files.internal("player/4/untitled11.gltf"));
        playerScene = new Scene(sceneAsset.scene);
        sceneAsset = new GLTFLoader().load(Gdx.files.internal("player/terrains/2/my_terrain.gltf"));
        terrainScene = new Scene(sceneAsset.scene);
        sceneManager.addScene(terrainScene);
        sceneManager.addScene(playerScene);

        instances = new Array<>();
        instances.add(playerScene.modelInstance);
        instances.add(terrainScene.modelInstance);

        texture = new Texture("snake.jpg");

        setSceneManager();
    }



    void setSceneManager(){

        sceneManager.setCamera(PCamera);


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
//        skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox); // sky or background of all things

    }

    Vector3 playerPosition;
    Matrix4 PlayerPosition;
    @Override
    public void show() {
        playerPosition = new Vector3();
        PlayerPosition = new Matrix4();
        playerPosition.set(0,-1,0);
//        PlayerPosition.setTranslation(playerPosition);
//        PlayerPosition.rotate(-5,0,0,180);
        playerScene.modelInstance.transform.setTranslation(playerPosition);

//        playerScene.animationController.setAnimation("idle");

//        playerPosition.rotate(Vector3.X,180);
//        playerScene.modelInstance.transform.setToRotation(playerPosition,180);



    }

    float playerSpeed=2;
    void processInput(float detaTime){
//        if (Gdx.input.isKeyPressed(Input.Keys.Q)){
//            Gdx.app.exit();
//        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            playerPosition.y+=playerSpeed*detaTime;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            playerPosition.x-=playerSpeed*detaTime;
//            playerScene.animationController.action("running", 2, 1f, this, 0.5f);

        }


        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            playerPosition.x+=playerSpeed*detaTime;
//            playerScene.animationController.action("running", 2, 1f,  this, 0.5f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            cx-=100*detaTime;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            cx+=100*detaTime;
        }
//        playerScene.modelInstance.transform.getTranslation(playerPosition);
//        PlayerPosition.setTranslation(playerPosition);
        OrthoCamera.position.set(cx,0f,0f);
//        OrthoCamera.update();
        System.out.println(OrthoCamera.position.x);
        playerScene.modelInstance.transform.setTranslation(playerPosition);


        System.out.println(playerPosition);

    }
    float cx=0;
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        processInput(delta);
        firstPersonCameraController.update();
//        OrthoCamera.update();
        instances.get(0).transform.setTranslation(0f,0f,0f);
        modelBatch.begin(PCamera);
        modelBatch.render(instances);
        modelBatch.end();
//        batch.setProjectionMatrix(OrthoCamera.combined);
        batch.begin();
        batch.draw(texture,0,0,50,50);
        batch.end();
        sceneManager.update(delta);
        sceneManager.render();
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

    }

//    @Override
//    public void onEnd(AnimationController.AnimationDesc animation) {
//
//    }

//    @Override
//    public void onLoop(AnimationController.AnimationDesc animation) {
//
//    }
}
