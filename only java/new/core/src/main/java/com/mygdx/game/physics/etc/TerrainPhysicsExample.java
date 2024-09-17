package com.mygdx.game.physics.etc;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.physics.bullet.collision.btHeightfieldTerrainShape;

import java.nio.FloatBuffer;

public class reversing {



    private float[] getHeightData(btHeightfieldTerrainShape shape) {
        int width = shape.getWidth();
        int height = shape.getHeight();
        float[] heightData = new float[width * height];
        FloatBuffer buffer = shape.getHeightData();

        // Extract height data from the buffer
        buffer.rewind();
        for (int i = 0; i < heightData.length; i++) {
            heightData[i] = buffer.get();
        }

        return heightData;
    }

    private Mesh createTerrainMesh(float[] heightData, int width, int height, float scale) {
        Mesh mesh = new Mesh(true, width * height * 6, width * height * 6,
            new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));

        float[] vertices = new float[width * height * 3];
        short[] indices = new short[(width - 1) * (height - 1) * 6];
        int index = 0;

        for (int z = 0; z < height - 1; z++) {
            for (int x = 0; x < width - 1; x++) {
                int i0 = z * width + x;
                int i1 = i0 + 1;
                int i2 = i0 + width;
                int i3 = i2 + 1;

                vertices[i0 * 3] = x * scale;
                vertices[i0 * 3 + 1] = heightData[i0];
                vertices[i0 * 3 + 2] = z * scale;

                vertices[i1 * 3] = (x + 1) * scale;
                vertices[i1 * 3 + 1] = heightData[i1];
                vertices[i1 * 3 + 2] = z * scale;

                vertices[i2 * 3] = x * scale;
                vertices[i2 * 3 + 1] = heightData[i2];
                vertices[i2 * 3 + 2] = (z + 1) * scale;

                vertices[i3 * 3] = (x + 1) * scale;
                vertices[i3 * 3 + 1] = heightData[i3];
                vertices[i3 * 3 + 2] = (z + 1) * scale;

                indices[index++] = (short) i0;
                indices[index++] = (short) i2;
                indices[index++] = (short) i1;
                indices[index++] = (short) i1;
                indices[index++] = (short) i2;
                indices[index++] = (short) i3;
            }
        }

        mesh.setVertices(vertices);
        mesh.setIndices(indices);

        return mesh;
    }

}
