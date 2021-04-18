package main;

import processing.core.PApplet;
import processing.core.PImage;

public class Main extends PApplet implements OnMessageListener{

	private TcpConnection tcp;
	private PImage img;
	private String urlImage;
	private int screen = 1;

	public static void main(String[] args) {
		PApplet.main("main.Main");
	}

	public void settings() {
		size(1200, 700);
	}

	public void setup() {
		tcp = TcpConnection.getInstance();
		tcp.setObserver(this);

	}

	public void draw () {
		background(255);
System.out.println("mouseX: "+ mouseX);
System.out.println("mouseY: "+ mouseY);
		switch (screen) {

		case 1: 
			urlImage = "../imagenes/pantallaInicio.png";
			setImage(urlImage);
			break;
		case 2:
			break;
		}
	}

	@Override
	public void OnMessage(Session s, String msg) {
		// TODO Auto-generated method stub

	}
	public void setImage(String url) {
		img = loadImage(url);
		img.resize(1200,700);
		image(img, 0, 0);
	}

	public void mousePressed() {

		switch (screen) {
		case 1: 
			if (mouseX > 335 && mouseX < 575 
					&& mouseY > 332 && mouseY < 427) {
				
			}
			break;
		case 2:
			break;

		}

	}
}


