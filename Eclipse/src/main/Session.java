package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

import com.google.gson.Gson;

import processing.core.PApplet;

public class Session extends Thread{

	private PApplet app;
	private String id;
	private Socket socket;
	private BufferedWriter writer;
	private OnMessageListener OML;
	private Avatar av;
	//private PApplet app;

	public Session (Socket socket) {
		this.socket = socket;
		this.id = UUID.randomUUID().toString();
		
	}



	public Avatar getAv() {
		return av;
	}



	public void setAv(Avatar av) {
		this.av = av;
	}



	public void run() {
		try {
			
			InputStream is = socket.getInputStream();
			

			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader breader = new BufferedReader(isr);

			while (true) {

				System.out.println("Esperando mensaje...");
				String mensajeRecibido = breader.readLine(); 
				System.out.println(mensajeRecibido);
				OML.OnMessage(this, mensajeRecibido);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void setObserver(OnMessageListener observer){
		this.OML = observer;
	}

	public String getID() {
		return this.id;
	}



	public PApplet getApp() {
		return app;
	}



	public void setApp(PApplet app) {
		this.app = app;
	}

	public void confirmarJuego(String msg) {
		 new Thread(
	                () -> {
	                    try {
	                    	OutputStream os = socket.getOutputStream();
	            			writer = new BufferedWriter(new OutputStreamWriter(os));
	                        writer.write(msg + "\n");
	                        writer.flush();
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                }
	        ).start();
		
	}



	public void createAvatar(boolean b) {
		av = new Avatar(10, 550, 0, b, app);
	}
}
