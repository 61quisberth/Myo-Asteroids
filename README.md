# Myo-Asteroids
Java Asteroids featuring a Myo controller 
by Jason Quisberth 

Current Course project for CS 582 Game Programming Coure at Catholic University 
Textbook: jMonkeyEngine 3.0 Beginners Guide by Ruth Kusterer

Features use of jMonkeyEngine 3.0 as well as Myo-Java 
http://jmonkeyengine.org
https://github.com/NicholasAStuart/myo-java

Eclipse project with jMonkeyEngine JAR binaries 
http://wiki.jmonkeyengine.org/doku.php/jme3:setting_up_jme3_in_eclipse

Loading and saving games (.j3o files)
http://wiki.jmonkeyengine.org/doku.php/jme3:advanced:save_and_load

overcoming timed locking deafult of myo device 
https://developer.thalmic.com/docs/api_reference/platform/script-tutorial.html

Progress to be stored on this file as development continues 

To run in eclipse: run MyGame.java 

To include jar in project:
right click referenced libraries and select Build Path -> Configure Build Path 
add external jars -> [select your jar]

In blender, to include ogre3d exporter tool from googlecode
https://code.google.com/p/blender2ogre/downloads/list

Assets folder breakdown:
Textures: Original models, textures and materials
Models: .j3o jmonkey model files 

Updates

2/11/2015 (assignment 2 due date):
Assignment 2 turned in, incomplete appstate, features include controls, listeners, mappings, triggers, and a scene graph with transformations
Created player geometry with ability to shoot a projective 
todo: Complete appstate 

2/18/2015 
Completed cube chaser example in ch 3 of textbook, complete with appstate desired for MyGame
todo: Implement appstate with MyGame

3/03/2015
Appstate for MyGame complete
Completed Ch 4 & 5 of textbook
Assets folder enhanced with subfolders for textures and models
Basic animation and model imported 
Worked on basic controls of main player
introduced lighting, lite gui, and sample code for anim controls and listeners
todo: clean up clutter from cram coding...