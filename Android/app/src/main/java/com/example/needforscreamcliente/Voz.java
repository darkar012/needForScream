package com.example.needforscreamcliente;

public class Voz {

    private String type = "voz";
    private int percentage;

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage( int percentage) {
        this.percentage = percentage;
    }

    public Voz( int percentage) {
        this.percentage = percentage;
    }
}

