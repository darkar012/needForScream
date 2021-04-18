package com.example.needforscreamcliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class counter extends AppCompatActivity {
    private ConstraintLayout fondo;
    private int counter = 0;

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
                    while (true) {
                        for (int i = 0; i < 20; i++) {

                            try {
                                Thread.sleep(700);

                                Log.e(">>>", "" + counter);
                                if (counter == 0) {
                                    fondo.setBackgroundResource(R.drawable.cuenta1);
                                } else if (counter == 1) {
                                    fondo.setBackgroundResource(R.drawable.cuenta2);
                                } else if (counter == 2) {
                                    Thread.sleep(200);
                                    fondo.setBackgroundResource((R.drawable.cuenta3));
                                } else {
                                    fondo.setBackgroundResource((R.drawable.scream));
                                    counter = -1;
                                    Thread.sleep(700);
                                    Intent j = new Intent(this, scream.class);
                                    startActivity(j);
                                }

                                counter++;

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }

        ).start();

}
}