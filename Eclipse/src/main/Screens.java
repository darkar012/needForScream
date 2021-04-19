package main;

import java.util.ArrayList;

import com.google.gson.Gson;


import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class Screens {


	private PImage inicioScr, instruccionesScr, conexionScr, conexionScr2, conexionScr3, juegoScr;
	private PImage p1estado, p2estado, jugador1win, jugador2win;
	private PFont font;

	private PApplet app;

	private int numScreen=1;
	private int conectados = 0;

	private boolean hayGanador=false;
	private int jgGanador=0;

	private int seg=0;
	private boolean timer=false;
	
	private int vel=0;
	private int vel2=0;
	private boolean velActivo=false;
	
	private ArrayList<Session> sessions;

	public Screens(PApplet app) {
		this.app=app;

		inicioScr = app.loadImage("../imagenes/inicio.png");
		instruccionesScr = app.loadImage("../imagenes/instrucciones.png");
		conexionScr = app.loadImage("../imagenes/conexion1.png");
		conexionScr2 = app.loadImage("../imagenes/conexion2.png");
		conexionScr3 = app.loadImage("../imagenes/conexionReady.png");

		jugador1win = app.loadImage("../imagenes/jugador1win.png");
		jugador2win = app.loadImage("../imagenes/jugador2win.png");


		font = app.createFont("../data/NFS.ttf", 28);
		app.textFont (font);


		juegoScr = app.loadImage("../imagenes/juego.png");
		sessions = new ArrayList<Session>();


	}

	public void ganar() {

		for (int i = 0; i < sessions.size(); i++) {

			int posX=sessions.get(i).getAv().getPosX();

			if (hayGanador==false) {

				if (posX>=990) {

					if (sessions.get(0).getID() == sessions.get(i).getID()) {

						jgGanador=1;

						sessions.get(i).confirmarJuego("gano");

						
					} else {

						jgGanador=2;

						sessions.get(i).confirmarJuego("gano");

					}

					hayGanador=true;

				}

			}

		}

	}
	
	public void velMax() {
		
		int contVel=0;
		int contVel2=0;
		
		if (velActivo) {
			
			contVel = sessions.get(0).getAv().getVel()*30+40;
			contVel2 = sessions.get(1).getAv().getVel()*30+40;
			
			if (contVel>vel) {
				
				vel=contVel;
			}
			
			if (contVel2>vel) {
				
				vel2=contVel2;
			}
			
		}
		
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
			}

			break;

		case 4:
		
				
				app.image(juegoScr, 0, 0);
				temporizador();
				velMax();
				for (int i = 0; i < sessions.size(); i++) {
					sessions.get(i).getAv().pintar();
					sessions.get(i).getAv().move();
				}
			


			ganar();
			pintarGanador();
			break; 

		default:
			break;
		}

	}




	public void pintarGanador() {

		if (jgGanador==1) {
			
			sessions.get(1).confirmarJuego("perdio");

			app.image(jugador1win, 118, 76);
			app.text(seg, 860, 350);
			app.text(vel, 860, 392);
			
			timer=false;
			velActivo=false;
			
			
			
		} 

		if(jgGanador==2) {

			sessions.get(0).confirmarJuego("perdio");

			
			
			app.image(jugador2win, 118, 76);
			app.text(seg, 870, 350);
			app.text(vel2, 870, 392);
			
			timer=false;
			velActivo=false;
			
			
		}

	}

	public void temporizador() {

		app.text("TIME: ",550,36);
		app.text(seg,550+150,36);

		if (timer) {

			if (app.frameCount % 60 == 0) {
				seg += 1;
			}

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

		Generic generic = gson.fromJson(msg, Generic.class);

		System.out.println(msg);

		switch (generic.type) {

		case "voz": 
			
			if (velActivo) {
				
				System.out.println("entro");
				Voz v = gson.fromJson(msg, Voz.class);
				if (sessions.get(0).getID() == Id) {
					sessions.get(0).getAv().setVel(v.getPercentage()/20);
					//System.out.println(sessions.get(0).getAv().getPosX());
				} else {
					sessions.get(1).getAv().setVel(v.getPercentage()/20);
				}
				
				timer = true;
			
			}
			break;
				

		case "message": 
			Message m = gson.fromJson(msg, Message.class);

			if (m.getMsg().equals("iniciar")) {
				System.out.println("inicio");
				for (int i = 0; i < sessions.size(); i++) {
					sessions.get(i).confirmarJuego("iniciar");
					numScreen = 4;
					
					velActivo=true;
					hayGanador =false;
					jgGanador=0;
					
					for (int j = 0; j < sessions.size(); j++) {
						sessions.get(j).getAv().setVel(0);
						sessions.get(j).getAv().setPosX(10);
					}

					seg=0;
					timer=false;
					
					vel=0;
					vel2=0;
				}
			}
			
			/*if (m.getMsg().equals("reiniciar")) {
				for (int i = 0; i < sessions.size(); i++) {
				sessions.get(i).confirmarJuego("reiniciar");
				}
				numScreen = 4;
				hayGanador =false;
				jgGanador=0;
				
				for (int i = 0; i < sessions.size(); i++) {
					sessions.get(i).getAv().setVel(0);
					sessions.get(i).getAv().setPosX(10);
				}

				seg=0;
				timer=false;
				
				vel=0;
				vel2=0;
				

			}*/
			
			if (m.getMsg().equals("finalizar")) {
				
				for (int i = 0; i < sessions.size(); i++) {
					sessions.get(i).confirmarJuego("finalizar");
					}
				app.exit();
				

			}
			break;	
		}
	}


}

