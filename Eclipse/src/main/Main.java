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
			setImage(urlImage);
			switch (screen) {
			case 1: 
				urlImage = "../imagenes/pantallaInicio.png";
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
		}

	}
