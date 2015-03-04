package Game;
// an edit of the orgreconverter code provided by jMonkeyEngine 

/*
 * Copyright (c) 2009-2010 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.jme3.app.SimpleApplication;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import java.io.File;
import java.io.IOException;

public class ConvertToj3o extends SimpleApplication {

	public static void main(String[] args){
		ConvertToj3o app = new ConvertToj3o();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		// loads a .xml file and adds some material proerties 
		Spatial mymodel = assetManager.loadModel("./Textures/Monkey/Suzanne.mesh.xml"); 
	    Material modelMat = new Material(assetManager, 
	            "Common/MatDefs/Light/Lighting.j3md");
	    modelMat.setBoolean("UseMaterialColors", true);
	    modelMat.setColor("Ambient", ColorRGBA.Gray );
	    modelMat.setColor("Diffuse", ColorRGBA.Green);
	    modelMat.setColor("Specular", ColorRGBA.White );
	    modelMat.setFloat("Shininess", 20f); // [1,128]
	    mymodel.setMaterial(modelMat);
	    
	    // adding directional light to become visible
		DirectionalLight dl = new DirectionalLight();
		dl.setColor(ColorRGBA.White);
		dl.setDirection(new Vector3f(0,-1,-1).normalizeLocal());
		rootNode.addLight(dl);
		rootNode.attachChild(mymodel);
		
		rootNode.rotate(180, 0, 0);

	}

	// method to save file into j3o upon closing of diaply or exit of main program
	@Override
	public void stop() {
		BinaryExporter exporter = BinaryExporter.getInstance();
		File file = new File("./Assets/Models/Monkey/Suzanne.j3o");
		try {
			exporter.save(rootNode, file);
			System.out.println("Converted file to j3o!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("error in io");
			e.printStackTrace();
		}
		super.stop(); // continue quitting the game
	}	


}