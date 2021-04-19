package com.example.needforscreamcliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Counter extends AppCompatActivity {
    private ConstraintLayout fondo;
    private int counter = 0;
    private boolean grita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        fondo = findViewById(R.id.fondo);

        loop();

    }

    public void loop() {
        new Thread(
                () -> {
                    grita = false;
                    while (!grita) {
                        for (int i = 0; i < 5; i++) {

                            try {

                                Thread.sleep(700);
                                if (counter == 0) {
                                    fondo.setBackgroundResource(R.drawable.cuenta1);
                                } else if (counter == 1) {
                                    fondo.setBackgroundResource(R.drawable.cuenta2);
                                } else if (counter == 2) {
                                    Thread.sleep(200);
                                    fondo.setBackgroundResource((R.drawable.cuenta3));
                                } else {
                                    fondo.setBackgroundResource((R.drawable.scream));
                                    grita = true;

                                }




                                counter++;

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    if (grita){
                        Intent j = new Intent(this, Scream.class);
                        startActivity(j);
                        finish();
                        return;
                    }

                }

        ).start();

    }
}