package com.Juliano1612.cadeira.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.Juliano1612.cadeira.CiCADa;

public class DesktopLauncher {
	public static void main (String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Cicada";
		config.useGL30 = true;
		config.height = 800;
		config.width = 1200;
		new LwjglApplication(new CiCADa(), config);
	}
}
