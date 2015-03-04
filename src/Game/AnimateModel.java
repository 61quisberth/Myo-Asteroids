// tesing animation capabilities from jmonkey using Jaime model from jmonkey github
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

	private Node player;
	private AnimControl control;
	private static final String ANI_IDLE = "Idle";
	private static final String ANI_WALK = "Walk";
	private AnimChannel channel;
	private static final String MAPPING_WALK = "walk forwards";

	@Override
	public void simpleInitApp() {

		// adding action listener 
		final Trigger TRIGGER_WALK= new KeyTrigger(KeyInput.KEY_SPACE);
		// add mapping and listener
		inputManager.addMapping(MAPPING_WALK, TRIGGER_WALK);
		inputManager.addListener(analogListener, MAPPING_WALK);
		inputManager.addListener(actionListener, MAPPING_WALK);
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

		// to find out that animation methods 
		//for (String anim : control.getAnimationNames())
		//{ System.out.println(anim); }


	}

	public static void main(String[] args) {
		AnimateModel app = new AnimateModel();

		app.start();
	}

	// Action listener for changing animation states
	private ActionListener actionListener = new ActionListener() {
		public void onAction(String name, boolean isPressed, float tpf)
		{
			if (name.equals(MAPPING_WALK) && isPressed) {
				if (!channel.getAnimationName().equals(ANI_WALK)) {
					channel.setAnim(ANI_WALK);
				}	
			}
			if (name.equals(MAPPING_WALK) && !isPressed) {
				channel.setAnim(ANI_IDLE);
			}
		} };

		// Analog listener for moving object as it is in walking state
		private AnalogListener analogListener = new AnalogListener() {
			public void onAnalog(String name, float intensity, float tpf) {
				if (name.equals(MAPPING_WALK) ) {
					player.move(0, 0, tpf);
				} }
		};

		// Method to perform action upon animation change
		public void onAnimChange(AnimControl control,
				AnimChannel channel, String animName) {
			if (animName.equals(ANI_WALK)) {
				System.out.println(control.getSpatial().getName()
						+ " started walking.");
			} else if (animName.equals(ANI_IDLE)) {
				System.out.println(control.getSpatial().getName()
						+ " started being idle.");
			}
		}

		// method performed after an animcation cycle is complete
		public void onAnimCycleDone(AnimControl control,
				AnimChannel channel, String animName)
		{
			if (animName.equals(ANI_WALK)) {
				System.out.println(control.getSpatial().getName()
						+ " completed one walk loop.");
			} else if (animName.equals(ANI_IDLE)) {
				System.out.println(control.getSpatial().getName()
						+ " completed one idle loop.");
			} }

}