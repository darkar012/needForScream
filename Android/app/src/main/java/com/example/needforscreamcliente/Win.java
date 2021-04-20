package com.example.needforscreamcliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.gson.Gson;

public class Win extends AppCompatActivity implements OnMessageListener, View.OnClickListener {

    private Button playBtn, exitBtn;
    private ConstraintLayout exitLayout;
    private boolean perdio;
    private TcpConnection tcp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gana);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Mantiene la pantalla del celular activa. No se suspende
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Mantiene la posición en vertical en los dispositivos
        playBtn = findViewById(R.id.playBtn3);
        exitBtn = findViewById(R.id.exitBtn2);
        exitLayout = findViewById(R.id.exitLayout);

        tcp = TcpConnection.getInstance(); //Instancia de la clase TCP
        tcp.setObserver(this); //Convertir la actividad en observador

        perdio = getIntent().getBooleanExtra("perdio", true);

        //Activa la pantalla de perdedor en la actividad. La pantalla de ganar está por defecto.
        if (!perdio) {
            exitLayout.setBackgroundResource(R.drawable.pierde);
        }

        playBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);

    }

    //Método para los botones.
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //Envía un mensaje al servidor y el otro jugador para volver a reiniciar el juego.
            case R.id.playBtn3:
                Message m = new Message("iniciar");
                Gson gson = new Gson();
                String str = gson.toJson(m);
                tcp.enviar(str);

                Intent i = new Intent(this, Counter.class);
                startActivity(i);
                finish();
                break;

            //Envía un mensaje al servidor y el otro jugador para cerrar las aplicaciones.
            case R.id.exitBtn2:

                Message m2 = new Message("finalizar");
                Gson gson2 = new Gson();
                String str2 = gson2.toJson(m2);
                tcp.enviar(str2);
                finishAffinity();
                break;
        }

    }

    //Recibe los mensajes desde el servidor que provenien del otro jugador.
    @Override
    public void OnMessage(String msg) {
        if (msg.equals("iniciar")) {
            Intent i = new Intent(this, Counter.class);
            startActivity(i);
            finish();
        }

        if (msg.equals("finalizar")) {
            finishAffinity();
        }
    }
}