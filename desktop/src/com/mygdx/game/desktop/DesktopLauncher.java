package com.mygdx.game.desktop;

//NIGHTLY

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

//LATEST
/*
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
*/

import com.mygdx.game.FlowerPowerGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		// LATEST:
/*
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = FlowerPowerGame.HEIGHT;
		config.width = FlowerPowerGame.WIDTH;
		new LwjglApplication(new FlowerPowerGame(new fireBaseInterfaceDesktop()), config);
*/

		// NIGHTLY:

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(FlowerPowerGame.WIDTH,FlowerPowerGame.HEIGHT);
		new Lwjgl3Application(new FlowerPowerGame(new fireBaseInterfaceDesktop()), config);

	}
}
