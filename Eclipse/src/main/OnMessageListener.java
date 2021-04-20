package main;

import processing.core.PApplet;

//Interfaz que identifica los observadores.
public interface OnMessageListener {
	void OnMessage (Session s, String msg);

}
