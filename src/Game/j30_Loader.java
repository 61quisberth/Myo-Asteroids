package Game;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class j30_Loader extends SimpleApplication {
	@Override
	public void simpleInitApp() {
		//Spatial mymodel = assetManager.loadModel("MyModel.j3o");
		Node mymodel = (Node)assetManager.loadModel("MyModel.j3o");
		rootNode.attachChild(mymodel);
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)));
		sun.setColor(ColorRGBA.White);
		rootNode.addLight(sun);
	}

	public static void main(String[] args) {
		j30_Loader app = new j30_Loader();
		app.start();
	}
} 