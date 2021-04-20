package com.example.needforscreamcliente;

//Clase que almacena y envia los mensajes desde al servidor.
public class Message {
    private String type = "message";
    private String msg;

    public Message(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
