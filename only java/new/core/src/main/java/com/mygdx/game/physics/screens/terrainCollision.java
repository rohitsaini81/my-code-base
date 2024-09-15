package com.mygdx.game.physics.screens;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.physics.etc.HeightMapTerrain;
import com.mygdx.game.physics.etc.Terrain;

import java.awt.*;

public class terrainCollision implements Screen {
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
    Terrain terrain;


    @Override
    public void show() {
        terrain = new HeightMapTerrain(new Pixmap(Gdx.files.internal("textures/heightmap.png")), 3f);
//        terrain = new HeightMapTerrain(new Pixmap(Gdx.files.internal("heightmapterrain.png")), 3f);

//        terrainScene = new Scene(terrain.getModelInstance());
        instances.add(terrain.getModelInstance());
    }

    public terrainCollision(){
        init();
        create();
        firstPersonCameraController = new FirstPersonCameraController(Camera);
        Gdx.input.setInputProcessor(firstPersonCameraController);
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
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        firstPersonCameraController.update();



        modelBatch.begin(Camera);
        modelBatch.render(instances, environment);
        modelBatch.end();
        instances.get(0).transform.rotate(0,1,0,1);
        ball.transform.translate(0f, -delta, 0f);

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
}
