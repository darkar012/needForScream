package com.example.needforscreamcliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

        if (conectado==false) {
            loop();
        }

        tcp = TcpConnection.getInstance();
        tcp.setObserver(this);

        play2 = findViewById(R.id.playBtn2);
        back = findViewById(R.id.backBtn);

        play2.setEnabled(false);

        play2.setOnClickListener(this);
        back.setOnClickListener(this);
    }

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
    if (conectado == true){
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

    @Override
    public void OnMessage(String msg) {
        runOnUiThread(
                ()->{
                    if (msg.equals("conectados")) {
                        play2.setEnabled(true);
                        conectado = true;

                    }
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
            case R.id.playBtn2:
                Message m = new Message("iniciar");
                Gson gson = new Gson();
                String str = gson.toJson(m);
                tcp.enviar(str);

                Intent i = new Intent(this, Counter.class);
                startActivity(i);
                finish();
                break;
            case R.id.backBtn:
                finish();
                break;
        }
    }
}