package com.example.needforscreamcliente;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpConnection extends Thread {
    private static TcpConnection unicainstancia;

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

    public void setObserver(OnMessageListener observer) {
        this.observer = observer;
    }

    public void run() {
        try {
            socket = new Socket("10.0.2.2", 5000);
observer.OnMessage("conectado");
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            writer = new BufferedWriter(osw);


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

