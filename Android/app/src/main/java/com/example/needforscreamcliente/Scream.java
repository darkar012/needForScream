package com.example.needforscreamcliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.google.gson.Gson;

public class Scream extends AppCompatActivity implements OnMessageListener {
    private SoundMeter meter; //Variable para llamar al medidor de audio del celular.
    private boolean recording = false;
    private int percentage;
    private ImageView guia;
    private TcpConnection tcp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scream);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Mantiene la pantalla del celular activa. No se suspende
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Mantiene la posición en vertical en los dispositivos

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.RECORD_AUDIO
        }, 11);


        guia = findViewById(R.id.guia);

        tcp = TcpConnection.getInstance(); //Instancia de la clase TCP
        tcp.setObserver(this); //Convertir la actividad en observador

        meter = new SoundMeter(); //Inicializa el medidor
    }

    //Método que normaliza y convierte en porcentaje los valores recibidos por los microfonos del dispositivo.
    //Luego de esto, son enviados al servidor, donde serán convertidos en velocidad.
    public void amplitudPercentage(double x) {
        percentage = (int) (x * 100) / 32768;

        Voz v = new Voz(percentage);
        Gson gson = new Gson();
        String json = gson.toJson(v);
        tcp.enviar(json);


        //If que hacen girar la aguja del acelerometro y así se genera la animación.
        // Log.e(">>>","%= "+ percentage );
        if (percentage >= 10) {
            rotate(-130, -110);
        }
        if (percentage >= 20) {
            rotate(-111, -90);
        }
        if (percentage >= 30) {
            rotate(-89, -60);
        }
        if (percentage >= 40) {
            rotate(-59, -30);
        }
        if (percentage >= 50) {
            rotate(-29, 0);
        }
        if (percentage >= 60) {
            rotate(0, 20);
        }
        if (percentage >= 70) {
            rotate(21, 40);
        }
        if (percentage >= 80) {
            rotate(41, 60);
        }
        if (percentage > 90) {
            rotate(60, 90);
        }


    }

    //Método para rotar la aguja del acelerómetro.
    //Toma un punto de pivote del elemento para hacer luego girar la figura.
    public void rotate(int degreesI, int degreesF) {
        RotateAnimation animation = new RotateAnimation(degreesI,
                degreesF,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.97f);
        animation.setDuration(1000);
        guia.startAnimation(animation);
    }

    //Método que recibe mensajes del servidor.
    @Override
    public void OnMessage(String msg) {
        runOnUiThread(
                () -> {

                    //Cada if es el caso en el que el jugador pierda o gane. El mensaje es recibido desde el servidor
                    //luego de que este valide el ganador.
                    if (msg.equals("perdio")) {
                        recording = false;
                        meter.stop(); //Se detiene el medidor del celular, y por lo tanto, no se recibe audio.
                        Intent i = new Intent(this, Win.class);
                        i.putExtra("perdio", false);
                        startActivity(i);
                        finish();
                    }
                    if (msg.equals("gano")) {
                        recording = false;
                        meter.stop(); //Se detiene el medidor del celular, y por lo tanto, no se recibe audio.
                        Intent i = new Intent(this, Win.class);
                        i.putExtra("perdio", true);
                        startActivity(i);
                        finish();
                    }
                }
        );
    }

    //Método para reiniciar el medidor del volumen del dispositivo al volver a llamar la pantalla.
    @Override
    protected void onResume() {
        super.onResume();
        meter.start();
        recording = true;
        new Thread(
                () -> {
                    while (recording) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        double amplitude = meter.getAmplitude(); //Toma la amplitud del celular y la guarda en la variab;e.

                        amplitudPercentage(amplitude);

                    }
                }
        ).start();
    }

    //Método para detener el medidor y micrófono del celular.
    protected void onPause() {
        super.onPause();
        meter.stop();
        recording = false;
    }
}