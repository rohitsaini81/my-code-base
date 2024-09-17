package com.mygdx.game.physics.etc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btHeightfieldTerrainShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.BufferUtils;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;

import java.nio.FloatBuffer;

/**
 * @author JamesTKhan
 * @version August 07, 2022
 */
public class HeightMapTerrain extends Terrain {

    private final HeightField field;
    private float[] heightData;  // Array to store height values
    public static btHeightfieldTerrainShape terrainShape;
    private static final Vector3 c00 = new Vector3();
    private static final Vector3 c01 = new Vector3();
    private static final Vector3 c10 = new Vector3();
    private static final Vector3 c11 = new Vector3();


    public HeightMapTerrain(Pixmap data, float magnitude) {

        this.width = data.getWidth();
        this.height = data.getHeight();


        this.heightMagnitude = magnitude;
        heightData = new float[width * height];


        field = new HeightField(true, data, true, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
        data.dispose();
        field.corner00.set(0, 0, 0);
        field.corner10.set(height, 0, 0);
        field.corner01.set(0, 0, height);
        field.corner11.set(height, 0, height);
        field.magnitude.set(0f, magnitude, 0f);
        field.update();

        Texture texture = new Texture(Gdx.files.internal("textures/sand.png"), true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);


        PBRTextureAttribute textureAttribute = PBRTextureAttribute.createBaseColorTexture(texture);
        textureAttribute.scaleU = 40f;
        textureAttribute.scaleV = 40f;

        Material material = new Material();
        material.set(textureAttribute);

        ModelBuilder mb = new ModelBuilder();
        mb.begin();
        mb.part("terrain", field.mesh, GL20.GL_TRIANGLES, material);
        modelInstance = new ModelInstance(mb.end());
    }


//    public HeightMapTerrain(ModelInstance minstance, HeightField field){
//        this.field = field;
//
//        Mesh mesh = minstance.model.meshes.first();
//        float[] Vertices = new float[mesh.getNumVertices()*mesh.getVertexSize()/4];
//        float MinX = Float.MAX_VALUE,maxX=Float.MIN_VALUE;
//        float MinY = Float.MAX_VALUE,maxY=Float.MIN_VALUE;
//        float MinZ = Float.MAX_VALUE,maxZ = Float.MIN_VALUE;
//
////        modelInstance2=minstance;
//    }


    @Override
    public float getHeightAtWorldCoord(float worldX, float worldZ) {
        // Convert world coordinates to a position relative to the terrain
        modelInstance.transform.getTranslation(c00);
        float terrainX = worldX - c00.x;
        float terrainZ = worldZ - c00.z;

        // The size between the vertices
        float gridSquareSize = height / ((float) width - 1);

        // Determine which grid square the coordinates are in
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

        // Validates the grid square
        if (gridX >= width - 1 || gridZ >= width - 1 || gridX < 0 || gridZ < 0) {
            return 0;
        }

        // Determine where on the grid square the coordinates are
        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;

        // Determine the triangle we are on and apply barrycentric.
        float Height;
        if (xCoord <= (1 - zCoord)) { // Upper left triangle
            Height = barryCentric(
                c00.set(0, field.data[gridZ * width + gridX], 0),
                c10.set(1, field.data[gridZ * width + (gridX + 1)], 0),
                c01.set(0, field.data[(gridZ + 1) * width + gridX], 1),
                new Vector2(xCoord, zCoord));
        } else {
            Height =  barryCentric(
                c10.set(1, field.data[gridZ * width + (gridX + 1)], 0),
                c11.set(1, field.data[(gridZ + 1) * width + (gridX + 1)], 1),
                c01.set(0, field.data[(gridZ + 1) * width + gridX], 1),
                new Vector2(xCoord, zCoord));
        }

        return Height * heightMagnitude;
    }

    public static float barryCentric(Vector3 p1, Vector3 p2, Vector3 p3, Vector2 pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }





    private btRigidBody terrainBody;
    private btDiscreteDynamicsWorld dynamicsWorld;

    private void createPhysicsTerrain() {
        boolean flipQuadEdges = false;

        // Convert the float[] to a FloatBuffer
        FloatBuffer heightBuffer = BufferUtils.newFloatBuffer(heightData.length);
        heightBuffer.put(heightData);
        heightBuffer.flip();  // Prepare the buffer for reading

        // Create the heightfield terrain shape
        terrainShape = new btHeightfieldTerrainShape(
            this.width,            // Width of the heightmap
            this.height,           // Height of the heightmap
            heightBuffer,          // FloatBuffer for height data
            1f,                    // Height scale
            0f,                    // Minimum height
            heightMagnitude,       // Maximum height
            1,                     // Up axis (Y-axis is up)
            flipQuadEdges          // Whether to flip quad edges
        );

        // Create a rigid body for the terrain
        btRigidBody.btRigidBodyConstructionInfo bodyInfo = new btRigidBody.btRigidBodyConstructionInfo(
            0,                      // Mass of 0 means the terrain is static
            null,                   // No motion state
            terrainShape,           // Collision shape
            Vector3.Zero            // No local inertia (static object)
        );
        terrainBody = new btRigidBody(bodyInfo);
        dynamicsWorld.addRigidBody(terrainBody);  // Add the terrain to the physics world
    }



        @Override
        public void dispose () {
            field.dispose();
        }
    }

