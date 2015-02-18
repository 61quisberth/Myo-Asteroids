package Game;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class CubeChaserControl extends AbstractControl{

	private Ray ray = new Ray();
	private final Camera cam;
	private final Node rootNode;

	public CubeChaserControl(Camera cam, Node rootNode) {
		this.cam = cam;
		this.rootNode = rootNode;
	}

	@Override
	protected void controlUpdate(float tpf) {
		CollisionResults results = new CollisionResults();
		ray.setOrigin(cam.getLocation());
		ray.setDirection(cam.getDirection());
		rootNode.collideWith(ray, results);
		if (results.size() > 0) {
			Geometry target = results.getClosestCollision().getGeometry();
			// interact with target
			if (target.equals(spatial)) {
				if (cam.getLocation().distance(spatial.getLocalTranslation()) <10) {
					spatial.move(cam.getDirection());
				} 
			}
		}
		spatial.rotate(tpf, tpf, tpf);
	}


	protected void controlRender(RenderManager rm, ViewPort vp) {
	}
	public Control cloneForSpatial(Spatial spatial) {
		throw new UnsupportedOperationException(
				"Not supported yet.");
	}
}
