package com.w4ll3.cadeira.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.w4ll3.cadeira.CADeira;

public class DesktopLauncher {
	public static void main (String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "CADeira";
		config.useGL30 = true;
		config.height = 800;
		config.width = 1200;
		new LwjglApplication(new CADeira(), config);
	}
}
