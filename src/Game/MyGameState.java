package Game;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class MyGameState extends AbstractAppState {

	private SimpleApplication app;
	private Camera cam;
	private Node rootNode;
	private AssetManager assetManager;
	private Ray ray = new Ray();

	@SuppressWarnings("deprecation")
	private static Box mesh = new Box(Vector3f.ZERO, 1, 1, 1);



	@Override
	public void update(float tpf) {
		CollisionResults results = new CollisionResults();
		ray.setOrigin(cam.getLocation());
		ray.setDirection(cam.getDirection());
		rootNode.collideWith(ray, results);
		if (results.size() > 0) {
			Geometry target = results.getClosestCollision().getGeometry();
			if (target.getControl(CubeChaserControl.class) != null) {
				if (cam.getLocation().
						distance(target.getLocalTranslation()) < 10) {
					target.move(cam.getDirection());
				}
			} 
		}
	}        	  
	@Override
	public void cleanup() {}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.app = (SimpleApplication) app;
		this.cam = this.app.getCamera();
		this.rootNode = this.app.getRootNode();
		this.assetManager = this.app.getAssetManager();
		makeCubes(40);
	}

	// cube generator from textbook 
	public Geometry myBox(String name, Vector3f loc, ColorRGBA color){
		Geometry geom = new Geometry(name, mesh);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		geom.setMaterial(mat);
		geom.setLocalTranslation(loc);
		return geom;
	}

	private void makeCubes(int number) {
		for (int i = 0; i < number; i++) {
			// randomize 3D coordinates
			Vector3f loc = new Vector3f(
					FastMath.nextRandomInt(-20, 20),
					FastMath.nextRandomInt(-20, 20),
					FastMath.nextRandomInt(-20, 20));
			//rootNode.attachChild(myBox("Cube" + i, loc, ColorRGBA.randomColor()));
			Geometry geom = myBox("Cube" + i, loc, ColorRGBA.randomColor());
			if (FastMath.nextRandomInt(1, 4) == 4) {
				geom.addControl(new CubeChaserControl(cam, rootNode));
			}
			rootNode.attachChild(geom);
		} 
	}
}

