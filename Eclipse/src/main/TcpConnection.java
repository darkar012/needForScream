package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;

//Esta clase usa el patrón Singleton para hacer una única instancia 
//y así poder enviar o recibir mensajes a los clientes.
public class TcpConnection extends Thread{

	private static TcpConnection unicainstancia;
	private ServerSocket server;

	//Crea la instancia de la clase e inicia el hilo.
	public static TcpConnection getInstance() {
		if (unicainstancia == null) {
			unicainstancia = new TcpConnection();
			unicainstancia.start();
		}
		return unicainstancia;
	}

	private TcpConnection() {
	}

	String recordatorio;
	private OnMessageListener OML;

	private ArrayList<Session> sessions; //Almacena los diferentes clientes o sesiones que se conectan.

	private int conexiones = 0;

	//Referencia al observador que llega en el TCP.
	public void setObserver(OnMessageListener observer){
		this.OML = observer;
	}

	//Inicia el hilo de la conexión TCP.
	public void run () {

		try {

			sessions = new ArrayList<Session>();

			server = new ServerSocket(5000);

			while (true) {
				System.out.println("esperando conexion");
				Socket socket = server.accept();
				
				Session session = new Session(socket);
				session.setObserver(OML);
				session.start();

				sessions.add(session);

				System.out.println("Cliente esta conectado");
				conexiones ++;
				
				//Este if confirma que ambos jugadores están conectados al servidor.
				if (conexiones == 2) {
					
					//Envía un mensaje a los clientes para activar los botones de iniciar en cada pantalla.
					for (int i = 0; i < sessions.size(); i++) {
						sessions.get(i).confirmarJuego("conectados");
					}
					
				}
				
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public ArrayList<Session> getSessions() {
		return sessions;

	}

	public int getConexiones() {
		return conexiones;
	}

	public void setConexiones(int conexiones) {
		this.conexiones = conexiones;
	}
	
	
}
