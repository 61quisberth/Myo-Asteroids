/** 
 * Main java project file
 * @author Jason Quisberth  2369861
 * @version 1.0 Spring 2015
 * @since Mar 05 2015
 */
package Game;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;

public class MyGame extends SimpleApplication {

	/**
	 * main method which creates instance of SimpleApplication  
	 * @param args
	 */
	public static void main(String[] args) {

		MyGame myoApp = new MyGame();

		AppSettings settings0 = new AppSettings(true);
		
		settings0.setTitle("Myo Monkey Asteroids"); // custom settings page 
		
		settings0.setSettingsDialogImage("./Interface/splash.png"); // custom splash page 
		// ^ source: http://fc07.deviantart.net/fs70/f/2012/148/3/a/water_splash_png_by_starlaa1-d51fstj.png
		myoApp.setSettings(settings0); // apply settings to app
		myoApp.start(); // start the game

	}
	/**
	 *  main method to create instance of AppState and attaching it to the state manager
	 */
	@Override
	public void simpleInitApp() {
		MyGameState state = new MyGameState();
		stateManager.attach(state);

	}
}



