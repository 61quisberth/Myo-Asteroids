package Game;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.material.Material;
import com.jme3.input.*;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.scene.shape.Line;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.input.FlyByCamera;

public class MyGameState extends AbstractAppState {

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
		// check for colision with camera ray 
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

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.app = (SimpleApplication) app;
		this.cam = this.app.getCamera();
		this.rootNode = this.app.getRootNode();
		this.assetManager = this.app.getAssetManager();
		this.inputManager = this.app.getInputManager();
		this.flyCam = this.app.getFlyByCamera();

		Vector3f loc = new Vector3f(0.0f,0.0f,180f);
		cam.setLocation(loc);

		// 2d settings 
		/*
        cam.setParallelProjection(true);
        cam.setLocation(new Vector3f(0,0,0.5f));
        flyCam.setEnabled(false);
        */

		flyCam.setMoveSpeed(1f);

		// make cursor visible
		flyCam.setDragToRotate(true);
		inputManager.setCursorVisible(true);

		// Left buttom mouse trigger
		final Trigger TRIGGER_ROTATE = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);

		// add mapping and listener
		inputManager.addMapping(MAPPING_ROTATE, TRIGGER_ROTATE);
		inputManager.addListener(analogListener, new String[]{MAPPING_ROTATE});

		genRect();
		genPlayer();
		//makeCubes(10);
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

	private void genRect(){

		Line line0 = new Line(new Vector3f(-70.0f,70.0f,0.0f) ,new Vector3f(70.0f,70.0f,0.0f) );
		Line line1 = new Line(new Vector3f(70.0f,70.0f,0.0f) ,new Vector3f(70.0f,-70.0f,0.0f) );
		Line line2 = new Line(new Vector3f(-70.0f,-70.0f,0.0f) ,new Vector3f(70.0f,-70.0f,0.0f) );
		Line line3 = new Line(new Vector3f(-70.0f,-70.0f,0.0f) ,new Vector3f(-70.0f,70.0f,0.0f) );

		line0.setLineWidth(2.0f);
		line1.setLineWidth(2.0f);
		line2.setLineWidth(2.0f);
		line3.setLineWidth(2.0f);
	
		Geometry side0= new Geometry("side 0", line0);
		Geometry side1 = new Geometry("side 1", line1);
		Geometry side2 = new Geometry("side 2", line2);
		Geometry side3 = new Geometry("side 3", line3);

		Material side0m= new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

		side0m.setColor("Color", ColorRGBA.White);

		side0.setMaterial(side0m);                  
		side1.setMaterial(side0m);                  
		side2.setMaterial(side0m);                  
		side3.setMaterial(side0m);                  

		Node boundingBox = new Node("bounding box");
		
		boundingBox.attachChild(side0);
		boundingBox.attachChild(side1);
		boundingBox.attachChild(side2);
		boundingBox.attachChild(side3);
		
		rootNode.attachChild(boundingBox);
	}
	
	
	@SuppressWarnings("deprecation")
	private void genPlayer(){
		// create main character cube 
		Box playerBox = new Box(Vector3f.ZERO, 3, 3, 3);
		Geometry playerGeom = new Geometry("player cube", playerBox);
		Material playerMat = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		playerMat.setColor("Color", ColorRGBA.Red);
		// give the object the blue material
		playerGeom.setMaterial(playerMat);

		// create a shot cube 
		Vector3f shotPos = new Vector3f(0.0f,4.0f,0.0f);
		Box shotBox = new Box(shotPos, 1, 1, 1);
		// create an object from the mesh
		Geometry shotGeom = new Geometry("shot cube", shotBox);
		Material shotMat = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		shotMat.setColor("Color", ColorRGBA.White);
		// give the object the blue material
		shotGeom.setMaterial(shotMat);


		// scene graph, spatials, and root node attachment 
		Node playerNode = new Node("player");
		Node shotNode= new Node("shot");
		playerNode.attachChild(playerGeom);
		shotNode.attachChild(shotGeom);
		// exapmple method call for myBox
		//mainNode.attachChild(myBox("Blue Cube", new Vector3f(0, -1.5f, 0), ColorRGBA.Blue));
		playerNode.attachChild(shotNode);
		rootNode.attachChild(playerNode);
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

	// analog listener method
	private AnalogListener analogListener = new AnalogListener() {

		public void onAnalog(String name, float intensity, float tpf){
			if (name.equals(MAPPING_ROTATE)) {
				// create collision parameters
				CollisionResults results0 = new CollisionResults();
				CollisionResults results1 = new CollisionResults();
				Vector2f click2d = inputManager.getCursorPosition();
				//Vector2f click2d = new Vector2f( inputManager.getCursorPosition() );
				Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 0f);
				Vector3f dir = cam.getWorldCoordinates( new Vector2f(click2d.getX(), click2d.getY()), 1f).subtractLocal(click3d);
				Ray ray = new Ray(click3d, dir);

				
				// check for collision
				Vector3f min = new Vector3f(-70.0f,-70.0f,0.0f);
				Vector3f max = new Vector3f(70.0f,70.0f,0.0f);

				BoundingBox bb = new BoundingBox(min, max);

				// check for intersection 
				rootNode.getChild("shot").collideWith(bb,results1);

				rootNode.collideWith(ray, results0);
				if (results0.size() > 0) {
					//System.out.println("Selection: Red Cube" );
					Geometry target = results0.getClosestCollision().getGeometry();

					if (target.getName().equals("player cube")) {
						if (results1.size() <= 6)
							System.out.println("Out of Bounds!!");
						rootNode.getChild("player").move(0.01f, 0.0f, 0.0f); // displays heiarchal transformations
						rootNode.getChild("shot").move(0.0f, 0.1f, 0.0f);
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

