package Game;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

/** Sample 1 - how to get started with the most simple JME 3 application.
 * Display a blue 3D cube and view from all sides by
 * moving the mouse and pressing the WASD keys. */
public class TestMaterial extends SimpleApplication {

	public static void main(String[] args){
		TestMaterial app = new TestMaterial();
		app.start(); // start the game
	}

	@Override
	public void simpleInitApp() {
		flyCam.setMoveSpeed(15.0f);

		FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
		viewPort.addProcessor(fpp);
		BloomFilter bloom =
				new BloomFilter(BloomFilter.GlowMode.SceneAndObjects);
		fpp.addFilter(bloom);

		Node tank = (Node) assetManager.loadModel(
				"Tank/Tank.j3o");
		Material mat = new Material(
				assetManager, "Common/MatDefs/Light/Lighting.j3md");
		// loading diffuse, normal, specular, and glow texture maps
		TextureKey tankDiffuse = new TextureKey(
				"Tank/tank_diffuse.jpg", false);
		mat.setTexture("DiffuseMap",
				assetManager.loadTexture(tankDiffuse));
		TextureKey tankNormal = new TextureKey(
				"Tank/tank_normals.png", false);
		mat.setTexture("NormalMap",
				assetManager.loadTexture(tankNormal));
		TextureKey tankSpecular= new TextureKey(
				"Tank/tank_specular.jpg", false);
		mat.setTexture("SpecularMap",
				assetManager.loadTexture(tankSpecular));
		TextureKey tankGlow = new TextureKey(
				"Tank/tank_glow_map.jpg", false);
		mat.setTexture("GlowMap",
				assetManager.loadTexture(tankGlow));
		mat.setColor("GlowColor", ColorRGBA.Red);

		tank.setMaterial(mat);
		rootNode.attachChild(tank);

		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(1, 0, -2));
		sun.setColor(ColorRGBA.White);
		rootNode.addLight(sun);
		AmbientLight ambient = new AmbientLight();
		ambient.setColor(ColorRGBA.White);
		rootNode.addLight(ambient);
	}
}