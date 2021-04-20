package com.example.needforscreamcliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.gson.Gson;

public class PantallaConexion extends AppCompatActivity implements OnMessageListener, View.OnClickListener {
    private ConstraintLayout fondo;
    private Button play2, back;
    private boolean conectado = false;
    private int counter = 0;
    private TcpConnection tcp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_conexion);
        fondo = findViewById(R.id.fondo);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Mantiene la posición en vertical en los dispositivos
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Mantiene la pantalla del celular activa. No se suspende

        if (conectado == false) {
            loop();
        }

        tcp = TcpConnection.getInstance(); //Instancia de la clase TCP
        tcp.setObserver(this); //Convertir la actividad en observador

        play2 = findViewById(R.id.playBtn2);
        back = findViewById(R.id.backBtn);

        play2.setEnabled(false);

        play2.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    //Método que hace la animación de conectarse en la actividad.
    public void loop() {
        new Thread(
                () -> {
                    while (true) {
                        for (int i = 0; i < 20; i++) {
                            try {
                                Thread.sleep(300);

                                if (counter == 0) {
                                    fondo.setBackgroundResource(R.drawable.conexion1);
                                } else if (counter == 1) {
                                    fondo.setBackgroundResource(R.drawable.conexion2);
                                } else {
                                    fondo.setBackgroundResource((R.drawable.conexion3));
                                    counter = -1;
                                }
                                counter++;
                                //Log.e(">>>", "" + counter);
                                if (conectado == true) {
                                    fondo.setBackgroundResource(R.drawable.conectado);
                                    return;

                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();

    }

    //Método que recibe mensajes del servidor.
    @Override
    public void OnMessage(String msg) {
        runOnUiThread(
                () -> {

                    //Verifica que ambos jugadores esten conectados.
                    if (msg.equals("conectados")) {
                        play2.setEnabled(true);
                        conectado = true;

                    }

                    //Recibe el mensaje de iniciar la partida.
                    if (msg.equals("iniciar")) {
                        Intent i = new Intent(this, Counter.class);
                        startActivity(i);
                        finish();
                    }
                }
        );

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //Envia el mensaje de iniciar al servidor y al jugador. Luego cambia a la siguiente actividad
            case R.id.playBtn2:
                Message m = new Message("iniciar");
                Gson gson = new Gson();
                String str = gson.toJson(m);
                tcp.enviar(str);

                Intent i = new Intent(this, Counter.class);
                startActivity(i);
                finish();
                break;

            //Cerrar la aplicación.
            case R.id.backBtn:

                finish();
                break;
        }
    }
}