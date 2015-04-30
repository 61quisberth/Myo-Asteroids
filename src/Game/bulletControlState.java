package Game;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitorAdapter;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

public class bulletControlState extends AbstractControl {
	private int screenWidth, screenHeight;

	private GameBoundingBox gbb;
	private BoundingBox bb;
	private Node rootNode;
	private BoundingBox shotBox;


	private float speed =35.0f;
	public Vector3f direction;

	// constructor
	public bulletControlState(Vector3f direction, int screenWidth, int screenHeight, Node rootNode, AssetManager assetManager) {
		this.direction = direction;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.rootNode = rootNode;
		gbb = new GameBoundingBox(assetManager);
		bb = gbb.initBoundingBox();
	
	}

	@Override
	protected void controlUpdate(float tpf) {
		//        movement
		shotBox = new BoundingBox(spatial.getLocalTranslation(),1.0f, 1.0f,1.0f);

		//        check boundaries
		initBound(rootNode);
		spatial.move(direction.mult(speed*tpf));
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {}

	protected void initBound(Node rootNode){

		//System.out.println(spatial.getLocalTranslation());

		CollisionResults results3 = new CollisionResults();  // shot + mets 
		CollisionResults results1 = new CollisionResults();  // shot + bb

		// traverse scene graph particually node meteors children
		SceneGraphVisitorAdapter myControlVisitor = new SceneGraphVisitorAdapter() {

			@Override
			public void visit(Geometry geom) {
				super.visit(geom);
				searchForBox(geom);
			}   

			// custom criteria search 
			// http://wiki.jmonkeyengine.org/doku.php/jme3:advanced:traverse_scenegraph
			private void searchForBox(Spatial spatial) {
				spatial.collideWith(bb, results1);
				if(results1.size() < 6)
					spatial.removeFromParent();
				rootNode.getChild("meteors").collideWith(shotBox,results3);
				// check if spatial is child of metoers node and also for collision
				if (results3.size() != 0 && spatial.getParent().getName().equals("meteors")){ 
					//if (results3.size() <= 6 && spatial.getParent().getName().equals("meteors")){
					System.out.println("collision with: " + spatial.getName());
					System.out.println("child of: " + spatial.getParent().getName());
					System.out.println("collision size: " + results3.size());
					// remove spatial from scene node
					spatial.removeFromParent();
				}   
				// clear results to check for other collisions
				results1.clear();
				results3.clear();
			}   
		};  

		// traverse scene node with either depth first or breadth first 
		//rootNode.depthFirstTraversal(myControlVisitor);
		rootNode.breadthFirstTraversal(myControlVisitor);
	}
}
