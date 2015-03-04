// lighting test 
package Game;

import com.jme3.app.SimpleApplication;
import com.jme3.light.*;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

/** This demo shows a single-colored lit sphere using material color properties.
 */
public class MaterialColor extends SimpleApplication {

  public static void main(String[] args) {
    MaterialColor app = new MaterialColor();
    app.start();
  }

  @Override
  public void simpleInitApp() {
    Sphere sphereMesh = new Sphere(32,32, 1f);
    Geometry sphereGeo = new Geometry("Colored lit sphere", sphereMesh);
    Material sphereMat = new Material(assetManager, 
            "Common/MatDefs/Light/Lighting.j3md");
    sphereMat.setBoolean("UseMaterialColors", true);
    sphereMat.setColor("Ambient", ColorRGBA.Gray );
    sphereMat.setColor("Diffuse", ColorRGBA.Green);
    sphereMat.setColor("Specular", ColorRGBA.White );
    sphereMat.setFloat("Shininess", 20f); // [1,128]
    sphereGeo.setMaterial(sphereMat);
    rootNode.attachChild(sphereGeo); 
    
    /** Must add a light to make the lit object visible! */
   /* DirectionalLight sun = new DirectionalLight();
    sun.setDirection(new Vector3f(1, 0, -2));
    sun.setColor(ColorRGBA.White);
    rootNode.addLight(sun);*/
    
    /** A white, spot light source. */
    PointLight lamp = new PointLight();
    lamp.setPosition(new Vector3f(-3,3,5));
    lamp.setColor(ColorRGBA.White);
    rootNode.addLight(lamp); 
    
    AmbientLight ambient = new AmbientLight();
    ambient.setColor(ColorRGBA.White);
    rootNode.addLight(ambient);
    
  }
}
