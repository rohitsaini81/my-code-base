package com.mygdx.game.shooter.SHAPES;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.scene.Scene;

import static com.mygdx.game.shooter.SCREENS.something.sceneManager;

public class custom_models {
    public static void set_model(){
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        for (int x = 0; x < 100; x+=10) {
            for (int z = 0; z < 100; z+=10) {
                Material material = new Material();
                Color stonecolor = new Color(141/255f,78/255f,44/255f, 1f);
                material.set(PBRColorAttribute.createBaseColorFactor(stonecolor));
                MeshPartBuilder builder = modelBuilder.part(x+","+z, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position| VertexAttributes.Usage.Normal,material);
                BoxShapeBuilder.build(builder,x,0f,z,1f,1f,1f);
            }
        }
        ModelInstance modelInstance = new ModelInstance(modelBuilder.end());
//        PlayerScene.modelInstance.model.materials.size=50;
        sceneManager.addScene(new Scene(modelInstance));
    }
}
