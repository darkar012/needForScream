package main;

import java.util.ArrayList;

import com.google.gson.Gson;


import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class Screens {

	//Variables para imagenes de diversos elementos.
	private PImage inicioScr, instruccionesScr, conexionScr, conexionScr2, conexionScr3, juegoScr;
	private PImage p1estado, p2estado, jugador1win, jugador2win;
	
	private PFont font; //Variable para la tipografía

	private PApplet app;

	private int numScreen=1; //Variable para cambiar las pantallas
	private int conectados = 0; //Variable para saber los jugadores.

	//Variables para definir el ganador.
	private boolean hayGanador=false;
	private int jgGanador=0;

	//Variables para el temporizador.
	private int seg=0;
	private boolean timer=false;

	//Variables de velocidad máxima para cada jugador.
	private int vel=0;
	private int vel2=0;
	private boolean velActivo=false;

	private ArrayList<Session> sessions; //Almacena los clientes conectados al servidor.

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

	//Método que valida cuál jugador llegó a la meta y ganó.
	//Al ganar, cambia variables globales que afectan otros métodos y muestran en pantalla quien ganó.
	public void ganar() {

		for (int i = 0; i < sessions.size(); i++) {

			int posX=sessions.get(i).getAv().getPosX();

			if (hayGanador==false) {

				if (posX>=990) {

					//Envía el mensaje al jugador 1 de que ganó, para así mostrarlo en pantalla.
					if (sessions.get(0).getID() == sessions.get(i).getID()) {

						jgGanador=1;

						sessions.get(i).confirmarJuego("gano");

						//Envía el mensaje al jugador 2 de que ganó, para así mostrarlo en pantalla.
					} else {

						jgGanador=2;

						sessions.get(i).confirmarJuego("gano");

					}

					hayGanador=true;

				}

			}

		}

	}

	//Método que guarda la velocidad máxima alcanzada por cada jugador.
	public void velMax() {

		int contVel=0;
		int contVel2=0;

		if (velActivo) {

			//Variables para contener la velocidad de cada jugador
			contVel = sessions.get(0).getAv().getVel()*30+40;
			contVel2 = sessions.get(1).getAv().getVel()*30+40;

			//Los siguientes if comparan cada contenedor de velocidad con la variable global de velocidad
			//y así poder ir definiendo cual ha sido la máxima alcanzada por el jugador.
			if (contVel>vel) {

				vel=contVel;
			}

			if (contVel2>vel) {

				vel2=contVel2;
			}

		}

	}

	//Método para pintar las diferentes pantallas y algunos elementos de ellas.
	public void paintScreen() {

		switch (numScreen) {
		case 1:

			app.image(inicioScr, 0, 0);

			break;

		case 2:

			app.image(instruccionesScr, 0, 0);

			break;

		case 3:

			//Cambia la imagen de waiting a ready en la pantalla de conexión.
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
			
			//Este for muestra los carros y sus movimientos en pantalla.
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

	//Método que pinta en pantalla el ganador con el tiempo que tardó en llegar a la meta y la "velocidad" máxima alcanzada.
	public void pintarGanador() {

		if (jgGanador==1) {

			sessions.get(1).confirmarJuego("perdio");//Mensaje al perdedor

			app.image(jugador1win, 118, 76);
			app.text(seg, 860, 350);
			app.text(vel, 860, 392);

			timer=false;
			velActivo=false;



		} 

		if(jgGanador==2) {

			sessions.get(0).confirmarJuego("perdio");//Mensaje al perdedor



			app.image(jugador2win, 118, 76);
			app.text(seg, 870, 350);
			app.text(vel2, 870, 392);

			timer=false;
			velActivo=false;


		}

	}

	//Método que inicia el tiempo en la partida al momento en el que ambos jugadores empiezan a jugar.
	public void temporizador() {

		app.text("TIME: ",550,36);
		app.text(seg,550+150,36);

		if (timer) {

			if (app.frameCount % 60 == 0) {
				seg += 1;
			}

		}
	}

	//Método que define las zonas sensibles de las pantallas con botones.
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

	//En este método se reciben mensajes y envía mensajes al dispositivo android.
	//los mensajes recibidos se interpretan para llevar a cabo diferentes acciones en el juego.
	public void OnMessage(Session s, String msg) {
		Gson gson = new Gson();
		String Id = s.getID();

		Generic generic = gson.fromJson(msg, Generic.class);

		System.out.println(msg);

		switch (generic.type) {

		//Este caso recibe los datos del micrófono del jugador provenientes de android y hace mover a los autos.
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

		//Envía el mensaje de iniciar a los dispositivos par así iniciar la partida a la vez
		//También funciona para reiniciar.
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

			//Este if cierra las aplicaciones de android y también el juego en eclipse.
			if (m.getMsg().equals("finalizar")) {

				for (int i = 0; i < sessions.size(); i++) {
					sessions.get(i).confirmarJuego("finalizar");
				}
				app.exit();


			}
			break;	
		}
	}
	
	public int getConectados() {
		return conectados;
	}

	public void setConectados(int conectados) {
		this.conectados = conectados;
	}

	public ArrayList<Session> getSessions() {
		return sessions;
	}

	public void setSessions(ArrayList<Session> sessions) {
		this.sessions = sessions;
	}

}

