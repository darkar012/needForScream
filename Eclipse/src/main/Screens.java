package main;

import processing.core.PApplet;
import processing.core.PImage;

public class Screens {

	
	private PImage inicioScr, instruccionesScr, conexionScr, juegoScr;
	private PImage p1estado, p2estado;
	private PApplet app;
	
	private int numScreen=1;
	
	public Screens(PApplet app) {
		this.app=app;
		inicioScr = app.loadImage("../imagenes/inicio.png");
		instruccionesScr = app.loadImage("../imagenes/instrucciones.png");
		conexionScr = app.loadImage("../imagenes/conexion.png");
		juegoScr = app.loadImage("../imagenes/juego.png");
		
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
			
			app.image(conexionScr, 0, 0);
			
			break;
			
		case 4:
			
			app.image(juegoScr, 0, 0);
			
			break; 
			
		default:
			break;
		}
		
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
}
