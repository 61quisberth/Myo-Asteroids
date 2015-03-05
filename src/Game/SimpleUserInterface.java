/** 
 * Test file for creating custom user interface 
 * 2369861
 * @author Jason Quisberth 
 * @since Mar 05 2015
 * @version 1.0 Spring 2015
 */
package Game;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.ui.Picture;

public class SimpleUserInterface extends SimpleApplication{

	private int distance=0; // value to update and print to gui
	private BitmapText distanceText; //text to printout to gui


	/**
	 * Init: Override default gui with custom gui 
	 */
	@Override
	public void simpleInitApp() {
		setDisplayStatView(false);
		setDisplayFps(false);

		// load and attach user interface frame
		Picture frame = new Picture("User interface frame");
	       frame.setImage(assetManager, "Interface/frame.png", false);
	       frame.move(settings.getWidth()/2-265, 0, -2);
	       frame.setWidth(530);
	       frame.setHeight(10);
	       guiNode.attachChild(frame);
	       
	       // load and attach logo of monkey 
	       Picture logo = new Picture("logo");
	       logo.setImage(assetManager, "Interface/Monkey.png", true);
	       logo.move(settings.getWidth()/2-47, 2, -1);
	       logo.setWidth(95);
	       logo.setHeight(75);
	       guiNode.attachChild(logo);


		// load font, initialize bitmap test obj and attach to gui node
		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		distanceText = new BitmapText(guiFont);
		distanceText.setSize(guiFont.getCharSet().getRenderedSize());
		distanceText.move(
				settings.getWidth()/2,         // X
				distanceText.getLineHeight(),  // Y
				0);                            // Z (depth layer)
		distanceText.setColor(ColorRGBA.White); // color of display
		//distanceText.move(settings.getWidth()/2+50, distanceText.getLineHeight()-20,0);
		guiNode.attachChild(distanceText);
	}

	/**
	 * Create object of simpleApplication and start application
	 * @param args
	 */
	public static void main(String[] args){
		SimpleUserInterface sui = new SimpleUserInterface();
		sui.start();

	}
	/**
	 * Updates the gui 
	 * @param tbf 	Translucent Render Bucket
	 */
	public void simpleUpdate(float tpf) {
		// obtain distance from camera and print out to gui
		distance = (int) Vector3f.ZERO.distance(cam.getLocation());
		distanceText.setText("Distance: "+distance + " Hello World");
	}

}
