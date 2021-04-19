package main;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;

public class Main extends PApplet implements OnMessageListener{

	private TcpConnection tcp;
	private String urlImage;
	private int screen = 1;
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

	@Override
	public void OnMessage(Session s, String msg) {
		//System.out.println("mensaje recibido de " + s.getID() + ": "+ msg);
		
		gameScreen.OnMessage(s, msg);
	}
	
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
	
	public void setSessions() {
		gameScreen.setSessions(tcp.getSessions());
	}

	public void mousePressed() {

		System.out.println("Mouse X= "+mouseX+" - Mouse Y= "+mouseY);
		
		gameScreen.buttons();
		

	}
}


