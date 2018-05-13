package ru.atom.chat.socket.message.request.messagedata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import ru.atom.chat.socket.enums.MovementType;

public class InGameMovement {
    private final MovementType direction;
    private final String data;


    @JsonCreator
    public InGameMovement(
            @JsonProperty("direction") MovementType direction,
            @JsonProperty("data") JsonNode data)
            throws IllegalArgumentException {
        this.direction = direction;
        this.data = data.toString();
    }

    public String getData() {
        return data;
    }

    public MovementType getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "{direction:" + direction + "data:" + data + "}";
    }
}
