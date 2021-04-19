package com.example.needforscreamcliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        playBtn=findViewById(R.id.playBtn3);
        exitBtn=findViewById(R.id.exitBtn2);
        exitLayout=findViewById(R.id.exitLayout);

        tcp = TcpConnection.getInstance();
        tcp.setObserver(this);

        perdio= getIntent().getBooleanExtra("perdio",true);

        if (!perdio){
            exitLayout.setBackgroundResource(R.drawable.pierde);
        }

        playBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.playBtn3:
                Message m = new Message("reiniciar");
                Gson gson = new Gson();
                String str = gson.toJson(m);
                tcp.enviar(str);

                Intent i = new Intent(this, Counter.class);
                startActivity(i);
                finish();
                break;

            case R.id.exitBtn2:
                finish();
                break;
        }

    }

    @Override
    public void OnMessage(String msg) {
        
    }
}