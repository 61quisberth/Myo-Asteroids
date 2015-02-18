package Game;

import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
//import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
//import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class MyGameControl extends AbstractControl{
	//private Camera cam;
	//private Node rootNode;

	public MyGameControl(Camera cam, Node rootNode) {
		//this.cam = cam;
		//this.rootNode = rootNode;
	}

	@Override
	protected void controlUpdate(float tpf) {
		spatial.rotate(tpf, tpf, tpf);
	}


	protected void controlRender(RenderManager rm, ViewPort vp) {
	}
	public Control cloneForSpatial(Spatial spatial) {
		throw new UnsupportedOperationException(
				"Not supported yet.");
	}
}
