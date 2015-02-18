//Game by Jason Quisberth 
//when setting up from scratch, create assets folder through eclipse 
package Game;

// required imports
import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;

public class CubeChaser extends SimpleApplication {

	//private final static String MAPPING_ROTATE = "Rotate";
	public static void main(String[] args) {

		CubeChaser app = new CubeChaser();

		AppSettings settings = new AppSettings(true);
		// custom settings page 
		settings.setTitle("My Cool Game");
		// source the image where the splash is from 
		settings.setSettingsDialogImage("splash.png");
		app.setSettings(settings); // apply settings to app
		app.start(); // start the game
	}

	@Override
	public void simpleInitApp() {
		CubeChaserState state = new CubeChaserState();
		stateManager.attach(state);
	}

	public void update(float tpf) {}

}