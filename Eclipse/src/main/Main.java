package main;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;

public class Main extends PApplet implements OnMessageListener{

	private TcpConnection tcp;
	private String urlImage;
	private Screens gameScreen;
	private PApplet app;
	private boolean todosConectados = true;

	public static void main(String[] args) {
		PApplet.main("main.Main");
	}



	public void settings() {
		size(1200, 700);
	}

	public void setup() {
		this.app = this;
		gameScreen = new Screens(this);
		tcp = TcpConnection.getInstance();
		tcp.setObserver(this);

	}

	public void draw () {
		background(255);

		if (todosConectados) {
			connect();
		}

		gameScreen.paintScreen();
	}

	//Método que recibe el mensaje desde android
	@Override
	public void OnMessage(Session s, String msg) {

		gameScreen.OnMessage(s, msg);
	}

	//Método que verifica que ambos jugadores estén conectados. Identifica el avatar de cada jugador.
	public void connect() {
		int conexiones = tcp.getConexiones();
		gameScreen.setConectados(conexiones);
		setSessions();
		for (int i = 0; i < tcp.getSessions().size(); i++) {
			tcp.getSessions().get(i).setApp(this);
			if (tcp.getSessions().get(i).getID() == tcp.getSessions().get(0).getID()) {
				tcp.getSessions().get(i).createAvatar(true);
			} else {
				tcp.getSessions().get(i).createAvatar(false);
			}
		}
		if (conexiones >= 2) {
			todosConectados = false;
		}
	}

	//Método para enviar las secciones desde la clase TcpConnection a Screen.
	public void setSessions() {
		gameScreen.setSessions(tcp.getSessions());
	}


	//Método para presionar los botones de las diferentes pantallas. Trae el método buttons() de la clase Screen
	//el cual tiene las zonas sensibles de click.
	public void mousePressed() {

		gameScreen.buttons();


	}
}


