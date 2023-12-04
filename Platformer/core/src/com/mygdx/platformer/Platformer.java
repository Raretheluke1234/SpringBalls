package com.mygdx.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.platformer.Screens.PlayScreen;

public class Platformer extends Game {
	//Dimensions for Viewport
	public static final int V_WIDTH = 480;
	public static final int V_HEIGHT = 320;

	//Sets the main screen in Game to PlayScreen
	@Override
	public void create () {
		setScreen(new PlayScreen(this));
	}

	//Sends the render method to Game to delegate tasks to set Screens
	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

	}
}
