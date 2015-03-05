/**
 * Loads a j3o file and renders for testing purposes
 * @author Jason Quisberth
 */
package Game;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class j30_Loader extends SimpleApplication {
	/**
	 * Init method: load object and attach to rootNode to render with light
	 */
	@Override
	public void simpleInitApp() {
		// testing loadng as Spatial and as node 
		//Spatial mymodel = assetManager.loadModel("MyModel.j3o");
		//Node mymodel = (Node)assetManager.loadModel("./Models/MyModel.j3o");
		Node mymodel = (Node)assetManager.loadModel("./Models/MyModel.j3o");
		rootNode.attachChild(mymodel);
		
		// add directional lighting 
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)));
		sun.setColor(ColorRGBA.White);
		rootNode.addLight(sun);
	}

	/**
	 * Create simple application object and begin application
	 * @param args
	 */
	public static void main(String[] args) {
		j30_Loader app = new j30_Loader();
		app.start();
	}
} 