package com.mygdx.game.shooter.SCREENS;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
//import com.mygdx.game.shooter.Controls.CameraController;
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
    private AssetManager assetManager;
    private SceneAsset sceneAsset;
    private Scene scene;
    private Scene Ground;
    private Scene temp1;
    public PerspectiveCamera camera;
    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private float time;
    private SceneSkybox skybox;
    private DirectionalLightEx light;
    private FirstPersonCameraController firstPersonCameraController;
    private CameraInputController cameraInputController;
//    private CameraController cameraController;



    Array<ModelInstance> instances;
    Environment environment;
    ModelBatch modelBatch;
    Model model;
    ModelInstance ground;
    ModelInstance ball;


Scene animationscene;

    public LoadingScene(){
        SceneAsset sceneAsset;
        modelBatch = new ModelBatch();
        sceneAsset = new GLTFLoader().load(Gdx.files.internal("models/1/Alien Slime.gltf"));
        scene = new Scene(sceneAsset.scene);
//        scene.modelInstance.transform.setToTranslation(0,1,0);
        sceneAsset = new GLTFLoader().load(Gdx.files.internal("models/2/untitled.gltf"));
        Ground = new Scene(sceneAsset.scene);
        sceneAsset = new GLTFLoader().load(Gdx.files.internal("models/3/scene.gltf"));
//        sceneAsset = new GLTFLoader().load(Gdx.files.internal("player/terrains/2/my_terrain.gltf"));
        temp1 = new Scene(sceneAsset.scene);
        sceneAsset = new GLTFLoader().load(Gdx.files.internal("models/6test/BASEmodel.gltf"));
        animationscene = new Scene(sceneAsset.scene);
//        animationscene.animationController.setAnimation("fly");
        sceneManager = new SceneManager();
//        sceneManager.addScene(scene);
//        assetManager = new AssetManager();
//        assetManager.setLoader(SceneAsset.class, ".gltf", new GLTFAssetLoader());
//        assetManager.setLoader(SceneAsset.class, ".glb", new GLBAssetLoader());
//        assetManager.load("models/1/Alien Slime.gltf", SceneAsset.class);
        sceneManager.addScene(scene);
        sceneManager.addScene(Ground);
        sceneManager.addScene(temp1);

        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
//        sceneManager.setCamera(camera);
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
    SpriteBatch batch;
    BitmapFont fontx;
    BitmapFont fonty;
    BitmapFont fontz;
    @Override
    public void show(){
        batch = new SpriteBatch();
        fontx = new BitmapFont();
        fonty = new BitmapFont();
        fontz = new BitmapFont();
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(10f, 10f, 10f); // Camera positioned at (10, 10, 10)
        camera.lookAt(0, 0, 0);
        camera.near = 1f; // Near clipping plane
        camera.far = 300f; // Far clipping plane
        camera.update();
        sceneManager.setCamera(camera);
        cameraInputController = new CameraInputController(camera);
        firstPersonCameraController = new FirstPersonCameraController(camera);
//        mycontrol=new thirdpersoncontrol(camera);

        Gdx.input.setInputProcessor(cameraInputController);
        camera.position.set(5,5,5);
    }
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
        Ground.modelInstance.transform.setToTranslation(0,-0.8f,0);
        ground.transform.setToTranslation(0, -0.8f, 0);
//        camera.position.crs(x,y,z);
//
        instances = new Array<ModelInstance>();
        instances.add(ground);
        instances.add(ball);




    }


    float SPEED=2;


    float x,z,velocity=0;
    float y=0.2f;

    void jump(float del){
        y+=velocity;
    }


    float camx,camy,camz=0;
    float lookx,lookz,looky=0;
    @Override
    public void render(float delta) {


        time += delta;
        if (y<=0){y=0;} y+=velocity*delta;
        if (velocity<=-10){velocity=0;}else {velocity-=delta+0.3f;}


        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){velocity=5;}
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){z+=delta*SPEED;}
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){z-=delta*SPEED;}
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){x+=delta*SPEED;}
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){x-=delta*SPEED;}

        // render
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        cameraInputController.update();
//        firstPersonCameraController.update();

        sceneManager.update(delta);
        sceneManager.render();
        if (y<=0){scene.modelInstance.transform.setToTranslation(x,0,z);}
        else {scene.modelInstance.transform.setToTranslation(x,y,z);}

//        ground.transform.setToTranslation(x,y,z);


//        assetManager.update();


        modelBatch.begin(camera);
        modelBatch.render(instances);
        modelBatch.end();

        if (batch!=null){
            batch.begin();
            fontx.draw(batch,"lookx:"+lookx,0,700);
            fonty.draw(batch,"looky:"+looky,0,690);
            fontz.draw(batch,"lookz:"+lookz,0,680);
            fontx.setColor(Color.RED);
            fonty.setColor(Color.RED);
            fontz.setColor(Color.RED);
            batch.end();
        }
    }

    @Override
    public void resize(int width, int height) {

        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
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
        batch.dispose();
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

