package main;

import java.util.ArrayList;

import com.google.gson.Gson;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class Screens {


	private PImage inicioScr, instruccionesScr, conexionScr, conexionScr2, conexionScr3, juegoScr;
	private PImage p1estado, p2estado;
	private PFont font;
	private PApplet app;

	private int numScreen=1;
	private int conectados = 0;

	private ArrayList<Session> sessions;

	public Screens(PApplet app) {
		this.app=app;
		inicioScr = app.loadImage("../imagenes/inicio.png");
		instruccionesScr = app.loadImage("../imagenes/instrucciones.png");
		conexionScr = app.loadImage("../imagenes/conexion1.png");
		conexionScr2 = app.loadImage("../imagenes/conexion2.png");
		conexionScr3 = app.loadImage("../imagenes/conexionReady.png");
		
		font = app.createFont("NFS font", 32);
		app.textFont (font);

		juegoScr = app.loadImage("../imagenes/juego.png");
		sessions = new ArrayList<Session>();
	}

	public ArrayList<Session> getSessions() {
		return sessions;
	}

	public void setSessions(ArrayList<Session> sessions) {
		this.sessions = sessions;
	}

	public void paintScreen() {

		switch (numScreen) {
		case 1:

			app.image(inicioScr, 0, 0);

			break;

		case 2:

			app.image(instruccionesScr, 0, 0);

			break;

		case 3:


			if (conectados == 0) {
				app.image(conexionScr, 0, 0);
			} else if (conectados == 1) {
				app.image(conexionScr2, 0, 0);
			} else {
				app.image(conexionScr3, 0, 0);

				new Thread(

						()->{
							try {
								Thread.sleep(500);
								numScreen = 4;
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return;

						}

						).start();

			}

			break;

		case 4:

			app.image(juegoScr, 0, 0);
			
			app.text("waiting 2nd player", 1000/2, 700/2);

			for (int i = 0; i < sessions.size(); i++) {
				sessions.get(i).getAv().pintar();
				sessions.get(i).getAv().move();
			}

			break; 

		default:
			break;
		}

	}

	public int getConectados() {
		return conectados;
	}

	public void setConectados(int conectados) {
		this.conectados = conectados;
	}

	public void buttons() {

		switch (numScreen) {
		case 1:

			if (app.mouseX>332 && app.mouseX<575 && 
					app.mouseY>337 && app.mouseY<427) {

				numScreen=2;
			}

			if (app.mouseX>625 && app.mouseX<866 && 
					app.mouseY>337 && app.mouseY<427) {

				app.exit();
			}

			break;

		case 2:

			if (app.mouseX>918 && app.mouseX<1045 && 
					app.mouseY>577 && app.mouseY<615) {

				numScreen=3;
			}

			break;

		default:
			break;
		}

	}

	public void OnMessage(Session s, String msg) {
		Gson gson = new Gson();
		String Id = s.getID();
		Voz v = gson.fromJson(msg, Voz.class);
		if (sessions.get(0).getID() == Id) {
			sessions.get(0).getAv().setVel(v.getPercentage()/30);
		} else {
			sessions.get(1).getAv().setVel(v.getPercentage()/30);
		}

	}
}
