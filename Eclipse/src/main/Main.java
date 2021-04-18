package main;

import processing.core.PApplet;
import processing.core.PImage;

public class Main extends PApplet implements OnMessageListener{

	private TcpConnection tcp;
	private String urlImage;
	private int screen = 1;
	private Screens gameScreen;

	public static void main(String[] args) {
		PApplet.main("main.Main");
	}

	public void settings() {
		size(1200, 700);
	}

	public void setup() {
		
		gameScreen = new Screens(this);
		tcp = TcpConnection.getInstance();
		tcp.setObserver(this);

	}

	public void draw () {
		background(255);
		connect();
		gameScreen.paintScreen();
	}

	@Override
	public void OnMessage(Session s, String msg) {
		// TODO Auto-generated method stub
gameScreen.OnMessage(s, msg);
	}
	
	public void connect() {
		int conexiones = tcp.getConexiones();
		gameScreen.setConectados(conexiones);
	}
	
	public void setImage(String url) {
		//img = loadImage(url);
		//img.resize(1200,700);
		//image(img, 0, 0);
	}

	public void mousePressed() {

		//System.out.println("Mouse X= "+mouseX+" - Mouse Y= "+mouseY);
		
		gameScreen.buttons();
		

	}
}


