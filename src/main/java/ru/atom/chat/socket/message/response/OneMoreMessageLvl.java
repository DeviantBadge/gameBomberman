package ru.atom.chat.socket.message.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import ru.atom.chat.socket.topics.MessageType;

public class OneMoreMessageLvl {
    private final String data;


    public OneMoreMessageLvl(String data) {
        this.data = data;
    }

    @JsonCreator
    public OneMoreMessageLvl(@JsonProperty("data") JsonNode data) {
        this.data = data.toString();
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "{data:" + data + "}";
    }
}
