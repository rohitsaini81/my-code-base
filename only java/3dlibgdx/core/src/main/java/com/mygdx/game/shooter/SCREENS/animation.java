package com.mygdx.game.shooter.SCREENS;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import net.mgsx.gltf.loaders.glb.GLBAssetLoader;
import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader;
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

import static com.badlogic.gdx.Gdx.input;

public class animation implements Screen,AnimationController.AnimationListener
{
    private SceneManager sceneManager;
    private SceneAsset sceneAsset;
    private Scene PlayerScene;
    private PerspectiveCamera camera;
    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private float time;
    private SceneSkybox skybox;
    private DirectionalLightEx light;
    private FirstPersonCameraController firstPersonCameraController;
    private CameraInputController cameraInputController;

//    Scene jay;

    public animation(){
        create();
    }
    public void create() {

        // create scene
//        sceneAsset = new GLTFLoader().load(Gdx.files.internal("obj/2/BASEmodel.gltf"));
        sceneAsset =  new GLTFLoader().load(Gdx.files.internal("obj/2/player.gltf"));

        PlayerScene = new Scene(sceneAsset.scene);
        DefaultShader.Config config = new DefaultShader.Config();
        config.numBones = 64; // Increase the number of bones to 64
//        ModelBatch modelBatch = new ModelBatch(new DefaultShaderProvider(config));

        sceneManager = new SceneManager(config.numBones);
//        sceneAsset =  new GLTFLoader().load(Gdx.files.internal("obj/2/player.gltf"));
//        jay = new Scene(sceneAsset.scene);

//        sceneManager.addScene(jay);
        sceneManager.addScene(PlayerScene);


//        AssetManager assetManager = new AssetManager();
//        assetManager.setLoader(SceneAsset.class, ".gltf", new GLTFAssetLoader());
//        assetManager.setLoader(SceneAsset.class, ".glb", new GLBAssetLoader());


        // setup camera (The BoomBox model is very small so you may need to adapt camera settings for your scene)
        camera = new PerspectiveCamera(60f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.near = 1f;
        camera.far = 200;
        sceneManager.setCamera(camera);
        camera.position.set(0,cameraHeight, 4f);

        firstPersonCameraController = new FirstPersonCameraController(camera);
        cameraInputController = new CameraInputController(camera);
        input.setInputProcessor(cameraInputController);

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

        PlayerScene.animationController.setAnimation("running", -1);
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

    // Camera
    float camHP=3;
    private float cameraHeight=camHP;
    private float cameraPitch=-camHP;

    private float speed = 5f;
    private float rotationSpeed = 80f;
    private Matrix4 playerTransform = new Matrix4();
    private final Vector3 moveTranslation = new Vector3();
    private final Vector3 currentPosition = new Vector3();
    void proccessInput(float deltaTime){
        //update the player transform
        playerTransform.set(PlayerScene.modelInstance.transform);
        if (input.isKeyPressed(Input.Keys.W)){moveTranslation.z+=speed*deltaTime;}
        if (input.isKeyPressed(Input.Keys.S)){moveTranslation.z-=speed*deltaTime;}
        if (input.isKeyPressed(Input.Keys.A)){playerTransform.rotate(Vector3.Y,rotationSpeed*deltaTime);}
        if (input.isKeyPressed(Input.Keys.D)){playerTransform.rotate(Vector3.Y,-rotationSpeed*deltaTime);}

        //apply the move translation to the transform
        playerTransform.translate(moveTranslation);
        // set the modified transform
        PlayerScene.modelInstance.transform.set(playerTransform);

//        update vector position
        PlayerScene.modelInstance.transform.getTranslation(currentPosition);
        moveTranslation.set(0,0,0);
    }
float ii=10f;
    @Override
    public void render(float delta) {
        time += delta;
        if (input.isKeyPressed(Input.Keys.L)){
            ii+=speed;
        }
//        firstPersonCameraController.update();
//        cameraInputController.update();
        proccessInput(delta);
        camera.position.set(currentPosition.x,cameraHeight,currentPosition.z-cameraPitch);
        camera.lookAt(currentPosition);
        camera.update();
        if (input.isKeyJustPressed(Input.Keys.SPACE)) {

            PlayerScene.animationController.action("jump", 1, 1f, this, 0.5f);
        }
        // render
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        sceneManager.update(delta);
        sceneManager.render();
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
    }

    @Override
    public void onEnd(AnimationController.AnimationDesc animation) {

    }

    @Override
    public void onLoop(AnimationController.AnimationDesc animation) {

    }
}
