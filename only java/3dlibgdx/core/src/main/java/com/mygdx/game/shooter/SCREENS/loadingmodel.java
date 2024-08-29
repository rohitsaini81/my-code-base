package com.mygdx.game.shooter.SCREENS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.shooter.Controls.movement;

import static com.mygdx.game.shooter.Main.player;

public class loadingmodel implements Screen {

    public PerspectiveCamera cam;
    public ModelBatch modelBatch;
    public Model model;
    ModelLoader loader;

    public AssetManager assets;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public Environment environment;
    public boolean loading;



//    public ModelInstance instance;
    public ModelBuilder modelBuilder;
    public CameraInputController camController;


    public loadingmodel(){

        modelBatch = new ModelBatch();
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.update();
        modelBuilder = new ModelBuilder();
        loader = new ObjLoader();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        assets = new AssetManager();
        assets.load("models1/1/ship.obj", Model.class);
        assets.finishLoading();
        loading = true;
    }
    @Override
    public void show() {

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f); // Camera positioned at (10, 10, 10)
        cam.lookAt(0, 0, 0);
        cam.near = 1f; // Near clipping plane
        cam.far = 300f; // Far clipping plane
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

    }


    private Model surfaceModel;
    private ModelInstance surfaceInstance;

    private void createWhiteSurface() {
        ModelBuilder modelBuilder = new ModelBuilder();
        surfaceModel = modelBuilder.createRect(
            -5f, 0, -5f,
            5f, 0, -5f,
            5f, 0, 5f,
            -5f, 0, 5f,
            0, -1, 0, // Normal pointing down
            new Material(ColorAttribute.createDiffuse(Color.WHITE)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );
        surfaceInstance = new ModelInstance(surfaceModel);
        surfaceInstance.transform.setToTranslation(0, 0, 0);

        instances.add(surfaceInstance);
    }
    private void doneLoading() {

        Model ship = assets.get("models1/1/ship.obj", Model.class);
        ModelInstance shipInstance = new ModelInstance(ship);
        ModelInstance shipInstance2 = new ModelInstance(ship);
        shipInstance2.transform.scl(0.5f);
        shipInstance2.transform.translate(5, 0, 0);
        instances.add(shipInstance);
        instances.add(shipInstance2);
        createWhiteSurface();
        loading = false;
    }



    float x,y=0;

    @Override
    public void render(float delta) {
        if (loading && assets.update()) {
            doneLoading();
        }

        movement.render(delta);
        if (x>10 || x<-10){x=0;}
        if (y>10 || y<-10){y=0;}
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            y+=delta*5;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            y-=delta*5;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            x-=delta*5;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            x+=delta*5;
        }
        cam.lookAt(x, y, 0);
        cam.update();






        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);



        modelBatch.begin(cam);
        modelBatch.render(instances, environment);
        modelBatch.end();
//        System.out.println("Player X,Y"+player.getX()+" "+player.getY());
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
        assets.dispose();
    }
}
