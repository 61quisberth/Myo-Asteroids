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
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.io.File;
import java.io.IOException;

public class LoadAndSave extends SimpleApplication {

	public static void main(String[] args){
		LoadAndSave app = new LoadAndSave();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		 try {
	           Node loadedNode = (Node) assetManager.loadModel(
	                                    "./SavedGames/savedgame.j3o");
	           rootNode.attachChild(loadedNode);
	           System.out.println("saved game loaded!");
	       } catch (com.jme3.asset.AssetNotFoundException e) {
	    	  System.out.println("count not find saved game"); 
	       }	

		
		Spatial mymodel = assetManager.loadModel("./Models/MyModel.j3o");

		    mymodel.move(
		        FastMath.nextRandomFloat()*10-5,
		        FastMath.nextRandomFloat()*10-5,
		        FastMath.nextRandomFloat()*10-5);
		    rootNode.attachChild(mymodel);

	}

	@Override
	public void stop() {
		BinaryExporter exporter = BinaryExporter.getInstance();
		File file = new File("./Assets/SavedGames/savedgame.j3o");
		try {
			exporter.save(rootNode, file);
			System.out.println("saved game scene!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("error in io");
			e.printStackTrace();
		}
		super.stop(); // continue quitting the game
	}	
	

}