// app state method for sample project for education
package Game;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.material.Material;
import com.jme3.input.*;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.input.controls.* ;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.input.FlyByCamera;

public class CubeChaserState extends AbstractAppState {

	private SimpleApplication app;
	private FlyByCamera flyCam;
	private Camera cam;
	private Node rootNode;
	private AssetManager assetManager;
	private Ray ray = new Ray();
	private InputManager inputManager;
	private final static String MAPPING_ROTATE = "Rotate";

	@SuppressWarnings("deprecation")
	private static Box mesh = new Box(Vector3f.ZERO, 1, 1, 1);

	private int counter=0;
	public int getCounter() { 
		return counter; 
	}

	@Override
	public void update(float tpf) {
		// test for camera collision constantly
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
					System.out.println(target.getControl(CubeChaserControl.class).hello() + " and I am running away from " + cam.getLocation() );
					counter++;
				}
			} 
		}
	}        	  
	@Override
	public void cleanup(){}

	// intialize parameters from application file
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.app = (SimpleApplication) app;
		this.cam = this.app.getCamera();
		this.rootNode = this.app.getRootNode();
		this.assetManager = this.app.getAssetManager();
		this.inputManager = this.app.getInputManager();
		this.flyCam = this.app.getFlyByCamera();

		flyCam.setMoveSpeed(10f);

		// make cursor visible
		flyCam.setDragToRotate(true);
		inputManager.setCursorVisible(true);

		// Left buttom mouse trigger
		final Trigger TRIGGER_ROTATE = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);

		// add mapping and listener
		inputManager.addMapping(MAPPING_ROTATE, TRIGGER_ROTATE);
		inputManager.addListener(analogListener, new String[]{MAPPING_ROTATE});

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

	// method to generate spatials for scene
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

	// analog listener method
	private AnalogListener analogListener = new AnalogListener() {

		public void onAnalog(String name, float intensity, float tpf){
			if (name.equals(MAPPING_ROTATE)) {
				// create collision parameters
				CollisionResults results = new CollisionResults();
				Vector2f click2d = inputManager.getCursorPosition();
				//Vector2f click2d = new Vector2f( inputManager.getCursorPosition() );
				Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 0f);
				Vector3f dir = cam.getWorldCoordinates( new Vector2f(click2d.getX(), click2d.getY()), 1f).subtractLocal(click3d);
				Ray ray = new Ray(click3d, dir);

				// check for collision with mouse click location
				rootNode.collideWith(ray, results);
				if (results.size() > 0) {
					//System.out.println("Selection: Red Cube" );
					Geometry target = results.getClosestCollision().getGeometry();
					//if (target.getName().equals("Red Cube")) {
					//rootNode.rotate(0, 0, -intensity); // should not rotate entire rootNode but another node that holds children 
					target.rotate(0, 0, -intensity); // rotate left
					//} 
				} 
				// handle no collision 
				else {
					//System.out.println("Selection: Nothing" );
				}
			}// end if 
		}//end onAnalog 
	}; // end analogListener

}

