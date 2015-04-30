/** 
 * main java project appstate
 * 2369861
 * @author Jason Quisberth 
 * @since Mar 05 2015
 * @version 1.0 Spring 2015
 */
package Game;

import Game.bulletControlState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.audio.AudioNode;
import com.jme3.bounding.BoundingBox;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.input.*;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.ui.Picture;
import com.jme3.input.FlyByCamera;

public class MyGameState extends AbstractAppState {

	private SimpleApplication app; // simple application
	private FlyByCamera flyCam; // fly cam
	private Camera cam; // standard camera
	private Node rootNode; // rootnode of scene graph
	private AssetManager assetManager; // asset manager 
	private InputManager inputManager; // input manager
	private Camera camera; // camera
	private long bulletCooldown;

	// key mappings
	private final static String MAPPING_MOVE_RIGHT= "Move Right"; // key mapping to move right
	private final static String MAPPING_MOVE_LEFT= "Move Left"; // key mapping to move left
	private final static String MAPPING_MOVE_UP= "Move Up"; // key mapping to move up 
	private final static String MAPPING_SHOOT_KEY = "Shoot Key"; // key mapping to shoot using keyboard

	// bound boxes
	private GameBoundingBox gbb;
	private BoundingBox bb;

	// player and shot motion parameters 
	private float playerDir = 5;
	private Vector3f Vel = new Vector3f(0.0f,0.0f,0.0f);
	private Vector3f acc = new Vector3f(0.0f,0.0f,0.0f);
	//private float ballSpeed = 0.0f;
	private float playerSpeed = 15.0f;
	private int numCubes = 10;
	//private Vector3f shotRot = new Vector3f(); 

	// collision results 
	private CollisionResults results2 = new CollisionResults(); // player + bb

	// node that is parent to the meteor geometies 
	private Node metNode = new Node("meteors");

	// node for shot
	private Node shotNode = new Node("shot");

	// guiNode class field 
	private BitmapText distanceText;
	protected BitmapFont guiFont;
	private Node guiNode; // gui node 

	// audio node required for sound effect
	private AudioNode audio_gun;
	private AudioNode audio_nature;


	// cube spatial for future asteroid spatial generation
	@SuppressWarnings("deprecation")
	private static Box mesh = new Box(Vector3f.ZERO, 1, 1, 1);

	private int counter=0; // iterator for debugging 

	public int getCounter() { 
		return counter; 
	}

	/**
	 *  @tpf float value related to frames per second used to keep game playable regardless of performance
	 */
	@Override
	public void update(float tpf) {

		// moving player and shot nodes for gameplay
		Vel.addLocal(acc.mult(10*tpf));
		rootNode.getChild("player").setLocalTranslation(rootNode.getChild("player").getLocalTranslation().add(Vel.mult(tpf)));

		rootNode.getChild("player").collideWith(bb,results2);

		acc = new Vector3f(0.0f,0.0f,0.0f);

		// check for ship out of bounds and reserve direction
		if (results2.size() <= 6){
			gbb.reflect(rootNode.getChild("player"),gbb.getClosestSide(rootNode.getChild("player").getLocalTranslation()));
		}

		results2.clear();
	}        	  


	/**
	 * method to occur during the closing of program to "clean up"
	 */
	@Override
	public void cleanup(){}

	/**
	 * Method to initialize parameters for display, input handling, and scene graph
	 * @param stateManager  AppStateManager to handle AppState behavior 
	 * @param app			Application to obtain rootNode, AssetManager, guiNode, etc. 
	 */
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		// defining class fields from app and stateManager
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

		// playing area bounding box
		gbb = new GameBoundingBox(assetManager);
		bb = gbb.initBoundingBox();

		initAudio();

		// initialize custom gui
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

		// set camera location
		Vector3f loc = new Vector3f(0.0f,0.0f,180f);
		cam.setLocation(loc);

		// flycam speed
		flyCam.setMoveSpeed(1f);

		// make cursor visible
		flyCam.setDragToRotate(true);
		inputManager.setCursorVisible(true);

		/* 
		 * 	Meteor node with meteor children
		 */
		makeCubes(numCubes);
		rootNode.attachChild(metNode);

		/*
		 * Keyboard Triggers start
		 */
		//shooting keybaord trigger
		final Trigger TRIGGER_SHOOT_KEY = new KeyTrigger(KeyInput.KEY_SPACE);
		// add mapping and listener
		inputManager.addMapping(MAPPING_SHOOT_KEY, TRIGGER_SHOOT_KEY);
		inputManager.addListener(analogListener, new String[]{MAPPING_SHOOT_KEY});


		//moving keybaord trigger
		final Trigger TRIGGER_MOVE_RIGHT = new KeyTrigger(KeyInput.KEY_RIGHT);
		// add mapping and listener
		inputManager.addMapping(MAPPING_MOVE_RIGHT, TRIGGER_MOVE_RIGHT);
		inputManager.addListener(analogListener, new String[]{MAPPING_MOVE_RIGHT});

		//moving keybaord trigger
		final Trigger TRIGGER_MOVE_LEFT = new KeyTrigger(KeyInput.KEY_LEFT);
		// add mapping and listener
		inputManager.addMapping(MAPPING_MOVE_LEFT, TRIGGER_MOVE_LEFT);
		inputManager.addListener(analogListener, new String[]{MAPPING_MOVE_LEFT});

		//moving keybaord trigger
		final Trigger TRIGGER_MOVE_UP = new KeyTrigger(KeyInput.KEY_UP);
		// add mapping and listener
		inputManager.addMapping(MAPPING_MOVE_UP, TRIGGER_MOVE_UP);
		inputManager.addListener(analogListener, new String[]{MAPPING_MOVE_UP});
		/*
		 * Keyboard Triggers end 
		 */

		// audio that plays in a loop continuously
		audio_nature = new AudioNode(assetManager, "Sounds/Arcade.wav", true); // sample audio frombook samples 
		audio_nature.setLooping(true);  // activate continuous playing
		audio_nature.setPositional(true);   
		//audio_nature.setLocalTranslation(Vector3f.ZERO.clone();
		audio_nature.setVolume(10); // specify volume 
		rootNode.attachChild(audio_nature);
		audio_nature.play(); 

		// create scene spatials, attach to proper nodes, and attach to rootNode
		initPlayer();
	}

	// initializes the audio node and sound effect of laser gun
	private void initAudio(){
		/* laser gun shot sound is to be triggered by a mouse click. */
		audio_gun = new AudioNode(assetManager, "Sounds/Gun.wav", false);
		audio_gun.setPositional(true);
		audio_gun.setReverbEnabled(true);
		audio_gun.setLooping(false);
		audio_gun.setVolume(0.01f);
		// attach audio node to root node
		rootNode.attachChild(audio_gun);
	}

	/**
	 * Cube generator from textbook, possible use for enemy spatials
	 * @param name 		identifier of output geometry
	 * @param loc		location of geometry output
	 * @param color		color of output geometry
	 * @return			output a geometry
	 */
	public Geometry myBox(String name, Vector3f loc, ColorRGBA color){
		Geometry geom = new Geometry(name, mesh);
		geom.scale(2.0f);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		geom.setMaterial(mat);
		geom.setLocalTranslation(loc);
		return geom;
	}

	/**
	 * Create player object consisting of two cube geometries, a body and a shot
	 */
	private void initPlayer(){

		Node playerBox= (Node) assetManager.loadModel(
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
		playerBox.setMaterial(mat);

		// scene graph, spatials, and root node attachment 
		Node playerNode = new Node("player");
		playerNode.attachChild(playerBox);
		//shotNode.attachChild(shotGeom);

		rootNode.attachChild(shotNode);
		rootNode.attachChild(playerNode);
	}

	@SuppressWarnings("deprecation")
	private Spatial initShot(String name){
		Node n = new Node(name);
		//System.out.println(rootNode.getChild("player").getLocalTranslation());
		Vector3f shotPos = rootNode.getChild("player").getLocalTranslation().add(0.0f,4.0f,0.0f); 
		Box shotBox = new Box(shotPos, 1, 1, 1);
		// create an object from the mesh
		Geometry shotGeom = new Geometry("shot cube", shotBox);
		Material shotMat = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		shotMat.setColor("Color", ColorRGBA.White);
		shotGeom.setMaterial(shotMat);

		n.setLocalTranslation(shotPos);
		n.attachChild(shotGeom);
		// add on particle emitter
		GameParticleEmitter emitter = new GameParticleEmitter(assetManager);
		n.attachChild(emitter.init());

		return n;
	}

	private void genShot(){
		//if (System.currentTimeMillis() - bulletCooldown > 83f) {
		//bulletCooldown = System.currentTimeMillis();

		Vector3f aim = getAimDir();

		// create bullet spatial 
		Spatial bullet = initShot("Bullet");
		Vector3f shotPos = rootNode.getChild("player").getLocalTranslation().add(0.0f,4.0f,0.0f); 
		bullet.setLocalTranslation(shotPos);
		System.out.println(bullet.getLocalTranslation());
		bullet.addControl(new bulletControlState(aim, cam.getWidth(), cam.getHeight(), rootNode, assetManager));
		shotNode.attachChild(bullet);

	}

	/**
	 * Generate randomly located cubes possibly for enemy generation
	 * @param number 	number of cubes to generate 
	 */
	private void makeCubes(int number) {
		for (int i = 0; i < number; i++) {
			// randomize 3D coordinates
			Vector3f loc = new Vector3f(
					FastMath.nextRandomInt(-65, 65),
					FastMath.nextRandomInt(-65, 65),
					0.0f);
			//rootNode.attachChild(myBox("Cube" + i, loc, ColorRGBA.randomColor()));
			Geometry geom = myBox("Cube" + i, loc, ColorRGBA.randomColor());
			metNode.attachChild(geom);
		} 
	}

	/**
	 *  Analog listener for keyboard input 
	 */
	private AnalogListener analogListener = new AnalogListener() {

		public void onAnalog(String name, float intensity, float tpf){
			if (name.equals(MAPPING_SHOOT_KEY)) {
				if (System.currentTimeMillis() - bulletCooldown > 83f) {
					bulletCooldown = System.currentTimeMillis();
					genShot();
					// if player is shooting, play audio_gun sound effect
				}
				audio_gun.playInstance();
			}// end if 
			else if (name.equals(MAPPING_MOVE_LEFT)) {
				rootNode.getChild("player").rotate(0,0,playerDir*tpf);
			}// end else if 
			else if (name.equals(MAPPING_MOVE_RIGHT)) {
				rootNode.getChild("player").rotate(0,0,-playerDir*tpf);
			}// end else if 
			else if (name.equals(MAPPING_MOVE_UP)) {
				acc = rootNode.getChild("player").getLocalRotation().getRotationColumn(1).normalize().mult(playerSpeed);
			}// end else if 
		}//end onAnalog 
	}; // end analogListener

	/*
	 * Returns direction of aim of fire
	 */
	private Vector3f getAimDir() {
		Vector2f origin = new Vector2f(cam.getWidth()/2,cam.getHeight()/2); 
		Vector3f playerPos = rootNode.getChild("player").getLocalTranslation();
		Vector3f dif = new Vector3f(origin.x-playerPos.x,origin.y-playerPos.y,0);

		Vector3f rot = rootNode.getChild("player").getLocalRotation().getRotationColumn(1);
		return rot;
		//return dif.normalizeLocal();
	}

}