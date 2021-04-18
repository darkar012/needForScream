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


public class TcpConnection extends Thread{

	private static TcpConnection unicainstancia;
	private ServerSocket server;

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
	private ArrayList<Session> sessions;

	public void setObserver(OnMessageListener observer){
		this.OML = observer;
	}

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
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ArrayList<Session> getSessions() {
		return sessions;

	}
}
