package Game;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.input.*;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.scene.shape.Line;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.ui.Picture;
import com.jme3.input.FlyByCamera;

public class MyGameState extends AbstractAppState {

	private SimpleApplication app;
	private FlyByCamera flyCam;
	private Camera cam;
	private Node rootNode;
	private AssetManager assetManager;
	private InputManager inputManager;
	private final static String MAPPING_SHOOT = "Shoot";
	private final static String MAPPING_MOVE_RIGHT= "Move Right";
	private final static String MAPPING_MOVE_LEFT= "Move Left";
	private final static String MAPPING_MOVE_UP= "Move Up";
	private final static String MAPPING_MOVE_DOWN= "Move Down";
	private final static String MAPPING_SHOOT_KEY = "Shoot Key";
	private float speedX = 0.0f;
	private float speedY = 0.0f;
	private float ballSpeed = 0.0f;
	// create collision parameters
	//private CollisionResults results0 = new CollisionResults();
	private CollisionResults results1 = new CollisionResults();

	// check for collision
	private Vector3f min = new Vector3f(-70.0f,-70.0f,0.0f);
	private Vector3f max = new Vector3f(70.0f,70.0f,0.0f);
	private BoundingBox bb = new BoundingBox(min, max);
	// guide class field 
	private BitmapText distanceText;
	protected BitmapFont guiFont;

	@SuppressWarnings("deprecation")
	private static Box mesh = new Box(Vector3f.ZERO, 1, 1, 1);

	private int counter=0;
	private Camera camera;
	private Node guiNode;
	public int getCounter() { 
		return counter; 
	}

	@Override
	public void update(float tpf) {

		rootNode.getChild("player").move(speedX,speedY,0);
		rootNode.getChild("shot").move(0.0f,ballSpeed,0.0f);

		// check for intersection 
		rootNode.getChild("shot").collideWith(bb,results1);


		//System.out.println(closest.getDistance());
		//System.out.println("What was hit? " + closest.getGeometry().getName() );

		//System.out.println(results1.getClosestCollision().getDistance());
		//System.out.println(results1.size());
		if (results1.size() <= 6){
			//System.out.println("Out of Bounds!!");
			rootNode.getChild("shot").getLocalTranslation().set(0.0f, 0.0f, 0.0f);
			ballSpeed = 0;
		}
		//rootNode.getChild("player").move(0.01f, 0.0f, 0.0f); // displays heiarchal transformations
		//rootNode.getChild("shot").move(0.0f, 0.1f, 0.0f);
		results1.clear();

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
		this.camera = this.app.getCamera();
		this.flyCam = this.app.getFlyByCamera();
		this.guiNode = this.app.getGuiNode();
		//guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");

		// turn off default gui
		this.app.setDisplayStatView(false);
		this.app.setDisplayFps(false);
		
		// custom gui start
		// load and attach user interface frame
		Picture frame = new Picture("User interface frame");
	       frame.setImage(assetManager, "Interface/frame.png", false);
	       frame.move(camera.getWidth()/2-265, 0, -2);
	       frame.setWidth(530);
	       frame.setHeight(10);
	       guiNode.attachChild(frame);
	       
	       // load and attach logo of monkey 
	       Picture logo = new Picture("logo");
	       logo.setImage(assetManager, "Interface/Monkey.png", true);
	       logo.move(camera.getWidth()/2-47, 2, -1);
	       logo.setWidth(95);
	       logo.setHeight(75);
	       guiNode.attachChild(logo);


		// load font, initialize bitmap test obj and attach to gui node
		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		distanceText = new BitmapText(guiFont);
		distanceText.setSize(guiFont.getCharSet().getRenderedSize());
		distanceText.move(
				camera.getWidth()/2,         // X
				distanceText.getLineHeight(),  // Y
				0);                            // Z (depth layer)
		distanceText.setColor(ColorRGBA.White);
		//distanceText.move(settings.getWidth()/2+50, distanceText.getLineHeight()-20,0);
		guiNode.attachChild(distanceText);
		// end of custom gui code

	    /** A white, spot light source. */
	    PointLight lamp = new PointLight();
	    lamp.setPosition(new Vector3f(-1,1,15));
	    lamp.setColor(ColorRGBA.White);
	    rootNode.addLight(lamp); 
	    /* A white ambient light source */
	    AmbientLight ambient = new AmbientLight();
	    ambient.setColor(ColorRGBA.White);
	    rootNode.addLight(ambient);		
		
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
		final Trigger TRIGGER_SHOOT= new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
		// add mapping and listener
		inputManager.addMapping(MAPPING_SHOOT, TRIGGER_SHOOT);
		inputManager.addListener(analogListener, new String[]{MAPPING_SHOOT});

		//moving keybaord trigger
		final Trigger TRIGGER_SHOOT_KEY = new KeyTrigger(KeyInput.KEY_SPACE);
		// add mapping and listener
		inputManager.addMapping(MAPPING_SHOOT_KEY, TRIGGER_SHOOT_KEY);
		inputManager.addListener(actionListener, new String[]{MAPPING_SHOOT_KEY});


		//moving keybaord trigger
		final Trigger TRIGGER_MOVE_RIGHT = new KeyTrigger(KeyInput.KEY_RIGHT);
		// add mapping and listener
		inputManager.addMapping(MAPPING_MOVE_RIGHT, TRIGGER_MOVE_RIGHT);
		inputManager.addListener(actionListener, new String[]{MAPPING_MOVE_RIGHT});

		//moving keybaord trigger
		final Trigger TRIGGER_MOVE_LEFT = new KeyTrigger(KeyInput.KEY_LEFT);
		// add mapping and listener
		inputManager.addMapping(MAPPING_MOVE_LEFT, TRIGGER_MOVE_LEFT);
		inputManager.addListener(actionListener, new String[]{MAPPING_MOVE_LEFT});

		//moving keybaord trigger
		final Trigger TRIGGER_MOVE_UP = new KeyTrigger(KeyInput.KEY_UP);
		// add mapping and listener
		inputManager.addMapping(MAPPING_MOVE_UP, TRIGGER_MOVE_UP);
		inputManager.addListener(actionListener, new String[]{MAPPING_MOVE_UP});

		//moving keybaord trigger
		final Trigger TRIGGER_MOVE_DOWN= new KeyTrigger(KeyInput.KEY_DOWN);
		// add mapping and listener
		inputManager.addMapping(MAPPING_MOVE_DOWN, TRIGGER_MOVE_DOWN);
		inputManager.addListener(actionListener, new String[]{MAPPING_MOVE_DOWN});

		genBoundingBox();
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

	private void genBoundingBox(){

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
		//Material playerMat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
		Material playerMat= new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		//playerMat.setColor("Color", ColorRGBA.Red);
		playerMat.setBoolean("UseMaterialColors", true);
		playerMat.setColor("Ambient", ColorRGBA.Black);
		playerMat.setColor("Diffuse", ColorRGBA.Red);
		playerMat.setColor("Specular", ColorRGBA.White );
		playerMat.setFloat("Shininess", 100f); // [1,128]
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
		//playerNode.attachChild(shotNode);
		playerNode.attachChild(shotNode);
		rootNode.attachChild(playerNode);
	}


	private void makeCubes(int number) {
		for (int i = 0; i < number; i++) {
			// randomize 3D coordinates
			Vector3f loc = new Vector3f(
					FastMath.nextRandomInt(-20, 20),
					FastMath.nextRandomInt(-20, 20),
					0.0f);
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
			if (name.equals(MAPPING_SHOOT)) {

				rootNode.getChild("shot").move(0.0f, 0.1f, 0.0f);

			}// end if 
		}//end onAnalog 
	}; // end analogListener

	private ActionListener actionListener = new ActionListener() {
		public void onAction(String name, boolean isPressed, float tpf) {
			if (name.equals(MAPPING_MOVE_RIGHT) && !isPressed) {
				// implement action here
				speedX +=0.1f;
			}
			else if (name.equals(MAPPING_MOVE_LEFT) && !isPressed) {
				// implement action here
				speedX -=0.1f;
			}
			else if (name.equals(MAPPING_MOVE_UP) && !isPressed) {
				// implement action here
				speedY +=0.1f;
			}
			else if (name.equals(MAPPING_MOVE_DOWN) && !isPressed) {
				// implement action here
				speedY -=0.1f;
			}
			else if (name.equals(MAPPING_SHOOT_KEY) && !isPressed) {
				// implement action here
				ballSpeed = 1.0f;
			}

		}
	};

}