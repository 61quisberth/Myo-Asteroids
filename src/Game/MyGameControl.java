/** 
 * Main java project control 
 * @author Jason Quisberth  2369861
 * @version 1.0 Spring 2015
 * @since Mar 05 2015
 */
package Game;

import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class MyGameControl extends AbstractControl{
	/**
	 * Constructor for control
	 * @param cam 		Camera
	 * @param rootNode	The rootNode of scene graph
	 */
	public MyGameControl(Camera cam, Node rootNode) {
	}

	/**
	 * Method to print out the identity of a spatial to the console
	 * @return returns an id of a spatial to be printed out to the console
	 */
	public String hello(){
		return "Hello, my name is "+spatial.getName();
	}

	/**
	 * Implement of the behavior of the control
	 * Rotate spatial causing call of control
	 */
	@Override
	protected void controlUpdate(float tpf) {
		spatial.rotate(tpf, tpf, tpf);
		spatial.rotate(tpf, tpf, tpf);
	}


	/**
	 * Optional rendering manipulation
	 * Will throw error if this is not supported
	 */
	protected void controlRender(RenderManager rm, ViewPort vp) {
	}
	public Control cloneForSpatial(Spatial spatial) {
		throw new UnsupportedOperationException(
				"Not supported yet.");
	}
}
