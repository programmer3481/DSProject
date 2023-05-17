package com.github.programmer3481.ganjang;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.github.programmer3481.ganjang.GanJang;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("GanJang");
		config.setWindowedMode(1536, 1536);
		config.setResizable(false);
		new Lwjgl3Application(new GanJang(), config);
	}
}
