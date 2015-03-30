/** 
 * main java project appstate
 * 2369861
 * @author Jason Quisberth 
 * @since Mar 05 2015
 * @version 1.0 Spring 2015
 */
package Game;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.audio.AudioNode;
import com.jme3.bounding.BoundingBox;
import com.jme3.collision.CollisionResults;
import com.jme3.effect.ParticleEmitter;
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
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.input.*;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.shape.Line;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.ui.Picture;
import com.jme3.input.FlyByCamera;

public class MyGameState extends AbstractAppState {

	private SimpleApplication app; // simple application
	private FlyByCamera flyCam; // fly cameare
	private Camera cam; // standard camera
	private Node rootNode; // rootnode of scene graph
	private AssetManager assetManager; // asset manager 
	private InputManager inputManager; // input manager
	private final static String MAPPING_MOVE_RIGHT= "Move Right"; // key mapping to move right
	private final static String MAPPING_MOVE_LEFT= "Move Left"; // key mapping to move left
	private final static String MAPPING_MOVE_UP= "Move Up"; // key mapping to move up 
	private final static String MAPPING_MOVE_DOWN= "Move Down"; // key mapping to move down
	private final static String MAPPING_SHOOT_KEY = "Shoot Key"; // key mapping to shoot using keyboard
	// direction and speed of shot 
	private float ballAngle = 5;
	private Vector3f Vel = new Vector3f(0.0f,0.0f,0.0f);
	private Vector3f acc = new Vector3f(0.0f,0.0f,0.0f);
	private float ballSpeed = 0.0f;
	private float playerSpeed = 15.0f;

	//private CollisionResults results0 = new CollisionResults();
	private CollisionResults results1 = new CollisionResults(); // collision results 
	private CollisionResults results2 = new CollisionResults(); // collision results 

	// boudning box parameters
	private Vector3f min = new Vector3f(-70.0f,-70.0f,0.0f);
	private Vector3f max = new Vector3f(70.0f,70.0f,0.0f);
	private BoundingBox bb = new BoundingBox(min, max);

	// node for shot
	private Node shotNode = new Node("shot");

	// guiNode class field 
	private BitmapText distanceText;
	protected BitmapFont guiFont;

	// audio nodes
	private AudioNode audio_gun;
	private AudioNode audio_nature;

	// cube spatial
	@SuppressWarnings("deprecation")
	private static Box mesh = new Box(Vector3f.ZERO, 1, 1, 1);

	private int counter=0; // interator for debugging 
	private Camera camera; // camera
	private Node guiNode; // gui node 
	public int getCounter() { 
		return counter; 
	}

	@Override
	public void update(float tpf) {

		// moving player and shot nodes for gameplay
		Vel.addLocal(acc.mult(10*tpf));
		//rootNode.getChild("player").move(speedX,speedY,0);
		rootNode.getChild("player").setLocalTranslation(rootNode.getChild("player").getLocalTranslation().add(Vel.mult(tpf)));
		rootNode.getChild("shot").move(0.0f,ballSpeed,0.0f);

		// check for intersection 
		rootNode.getChild("shot").collideWith(bb,results1);
		rootNode.getChild("player").collideWith(bb,results2);

		// reset acceleration to 0
		acc = new Vector3f(0.0f,0.0f,0.0f);

		// check for ship out of bounds 
		if (results2.size() <= 6){
			System.out.println("size: " + results2.size());
			//Vector3f mirror = new Vector3f(-rootNode.getChild("player").getLocalTranslation().x + 1.0f,-rootNode.getChild("player").getLocalTranslation().y+ 1.0f,rootNode.getChild("player").getLocalTranslation().z);
			Vector3f mirror = new Vector3f(-rootNode.getChild("player").getLocalTranslation().x + 1.0f,-rootNode.getChild("player").getLocalTranslation().y+ 1.0f,rootNode.getChild("player").getLocalTranslation().z);
			rootNode.getChild("player").getLocalTranslation().set(mirror);
			//System.out.println("ship out");
			ballSpeed = 0;
		}


		// if out of bounds, move shot back to original location from ship
		if (results1.size() <= 6){
			//rootNode.getChild("shot").getLocalTranslation().set(0.0f, 0.0f, 0.0f);
			rootNode.getChild("shot").getLocalTranslation().set(rootNode.getChild("player").getLocalTranslation());
			//rootNode.getChild("player").getLocalTranslation();
			ballSpeed = 0;
		}

		// clear collision results for bounding box
		results1.clear();
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

		// particles emmitter for flame start
		ParticleEmitter fire = 
				new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
		Material mat_red = new Material(assetManager, 
				"Common/MatDefs/Misc/Particle.j3md");
		mat_red.setTexture("Texture", assetManager.loadTexture(
				"flame.png"));
		fire.setMaterial(mat_red);
		fire.setImagesX(2); 
		fire.setImagesY(2); // 2x2 texture animation
		fire.setEndColor(  new ColorRGBA(1f, 0f, 0f, 1f));   // red
		fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
		fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
		fire.setStartSize(1.5f);
		fire.setEndSize(0.1f);
		fire.setGravity(0, 0, 0);
		fire.setLowLife(1f);
		fire.setHighLife(3f);
		fire.getParticleInfluencer().setVelocityVariation(0.3f);
		//rootNode.attachChild(fire);
		shotNode.attachChild(fire);
		// particles emmitter for flame end 

		initAudio();

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

		// set camera location
		Vector3f loc = new Vector3f(0.0f,0.0f,180f);
		cam.setLocation(loc);

		// 2d settings 
		/*
        cam.setParallelProjection(true);
        cam.setLocation(new Vector3f(0,0,0.5f));
        flyCam.setEnabled(false);
		 */

		// flycam speed
		flyCam.setMoveSpeed(1f);

		// make cursor visible
		flyCam.setDragToRotate(true);
		inputManager.setCursorVisible(true);

		//moving keybaord trigger
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

		//moving keybaord trigger
		final Trigger TRIGGER_MOVE_DOWN= new KeyTrigger(KeyInput.KEY_DOWN);
		// add mapping and listener
		inputManager.addMapping(MAPPING_MOVE_DOWN, TRIGGER_MOVE_DOWN);
		inputManager.addListener(analogListener, new String[]{MAPPING_MOVE_DOWN});

		// create scene spatials, attach to proper nodes, and attach to rootNode
		genBoundingBox();
		genPlayer();
		//makeCubes(10);
	}

	private void initAudio(){
		/** We create two audio nodes. */
		/* gun shot sound is to be triggered by a mouse click. */
		audio_gun = new AudioNode(assetManager, "Gun.wav", false);
		audio_gun.setPositional(false);
		audio_gun.setLooping(false);
		audio_gun.setVolume(2);
		rootNode.attachChild(audio_gun);

		/* nature sound - keeps playing in a loop. */
		audio_nature = new AudioNode(assetManager, "Ocean Waves.ogg", true); // only mono for this type
		audio_nature.setLooping(true);  // activate continuous playing
		audio_nature.setPositional(true);   
		audio_nature.setVolume(3);
		rootNode.attachChild(audio_nature);
		audio_nature.play(); // play continuously!
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
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		geom.setMaterial(mat);
		geom.setLocalTranslation(loc);
		return geom;
	}

	/**
	 * Generate bounding box for gameplay using connected lines
	 */
	private void genBoundingBox(){
		// using 4 lines, we create a box
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

		// attach sides to boudingbox node
		boundingBox.attachChild(side0);
		boundingBox.attachChild(side1);
		boundingBox.attachChild(side2);
		boundingBox.attachChild(side3);

		// attach bounding box to rootNode
		rootNode.attachChild(boundingBox);
	}

	/**
	 * Create player object consisting of two cube geoemtries, a body and a shot
	 */
	@SuppressWarnings("deprecation")
	private void genPlayer(){

		/*
		// create main character cube 
		Box playerBox = new Box(Vector3f.ZERO, 3, 3, 3);
		Geometry playerGeom = new Geometry("player cube", playerBox);

		// Create materila properties 
		Material playerMat= new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		//playerMat.setColor("Color", ColorRGBA.Red);
		playerMat.setBoolean("UseMaterialColors", true);
		playerMat.setColor("Ambient", ColorRGBA.Black);
		playerMat.setColor("Diffuse", ColorRGBA.Red);
		playerMat.setColor("Specular", ColorRGBA.White );
		playerMat.setFloat("Shininess", 100f); // [1,128]
		playerGeom.setMaterial(playerMat);
		 */


		//FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
		//viewPort.addProcessor(fpp);
		//BloomFilter bloom =
		//		new BloomFilter(BloomFilter.GlowMode.SceneAndObjects);
		//fpp.addFilter(bloom);

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
		//TextureKey tankGlow = new TextureKey(
		//		"Tank/tank_glow_map.jpg", false);
		//mat.setTexture("GlowMap",
		//		assetManager.loadTexture(tankGlow));
		//mat.setColor("GlowColor", ColorRGBA.Red);

		playerBox.setMaterial(mat);
		//rootNode.attachChild(playerBox);



		// create a shot cube 
		Vector3f shotPos = new Vector3f(0.0f,4.0f,0.0f);
		Box shotBox = new Box(shotPos, 1, 1, 1);
		// create an object from the mesh
		Geometry shotGeom = new Geometry("shot cube", shotBox);
		Material shotMat = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		shotMat.setColor("Color", ColorRGBA.White);
		shotGeom.setMaterial(shotMat);


		// scene graph, spatials, and root node attachment 
		Node playerNode = new Node("player");
		Node shipNode = new Node("ship");
		//shotNode= new Node("shot");
		playerNode.attachChild(playerBox);
		shotNode.attachChild(shotGeom);

		// example method call for myBox
		//mainNode.attachChild(myBox("Blue Cube", new Vector3f(0, -1.5f, 0), ColorRGBA.Blue));
		//playerNode.attachChild(shotNode);

		rootNode.attachChild(shotNode);
		rootNode.attachChild(playerNode);
	}


	/**
	 * Generate randomly located cubes possibly for enemy generation
	 * @param number 	number of cubes to generate 
	 */
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
				// add control method for enemy cubes
				geom.addControl(new MyGameControl(cam, rootNode));
			}
			rootNode.attachChild(geom);
		} 
	}

	/**
	 *  Analog listener for shooting with mouse
	 */
	private AnalogListener analogListener = new AnalogListener() {

		public void onAnalog(String name, float intensity, float tpf){
			if (name.equals(MAPPING_SHOOT_KEY)) {
				ballSpeed = 1.0f;
				//audio_gun.play();
				audio_gun.playInstance();
			}// end if 
			else if (name.equals(MAPPING_MOVE_LEFT)) {
				rootNode.getChild("player").rotate(0,0,ballAngle*tpf);
			}// end if 
			else if (name.equals(MAPPING_MOVE_RIGHT)) {
				rootNode.getChild("player").rotate(0,0,-ballAngle*tpf);
			}// end if 
			else if (name.equals(MAPPING_MOVE_UP)) {
				//thrust += 0.008;
				acc = rootNode.getChild("player").getLocalRotation().getRotationColumn(1).normalize().mult(playerSpeed);
			}// end if 
			else if (name.equals(MAPPING_MOVE_DOWN)) {
				//thrust -= 0.008;
			}// end if 
		}//end onAnalog 
	}; // end analogListener

}