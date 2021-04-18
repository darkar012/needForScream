package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

	
	private String id;
	private Socket socket;
	private BufferedWriter writer;
	private OnMessageListener OML;
	//private PApplet app;

	public Session (Socket socket) {
		this.socket = socket;
		this.id = UUID.randomUUID().toString();
		
	}



	public void run() {
		try {

			InetAddress i = socket.getInetAddress();
			String ip = i.toString();
			System.out.println(ip);
			
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();

			writer = new BufferedWriter(new OutputStreamWriter(os));

			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader breader = new BufferedReader(isr);

			while (true) {

				System.out.println("Esperando mensaje...");
				String mensajeRecibido = breader.readLine(); 
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
}
