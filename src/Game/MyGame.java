//Game by Jason Quisberth 
//when setting up from scratch, create assets folder through eclipse 
package Game;

//TODO: create shot fired scene translation of shot cube


// required imports
import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;

public class MyGame extends SimpleApplication {

	// some static variables for the mapping and shot cube geometry
	//private Geometry geom;
	//private static Box mesh = new Box(Vector3f.ZERO,1.0f,1.0f,1.0f);

	public static void main(String[] args) {

		MyGame myoApp = new MyGame();

		AppSettings settings0 = new AppSettings(true);
		// custom settings page 
		settings0.setTitle("Myo Monkey Asteroids");
		// source the image where the splash is from 
		settings0.setSettingsDialogImage("splash.png");
		myoApp.setSettings(settings0); // apply settings to app
		myoApp.start(); // start the game

	}

	@Override
	public void simpleInitApp() {
		MyGameState state = new MyGameState();
		stateManager.attach(state);

	}
}



