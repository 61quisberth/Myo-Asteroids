package Game;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Line;

public class GameBoundingBox {

	private AssetManager assetManager;

	// bounding box parameters 
	final Vector3f min = new Vector3f(-70.0f,-70.0f,0.0f);
	final Vector3f max = new Vector3f(70.0f,70.0f,0.0f);
	final BoundingBox bb = new BoundingBox(min, max);
	final Vector3f LEFT = new Vector3f(-70.0f,10.0f,0.0f);
	final Vector3f RIGHT = new Vector3f(70.0f,0.0f,0.0f);
	final Vector3f UP = new Vector3f(0.0f,70.0f,0.0f);
	final Vector3f DOWN = new Vector3f(0.0f,-70.0f,0.0f);
	final Vector3f[] SIDES = {LEFT, RIGHT,UP,DOWN};

	public GameBoundingBox(AssetManager assetManager){
		this.assetManager = assetManager; 
	}

	/**
	 * Generate bounding box for gameplay using connected lines
	 */
	public BoundingBox initBoundingBox(){

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
		//rootNode.attachChild(boundingBox);
		return bb;
	}


	/* 
	 *  Returns int correlating to the side that is closest, return 4 for error
	 */
	public int getClosestSide(Vector3f p){
		float minDist = 100000.0f; // hardcoded value never to be reached
		int closestSide = 4;
		// iterate through array and obtain side which is closest
		for(int i = 0; i < SIDES.length; i++){
			if(SIDES[i].distance(p) < minDist){
				minDist = SIDES[i].distance(p);
				closestSide = i;
			}
		}
		System.out.println("refection side: " + closestSide);
		return closestSide;
	}


	/*
	 * side of boundingbox, reflect players position 
	 */
	public void reflect(Spatial sp, int side){
		float shift = 6.0f;
		switch (side) {
		case 0: /* left */ 
			Vector3f mirror0= new Vector3f(-sp.getLocalTranslation().x - shift,sp.getLocalTranslation().y,sp.getLocalTranslation().z);
			sp.setLocalTranslation(mirror0);
			break;
		case 1: /* right */ 
			Vector3f mirror1= new Vector3f(-sp.getLocalTranslation().x + shift,sp.getLocalTranslation().y,sp.getLocalTranslation().z);
			sp.setLocalTranslation(mirror1);
			break;
		case 2: /* up */
			Vector3f mirror2= new Vector3f(sp.getLocalTranslation().x,-sp.getLocalTranslation().y + shift,sp.getLocalTranslation().z);
			sp.setLocalTranslation(mirror2);
			break;
		case 3:  /* down */
			Vector3f mirror3= new Vector3f(sp.getLocalTranslation().x,-sp.getLocalTranslation().y - shift,sp.getLocalTranslation().z);
			sp.setLocalTranslation(mirror3);
			break;
		default: /* do not reflect */ 
			break;
		}
	}
}
