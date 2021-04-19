package com.example.needforscreamcliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.google.gson.Gson;

public class Scream extends AppCompatActivity implements OnMessageListener {
    private SoundMeter meter;
    private boolean recording = false;
    private int percentage;
    private ImageView guia;
    private TcpConnection tcp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scream);


        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.RECORD_AUDIO
        }, 11);


        guia = findViewById(R.id.guia);

        tcp = TcpConnection.getInstance();
        tcp.setObserver(this);

        meter = new SoundMeter();
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
                        double amplitude = meter.getAmplitude();

                        amplitudPercentage(amplitude);

                    }
                }
        ).start();
    }

    public void amplitudPercentage(double x) {
        percentage = (int) (x * 100) / 32768;
        Voz v = new Voz(percentage);
        Gson gson = new Gson();
        String json = gson.toJson(v);
        tcp.enviar(json);


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

    public void rotate(int degreesI, int degreesF) {
        RotateAnimation animation = new RotateAnimation(degreesI,
                degreesF,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, //Como debe interpretarse pivotXValue
                RotateAnimation.RELATIVE_TO_SELF, 0.97f);
        animation.setDuration(1000);
        guia.startAnimation(animation);
    }

    @Override
    public void OnMessage(String msg) {
        runOnUiThread(
                () -> {
                    if (msg.equals("perdio")) {
                        recording = false;
                        meter.stop();
                        Intent i = new Intent(this, Win.class);
                        i.putExtra("perdio", false);
                        startActivity(i);
                        finish();
                    }
                    if (msg.equals("gano")) {
                        recording = false;
                        meter.stop();
                        Intent i = new Intent(this, Win.class);
                        i.putExtra("perdio", true);
                        startActivity(i);
                        finish();
                    }
                }
        );
    }
}