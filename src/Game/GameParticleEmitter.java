package Game;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class GameParticleEmitter {

	private AssetManager assetManager; 

	// constructor
	public GameParticleEmitter(AssetManager assetManager){
		this.assetManager = assetManager;
	}

	// initialize particle emitter for shot spatial
	public Spatial init(){
		ParticleEmitter fire = 
				new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
		Material mat_red = new Material(assetManager, 
				"Common/MatDefs/Misc/Particle.j3md");
		// load particle texture from book samples
		mat_red.setTexture("Texture", assetManager.loadTexture(
				"Effects/flame.png"));
		fire.setMaterial(mat_red);
		fire.setImagesX(2); 
		fire.setImagesY(2); // 2x2 texture animation
		// set up starting and ending colors
		fire.setEndColor(  new ColorRGBA(0f, 0f, 1f, 1f));   
		fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); 
		fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
		fire.setStartSize(3.5f);
		fire.setEndSize(0.1f);
		fire.setGravity(0, 0, 0);
		fire.setLowLife(1f);
		fire.setHighLife(3f);
		fire.getParticleInfluencer().setVelocityVariation(0.3f);
		return fire;
		//shotNode.attachChild(fire);
	}	
}