//Game by Jason Quisberth 
//when setting up from scratch, create assets folder through eclipse 
package Game;

// required imports
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.* ;
import com.jme3.input.*;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.math.ColorRGBA;

public class MyGame extends SimpleApplication {

	// some static variables for the mapping and shot cube geometry
	private final static String MAPPING_ROTATE = "Rotate";
	private static Geometry geom1; // cube to be shot 
	//private Geometry geom;
	//private static Box mesh = new Box(Vector3f.ZERO,1.0f,1.0f,1.0f);

	public static void main(String[] args) {

		MyGame app = new MyGame();

		AppSettings settings = new AppSettings(true);
		// custom settings page 
		settings.setTitle("My Cool Game");
		// source the image where the splash is from 
		settings.setSettingsDialogImage("splash.png");
		app.setSettings(settings); // apply settings to app
		app.start(); // start the game

	}

	// cube generator from textbook 
	/*
	public Geometry myBox(String name, Vector3f loc, ColorRGBA color){
		Geometry geom = new Geometry(name, mesh);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		geom.setMaterial(mat);
		geom.setLocalTranslation(loc);
		return geom;
	}
	 */

	@SuppressWarnings("deprecation") // warning associated with depreciation suppresed 
	@Override
	public void simpleInitApp() {


		flyCam.setMoveSpeed(100f);
		//obtain camera info: default settings camera at 0,0,10, and rotated to look in the direction 0,0,-1
		//Vector3f loc = cam.getLocation();
		//Quaternion rot = cam.getRotation();
		
		// set camera location for game
		Vector3f camLoc = new Vector3f(0.0f,10.0f,80.0f);
		cam.setLocation(camLoc);
		//cam.setRotation(q);

		// Left buttom mouse trigger
		final Trigger TRIGGER_ROTATE = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);

		// make cursor visible
		flyCam.setDragToRotate(true);
		inputManager.setCursorVisible(true);

		// add mapping and listener
		inputManager.addMapping(MAPPING_ROTATE, TRIGGER_ROTATE);
		inputManager.addListener(analogListener, new String[]{MAPPING_ROTATE});

		// create main character cube 
		Box redBox = new Box(Vector3f.ZERO, 3, 3, 3);
		// create an object from the mesh
		Geometry geom = new Geometry("Red Cube", redBox);
		// create a simple blue material
		Material mat = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Red);
		// give the object the blue material
		geom.setMaterial(mat);

		// create a shot cube 
		Vector3f pos = new Vector3f(0.0f,4.0f,0.0f);
		Box shotBox = new Box(pos, 1, 1, 1);
		// create an object from the mesh
		geom1 = new Geometry("White Cube", shotBox);
		//Geometry geom1 = new Geometry("White Cube", shotBox);
		// create a simple blue material
		Material mat1 = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		mat1.setColor("Color", ColorRGBA.White);
		// give the object the blue material
		geom1.setMaterial(mat1);


		// scene graph, spatials, and root node attachment 
		Node mainNode = new Node("mainNode");
		mainNode.attachChild(geom);
		mainNode.attachChild(geom1);
		//mainNode.attachChild(myBox("Red Cube", new Vector3f(0,  1.5f, 0), ColorRGBA.Red));
		//mainNode.attachChild(myBox("Blue Cube", new Vector3f(0, -1.5f, 0), ColorRGBA.Blue));
		rootNode.attachChild(mainNode);
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
				// handle no collision 
				else {
					//System.out.println("Selection: Nothing" );
				}
			}// end if 
		}//end onAnalog 
	}; // end analogListener


}



