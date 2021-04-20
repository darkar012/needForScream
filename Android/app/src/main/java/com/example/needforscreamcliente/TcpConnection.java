package com.example.needforscreamcliente;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

//Esta clase usa el patrón Singleton para hacer una única instancia
//y así poder enviar o recibir mensajes del servidor.
public class TcpConnection extends Thread {
    private static TcpConnection unicainstancia;

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

    private Socket socket;
    private BufferedWriter writer;
    private OnMessageListener observer;

    //Referencia al observador que llega en el TCP.
    public void setObserver(OnMessageListener observer) {
        this.observer = observer;
    }

    //Inicia el hilo de la conexión TCP.
    public void run() {
        try {

            socket = new Socket("192.168.1.7", 5000);


            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            writer = new BufferedWriter(osw);
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader breader = new BufferedReader(isr);

            //Lee siempre los mensajes que llegan
            while (true) {

                String mensajeRecibido = breader.readLine();
                observer.OnMessage(mensajeRecibido);

            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Método para enviar mensajes al servidor.
    public void enviar(String msg) {
        new Thread(
                () -> {
                    try {
                        writer.write(msg + "\n");
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }
}

