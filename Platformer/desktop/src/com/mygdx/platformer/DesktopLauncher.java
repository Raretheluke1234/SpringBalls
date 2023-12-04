package com.mygdx.platformer;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.platformer.Platformer;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		//declares config
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		//fps of game
		config.setForegroundFPS(60);
		//title of window
		config.setTitle("Spring Balls");
		//Sets size of window upon initiation
		config.setWindowedMode(480,320);
		//it's game time
		new Lwjgl3Application(new Platformer(), config);
	}
}
