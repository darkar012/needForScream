package main;

import processing.core.PApplet;
import processing.core.PImage;

public class Avatar extends PApplet{
	
	private int posX, posY, vel;
	private PImage car1;
	private PImage car2;
	private boolean isP1;
	private PApplet app;
	
	
	
	
	public Avatar(int posX, int posY, int vel, boolean isP1, PApplet app) {
		
		this.posX = posX;
		this.posY = posY;
		this.vel = vel;
		car1 = app.loadImage("../imagenes/redCar.png");
		car2 = app.loadImage("../imagenes/grayCar.png");
		this.isP1 = isP1;
		this.app = app;
		
	}
	
	public void pintar() {
		
		if (isP1) {
			app.image(car2, posX, posY);
		} else {
			app.image(car1, posX, posY+80);
		}
	}
	
	
	
	public void move() {
		
		if(posX > 0 && posX+210 < 1200) {
			posX = posX+vel;
			System.out.println(posX);
		}
		
	}

	public int getPosX() {
		return posX;
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}
	public int getPosY() {
		return posY;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}
	public int getVel() {
		return vel;
	}
	public void setVel(int vel) {
		this.vel = vel;
	}
	public PImage getCar1() {
		return car1;
	}
	public void setCar1(PImage car1) {
		this.car1 = car1;
	}
	public PImage getCar2() {
		return car2;
	}
	public void setCar2(PImage car2) {
		this.car2 = car2;
	}
	public boolean isP1() {
		return isP1;
	}
	public void setP1(boolean isP1) {
		this.isP1 = isP1;
	}
	
	

}
