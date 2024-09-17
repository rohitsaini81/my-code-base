package com.mygdx.game.physics.etc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.physics.bullet.collision.btHeightfieldTerrainShape;
import com.badlogic.gdx.utils.BufferUtils;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

import java.nio.FloatBuffer;


public class GltfTerrainToHeightfield {
    private ModelInstance terrainInstance;
    private float[] heightData;
    private btHeightfieldTerrainShape terrainShape;


    SceneAsset sceneAsset;
    Scene terrainScene;
    // Load the GLTF model and generate heightfield shape
    public GltfTerrainToHeightfield(Scene scene,float heightScale) {
        // Load the terrain GLTF model

//        sceneAsset = new GLTFLoader().load(Gdx.files.internal(gltfFilePath));
        terrainScene = scene;
        terrainInstance =terrainScene.modelInstance;
        // Extract the mesh from the model
        extractHeightDataFromTerrain(terrainInstance);

        // Create the btHeightfieldTerrainShape
        createPhysicsTerrain(heightScale);
    }

    private void extractHeightDataFromTerrain(ModelInstance terrainInstance) {
        // Assuming a single mesh in the model
        if (terrainInstance.model.meshes.size == 0) return;
        Mesh terrainMesh = terrainInstance.model.meshes.first();

        // Extract vertex positions from the mesh
        int numVertices = terrainMesh.getNumVertices();
        int vertexSize = terrainMesh.getVertexSize() / 4; // Divide by 4 because the vertex size is in bytes
        int positionOffset = terrainMesh.getVertexAttribute(VertexAttributes.Usage.Position).offset / 4;


        // Create a buffer to hold the vertices
        float[] vertices = new float[numVertices * vertexSize];
        terrainMesh.getVertices(vertices);

        // Find the min and max X and Z coordinates and collect the Y (height) values
        float minX = Float.MAX_VALUE, maxX = Float.MIN_VALUE;
        float minZ = Float.MAX_VALUE, maxZ = Float.MIN_VALUE;

        heightData = new float[numVertices]; // To store Y-coordinates (height data)

        for (int i = 0; i < numVertices; i++) {
            int index = i * vertexSize + positionOffset;
            System.out.println("index : "+index);
            float x = vertices[index];
            float y = vertices[index + 1]; // Y is height
            float z = vertices[index + 2];

            // Update heightData with Y values
            heightData[i] = y;

            // Track the min and max X, Z values for terrain dimensions
            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
            if (z < minZ) minZ = z;
            if (z > maxZ) maxZ = z;
        }

        Gdx.app.log("Terrain", "Width (X): " + (maxX - minX) + ", Depth (Z): " + (maxZ - minZ));
    }

    private void createPhysicsTerrain(float heightScale) {
        // Convert the heightData to FloatBuffer
        FloatBuffer heightBuffer = BufferUtils.newFloatBuffer(heightData.length);
        heightBuffer.put(heightData);
        heightBuffer.flip();

        int width = (int) Math.sqrt(heightData.length);  // Assuming a square terrain grid
        int depth = width;  // Assuming width and depth are the same

        // Create the Bullet heightfield shape
        terrainShape = new btHeightfieldTerrainShape(
            width, depth,        // Width and depth of the terrain
            heightBuffer,        // FloatBuffer with height data
            heightScale,         // Height scaling factor
            0,                   // Minimum height (Y)
            1,                   // Maximum height (Y)
            1,                   // Up axis (1 for Y-up)
            false                // Flip quad edges if necessary
        );
    }

    public btHeightfieldTerrainShape getTerrainShape() {
        return terrainShape;
    }

    public void dispose() {
        sceneAsset.dispose();
        if (terrainShape != null) terrainShape.dispose();
    }
}
