package com.mygdx.game.games2d.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

    public class tenis implements Screen {

        private World world;
        private Box2DDebugRenderer debugRenderer;
        private OrthographicCamera camera;

        private Body groundBody;
        private Body ballBody;

        public tenis(){
            create();
        }
        public void create() {
            // Create the physics world with gravity
            world = new World(new com.badlogic.gdx.math.Vector2(0, -9.8f), true);
            debugRenderer = new Box2DDebugRenderer();

            // Set up the camera for rendering
            camera = new OrthographicCamera(Gdx.graphics.getWidth() / 32f, Gdx.graphics.getHeight() / 32f);



// Create the top boundary (static body)
            BodyDef topBodyDef = new BodyDef();
            topBodyDef.position.set(0, camera.viewportHeight / 2f); // Corrected position for the top
            Body topBody = world.createBody(topBodyDef);

            PolygonShape topBox = new PolygonShape();
            topBox.setAsBox(camera.viewportWidth / 2f, 1); // Correct width and small height
            topBody.createFixture(topBox, 0.0f);
            topBox.dispose();

// Create the left boundary (static body)
            BodyDef leftBodyDef = new BodyDef();
            leftBodyDef.position.set(-camera.viewportWidth / 2f, 0); // Left wall at correct x-position
            Body leftBody = world.createBody(leftBodyDef);

            PolygonShape leftBox = new PolygonShape();
            leftBox.setAsBox(1, camera.viewportHeight / 2f); // Small width, tall height
            leftBody.createFixture(leftBox, 0.0f);
            leftBox.dispose();

// Create the right boundary (static body)
            BodyDef rightBodyDef = new BodyDef();
            rightBodyDef.position.set(camera.viewportWidth / 2f, 0); // Right wall at correct x-position
            Body rightBody = world.createBody(rightBodyDef);

            PolygonShape rightBox = new PolygonShape();
            rightBox.setAsBox(1, camera.viewportHeight / 2f); // Small width, tall height
            rightBody.createFixture(rightBox, 0.0f);
            rightBox.dispose();




            // Create the ground body at bottom
            BodyDef groundBodyDef = new BodyDef();
            groundBodyDef.position.set(0, -5); // Set ground position
            groundBody = world.createBody(groundBodyDef);

            PolygonShape groundBox = new PolygonShape();
            groundBox.setAsBox(50, 1); // Width = 50, Height = 1 (Thin ground surface)
            groundBody.createFixture(groundBox, 0.0f); // Static ground with no density
            groundBox.dispose();

            // Create the ball body
            BodyDef ballBodyDef = new BodyDef();
            ballBodyDef.type = BodyType.DynamicBody; // Dynamic body affected by gravity
            ballBodyDef.position.set(0, 5); // Start position of the ball
            ballBody = world.createBody(ballBodyDef);

            CircleShape ballShape = new CircleShape();
            ballShape.setRadius(1f); // Radius of the ball

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = ballShape;
            fixtureDef.density = 1.0f;
            fixtureDef.friction = 0.4f;
            fixtureDef.restitution = 0.9f; // Bounciness

            ballBody.createFixture(fixtureDef);
            ballShape.dispose();

            BodyDef tileBodyDef = new BodyDef();
            tileBodyDef.type = BodyDef.BodyType.StaticBody; // Static body
            tileBodyDef.position.set(5, -3); // Position of the tile
            tileBody = world.createBody(tileBodyDef);

            PolygonShape tileShape = new PolygonShape();
            tileShape.setAsBox(1.5f, 0.5f); // Width and height are 20x20 units
            tileBody.createFixture(tileShape, 0.0f);
            tileShape.dispose();
        }


        private Body tileBody; // Body for the tile
        @Override
        public void show() {
            jumpforce.y=100;
        }




        Vector2 jumpforce= new Vector2();
        private void processInput() {
            // Check if space is pressed and if the ball can jump
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                jumpforce.y+=100;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                tileBody.setTransform(tileBody.getPosition().x - 0.1f, tileBody.getPosition().y, tileBody.getAngle());//                jumpforce.x-=10;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                tileBody.setTransform(tileBody.getPosition().x + 0.1f, tileBody.getPosition().y, tileBody.getAngle());
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                tileBody.setTransform(tileBody.getPosition().x, tileBody.getPosition().y + 0.1f, tileBody.getAngle());
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                tileBody.setTransform(tileBody.getPosition().x, tileBody.getPosition().y - 0.1f, tileBody.getAngle());            }

            ballBody.applyLinearImpulse(jumpforce, ballBody.getWorldCenter(), true);
            jumpforce.set(0,0);

        }

        @Override
        public void render(float delta) {
            // Clear the screen
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            processInput();
            // Step the physics simulation
            world.step(1/60f, 6, 2);

            // Update the camera and render the physics world
            camera.update();
            debugRenderer.render(world, camera.combined);
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
            // Clean up the resources
            world.dispose();
            debugRenderer.dispose();
        }


        private class MyContactListener implements ContactListener {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                System.out.println(fixtureA.getBody());
                if (isBall(fixtureA) || isBall(fixtureB)) {
                    System.out.println("Ball collided with something!");

                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }

            private boolean isBall(Fixture fixture) {
            return fixture.getBody() == ballBody;
        }

        }
    }
