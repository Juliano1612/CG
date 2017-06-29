package com.Juliano1612.cadeira.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.Juliano1612.cadeira.CiCADa;

public class DesktopLauncher {

	public static void main (String[] arg) {

		//int MINWIDTH = 800;
		//int MAXWIDTH = Gdx.graphics.g;
		//int MINHEIGHT = 1000;
		//int MAXHEIGHT = Gdx.graphics.getHeight();

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		//config.setWindowSizeLimits(MINWIDTH, MINHEIGHT, MAXWIDTH,MAXHEIGHT);
		config.setTitle("CiCADa");
		config.setMaximized(true);
		//config.height = 800;
		//config.width = 1200;
		new Lwjgl3Application(new CiCADa(), config);
	}
}
