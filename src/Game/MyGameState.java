// TODO: complete appstate before continuing with project 
/*
package Game;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.*; 
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.input.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
// implement action here
public class MyGameState extends AbstractAppState{

	private SimpleApplication app;
	private final Camera cam;
	private final Node rootNode;
	private AssetManager assetManager;
	private Ray ray = new Ray();
	private static Box mesh = new Box(Vector3f.ZERO, 1, 1, 1);	


	@Override
	public void update(float tpf) {

		CollisionResults results = new CollisionResults();
		Vector2f click2d = inputManager.getCursorPosition();
		//Vector2f click2d = new Vector2f( inputManager.getCursorPosition() );
		Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 0f);
		Vector3f dir = cam.getWorldCoordinates( new Vector2f(click2d.getX(), click2d.getY()), 1f).subtractLocal(click3d);
		Ray ray = new Ray(click3d, dir);

		// check for collision
		rootNode.collideWith(ray, results);
		if (results.size() > 0) {
			//System.out.println("Selection: Red Cube" );
			Geometry target = results.getClosestCollision().getGeometry();
			if (target.getName().equals("Red Cube")) {
				//rootNode.rotate(0, 0, -intensity); // should not rotate entire rootNode but another node that holds children 
				//target.rotate(0, 0, -intensity); // rotate left
				geom1.move(0.0f,0.01f,0.0f);
			} 
		} 
	}

	@Override
	public void cleanup() {}
	@Override
	public void initialize(AppStateManager stateManager,
			Application app) {
		super.initialize(stateManager, app);
		this.app = (SimpleApplication) app;
		this.cam = this.app.getCamera();
		this.rootNode = this.app.getRootNode();
		this.assetManager = this.app.getAssetManager();

	}
}
*/