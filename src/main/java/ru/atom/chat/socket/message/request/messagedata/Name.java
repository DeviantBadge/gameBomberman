package ru.atom.chat.socket.message.request.messagedata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import ru.atom.chat.socket.enums.MovementType;

public class Name {
    private final String name;


    @JsonCreator
    public Name(
            @JsonProperty("name") String name)
            throws IllegalArgumentException {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "{name:" + name + "}";
    }
}
