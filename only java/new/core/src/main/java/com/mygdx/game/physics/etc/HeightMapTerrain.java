package com.mygdx.game.physics.etc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.physics.bullet.collision.btHeightfieldTerrainShape;
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

    int height;

    public HeightMapTerrain(Pixmap data, float magnitude) {
        this.width = data.getWidth();
        height = data.getHeight();
        this.size = width;

        this.heightMagnitude = magnitude;
        heightData = new float[width * height];


        field = new HeightField(true, data, true, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
        data.dispose();
        field.corner00.set(0, 0, 0);
        field.corner10.set(size, 0, 0);
        field.corner01.set(0, 0, size);
        field.corner11.set(size, 0, size);
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


//    private btHeightfieldTerrainShape terrainShape;

    private void createPhysicsTerrain() {
        boolean flipQuadEdges = false;

        // Convert the float[] to a FloatBuffer
        FloatBuffer heightBuffer = BufferUtils.newFloatBuffer(heightData.length);
        heightBuffer.put(heightData);
        heightBuffer.flip();  // Prepare the buffer for reading

        terrainShape = new btHeightfieldTerrainShape(
            this.width,            // Width of the heightmap
            this.height,           // Height of the heightmap
            heightBuffer,          // FloatBuffer for height data
            1f,                    // Height scale
            0f,                    // Minimum height
            heightMagnitude,       // Maximum height
            1,                     // Up axis (1 means Y-axis is up in Bullet)
            flipQuadEdges          // Whether to flip quad edges
        );
    }


        private float[] extractHeightData (Pixmap data, float magnitude){
            int width = data.getWidth();
            int height = data.getHeight();
            float[] heightData = new float[width * height];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = data.getPixel(x, y);
                    float gray = ((pixel >> 24) & 0xff) / 255.0f;  // Extract grayscale value (height)
                    heightData[x + y * width] = gray * magnitude;  // Scale height by magnitude
                }
            }
            return heightData;
        }


        @Override
        public void dispose () {
            field.dispose();
        }
    }

