/**
 *  Test of animation capabilities from jmonkey using Jaime model from jmonkey github
 */
package Game;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;


public class AnimateModel extends SimpleApplication implements AnimEventListener{

	private Node player; // node 
	private AnimControl control; //animaiton contrl
	private static final String ANI_IDLE = "Idle"; // idle animation state 
	private static final String ANI_WALK = "Walk"; // walking animation state
	private AnimChannel channel; // animation channel
	private static final String MAPPING_WALK = "walk forwards"; // key mapping for walking foreward animation

	@Override
	public void simpleInitApp() {

		// adding action listener 
		final Trigger TRIGGER_WALK= new KeyTrigger(KeyInput.KEY_SPACE);
		// add mapping and listener
		inputManager.addMapping(MAPPING_WALK, TRIGGER_WALK);
		inputManager.addListener(analogListener, MAPPING_WALK);
		inputManager.addListener(actionListener, MAPPING_WALK);

		// loading model into scene graph
		//Spatial mymodel = assetManager.loadModel("MyModel.j3o");
		player = (Node)assetManager.loadModel("./Models/Jaime/Jaime.j3o");
		player.rotate(0, FastMath.DEG_TO_RAD * 0, 0);
		rootNode.attachChild(player);

		// add animation control
		control = player.getControl(AnimControl.class); 

		// initialize animation channel
		channel = control.createChannel();
		channel.setAnim(ANI_IDLE); 
		// registering the anim control listener
		control.addListener(this);

		// light souuce when using materials
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)));
		sun.setColor(ColorRGBA.White);
		rootNode.addLight(sun);

		// Optional code to find out animation methods 
		//for (String anim : control.getAnimationNames())
		//{ System.out.println(anim); }


	}

	/**
	 * Create instance of animate model class and call begin method
	 * @param args
	 */
	public static void main(String[] args) {
		AnimateModel app = new AnimateModel();

		app.start();
	}

	/**
	 * Action listener for changing animation states
	 */
	private ActionListener actionListener = new ActionListener() {
		/**
		 * Upon action listener being called
		 * @param name 			Name of mapping
		 * @param isPressed		If a button is being pressed
		 * @param tbf 			Translucent render buffer
		 */
		public void onAction(String name, boolean isPressed, float tpf)
		{
			// if button is pressed, the set state to walking
			if (name.equals(MAPPING_WALK) && isPressed) {
				if (!channel.getAnimationName().equals(ANI_WALK)) {
					channel.setAnim(ANI_WALK);
				}	
			}
			// else: set to idle
			if (name.equals(MAPPING_WALK) && !isPressed) {
				channel.setAnim(ANI_IDLE);
			}
		} };

		/**
		 * Analog listener for moving object as it is in walking state
		 */
		private AnalogListener analogListener = new AnalogListener() {
			/**
			 * Upon analog listener being called
			 * @param name 			Name of mapping
			 * @param intensity 	intensity of analog signal
			 * @param tbf 			Translucent render buffer
			 */
			public void onAnalog(String name, float intensity, float tpf) {
				// if mapping is mapping of walk, the move player to aid in walking naimation
				if (name.equals(MAPPING_WALK) ) {
					player.move(0, 0, tpf);
				} }
		};

		/**
		 * Method to perform action upon animation change
		 * @param control 	animation control
		 * @param channel	animation channel
		 * @param animName 	name of animation
		 */
		public void onAnimChange(AnimControl control,
				AnimChannel channel, String animName) {
			// if the animation state is walking or idle, print appropriate statement to console
			if (animName.equals(ANI_WALK)) {
				System.out.println(control.getSpatial().getName()
						+ " started walking.");
			} else if (animName.equals(ANI_IDLE)) {
				System.out.println(control.getSpatial().getName()
						+ " started being idle.");
			}
		}

		/**
		 * Performed after an animation cycle is complete
		 * @param control 	animation control
		 * @param channel	animation channel
		 * @param animName 	name of animation
		 */
		public void onAnimCycleDone(AnimControl control,
				AnimChannel channel, String animName)
		{
			// Prints out completion of either a walking or idle animation loop
			if (animName.equals(ANI_WALK)) {
				System.out.println(control.getSpatial().getName()
						+ " completed one walk loop.");
			} else if (animName.equals(ANI_IDLE)) {
				System.out.println(control.getSpatial().getName()
						+ " completed one idle loop.");
			} }

}