package com.mygdx.game.shooter.SCREENS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.shooter.SHAPES.custom_models;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

import java.util.Arrays;
import java.util.Objects;

import static com.badlogic.gdx.Gdx.input;

public class something implements Screen, AnimationController.AnimationListener {
//    player objects
    public Matrix4 playerTransform = new Matrix4();
    public final Vector3 moveTranslation = new Vector3();
    public final Vector3 currentPosition = new Vector3();
    public static Scene playerScene; // main model object, changing behaviour like position animation so on

//    [|----

    class Settings{
        public final float CAMERA_START_PITCH = 1f;
        public final float CAMERA_MIN_PITCH = CAMERA_START_PITCH-20f;
        public final float CAMERA_MAX_PITCH = CAMERA_START_PITCH;
        public final float CAMERA_PITCH_FACTOR = 0.3f;
        public final float CAMERA_ZOOM_LEVEL_FACTOR = 0.3f;
        public final float CAMERA_ANGLE_AROUND_PLAYER_FACTOR = 0.2f;
        public final float CAMERA_MIN_DISTANCE_FROM_PLAYER = 4;
    }
    enum CameraMode{
        FREE_LOOK,
        BEHIND_PLAYER
    }
//    CameraMode CameraMode;
    Settings settings = new Settings();;
    // Camera
    private PerspectiveCamera camera;


    private CameraMode cameraMode = CameraMode.BEHIND_PLAYER;
    private float cameraPitch= settings.CAMERA_START_PITCH;
    private float distanceFromPlayer = 4;
    private float angleAroundPlayer = 0f;
    private float angleBehindPlayer = 0f;


    public something(){
        camera = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        DefaultShader.Config config = new DefaultShader.Config();
        config.numBones = 64; // Increase the number of bones to 64
        sceneManager = new SceneManager(config.numBones);

    }
    private void updateCamera(){
        float horDistance = calculateHorizontalDistance(distanceFromPlayer);
        float virDistance = calculateVerticalDistance (distanceFromPlayer);

        calculatePitch();
        calculateAngleAroundPlayer();
        calculateCameraPosition(currentPosition,horDistance,virDistance);

        camera.up.set(Vector3.Y);
        camera.lookAt(currentPosition);
        camera.update();
    }

    private void calculateCameraPosition(Vector3 currentPosition,float horDistance, float verDistance){
        float offsetX = (float) (horDistance * Math.sin(Math.toRadians(angleAroundPlayer)));
        float offsetZ = (float) (horDistance * Math.cos(Math.toRadians(angleAroundPlayer)));

        camera.position.x = currentPosition.x - offsetX;
        camera.position.z = currentPosition.z - offsetZ;
        camera.position.y = currentPosition.y - verDistance;
//        camera.position.y = currentPosition.y+5;

    }

    private void calculateAngleAroundPlayer(){
        if (cameraMode==CameraMode.FREE_LOOK){
            float angleChange = Gdx.input.getDeltaX()*settings.CAMERA_ANGLE_AROUND_PLAYER_FACTOR;
            angleAroundPlayer-=angleChange;
        }
        else {
            angleAroundPlayer = angleBehindPlayer;
        }
    }

    private void calculatePitch(){
        float pitchChange =-input.getDeltaX()*settings.CAMERA_PITCH_FACTOR;
        cameraPitch-=pitchChange;
        if (cameraPitch<settings.CAMERA_MIN_PITCH){
            cameraPitch = settings.CAMERA_MIN_PITCH;
        }
        else if (cameraPitch>settings.CAMERA_MAX_PITCH){
            cameraPitch = settings.CAMERA_MAX_PITCH;
        }
    }
    private float calculateVerticalDistance(float distanceFromPlayer){
        return (float) (distanceFromPlayer*Math.sin(Math.toRadians(cameraPitch)));
    }
    private float calculateHorizontalDistance(float distanceFromPlayer){
        return (float) (distanceFromPlayer*Math.cos(Math.toRadians(cameraPitch)));
    }





//    ----|]

    float velocity = 0;
    float gravity=0.3f;
    float playerX,playerY=30,playerZ;
    boolean isJumping=false;
    float jumpAnimationTimer = 0; // Timer to track the delay
    float playerSpeed=30f;
    float playerRotationSpeed=80f;
    void processInput(float deltaTime){
        playerTransform.set(playerScene.modelInstance.transform);
        if (input.isKeyJustPressed(Input.Keys.Q)){
            Gdx.app.exit();
        }
        if (input.isKeyPressed(Input.Keys.W)){
            if (!isJumping){playerScene.animationController.action("running", 1, 1f,this, 0f);}
            moveTranslation.z+=playerSpeed*deltaTime;
        }

        if (input.isKeyPressed(Input.Keys.S)){
            if (!isJumping){playerScene.animationController.action("running", 2, 1f,this, 0.5f);}
            moveTranslation.z-=playerSpeed*deltaTime;
        }

        if (input.isKeyPressed(Input.Keys.A)){
//            if (playerY<=30){playerScene.animationController.action("running", 1, 1f,this, 0.5f);}
            playerTransform.rotate(Vector3.Y,playerRotationSpeed*deltaTime);
            angleBehindPlayer +=playerRotationSpeed*deltaTime;
        }
        if (input.isKeyPressed(Input.Keys.D)){
//            if (playerY<=30){playerScene.animationController.action("running", 1, 1f,this, 0.5f);}
            playerTransform.rotate(Vector3.Y,-playerRotationSpeed*deltaTime);
            angleBehindPlayer -= playerRotationSpeed*deltaTime;
        }
        if (input.isButtonPressed(Input.Buttons.LEFT) || input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT)){
            playerScene.animationController.action("punch", 1, 1f, this, 0.5f);
        }
        if (input.isKeyJustPressed(Input.Keys.SPACE) && playerY <= 30) {  // Can only jump if on the ground
            playerScene.animationController.action("jump", 1, 1f, this, 0.5f);
        }

        playerTransform.translate(moveTranslation);
        playerScene.modelInstance.transform.set(playerTransform);

//        update vector position
        playerScene.modelInstance.transform.getTranslation(currentPosition);
        currentPosition.y=32.65f;

        System.out.println(currentPosition);
        moveTranslation.set(0,0,0);
    }


    FirstPersonCameraController firstPersonCameraController;
    SceneAsset sceneAsset;
    public static SceneManager sceneManager; // for rendering all models at once





    @Override
    public void show() {
        __init();
        __init_models();
        __player_init();
        __terrain_init();
        sceneManager.setCamera(camera);
        System.out.println("Game is running");

    }
    private void __init_models(){
        sceneAsset = new GLTFLoader().load(Gdx.files.internal("player/player.gltf"));
        playerScene = new Scene(sceneAsset.scene);
        sceneManager.addScene(playerScene);
        custom_models.set_model();
        playerScene.animationController.setAnimation("idle", -1);
    }
    private void __init(){
        camera.lookAt(0f,0f,0f);
        camera.position.set(0f,10f,0);
        camera.near=1f;
        camera.far=200f;
        camera.update();
        Gdx.input.setCursorCatched(true);
        DirectionalLightEx light;
        light = new DirectionalLightEx();
        light.direction.set(1, -3, 1).nor();
        light.color.set(Color.WHITE);
        sceneManager.environment.add(light);
        // setup quick IBL (image based lighting)
        Cubemap diffuseCubemap;
        Cubemap specularCubemap;
        Texture brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();
        sceneManager.setAmbientLight(1f);
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));


        // Dispose
//        environmentCubemap.dispose();
        diffuseCubemap.dispose();
        specularCubemap.dispose();
        brdfLUT.dispose();
//        skybox.dispose();

        firstPersonCameraController = new FirstPersonCameraController(camera);
        Gdx.input.setInputProcessor(firstPersonCameraController);

    }
    private void __player_init(){

        moveTranslation.y=1;
        playerScene.modelInstance.transform.setToTranslation(playerX,playerY,playerZ);

    }
    Scene terrrainScene;
    private void __terrain_init(){
        sceneAsset = new GLTFLoader().load(Gdx.files.internal("player/terrains/1/my_terrain.gltf"));
        terrrainScene = new Scene(sceneAsset.scene);
        sceneManager.addScene(terrrainScene);
//        terrrainScene.animationController.setAnimation("idle", -1);
    }



    @Override
    public void render(float delta) {
//        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        processInput(delta);
        updateCamera();
//        firstPersonCameraController.update();
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
        sceneManager.dispose();
        sceneAsset.dispose();
//        environmentCubemap.dispose();
//        diffuseCubemap.dispose();
//        specularCubemap.dispose();
//        brdfLUT.dispose();
//        skybox.dispose();
    }

    @Override
    public void onEnd(AnimationController.AnimationDesc animation) {
        System.out.println("animation : "+animation.animation.id);
        if (animation.animation.id.equals("jump")) {
            playerScene.animationController.action("jump", 1, -1f, null, 0.5f); // Speed -1 for reverse
        }
    }

    @Override
    public void onLoop(AnimationController.AnimationDesc animation) {

    }
}
