package ru.atom.chat.socket.message.response.messagedata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import ru.atom.chat.socket.enums.MovementType;

public class Possess {
    private final int h;
    private final int w;
    private final int possess;


    public Possess(int h, int w, int possess){
        this.h = h;
        this.w = w;
        this.possess = possess;
    }

    public int getH() {
        return h;
    }

    public int getW() {
        return w;
    }

    public int getPossess() {
        return possess;
    }
}
