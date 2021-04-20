package com.example.needforscreamcliente;

//Clase que almacena y envía la intensidad de los micrófonos al servidor.
public class Voz {

    private String type = "voz";
    private int percentage;

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public Voz(int percentage) {
        this.percentage = percentage;
    }
}

