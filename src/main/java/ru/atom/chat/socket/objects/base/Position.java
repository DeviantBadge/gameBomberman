package ru.atom.chat.socket.objects.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.atom.chat.socket.message.request.InGameMovement;

public class Position {
    private static Logger log = LoggerFactory.getLogger(InGameMovement.class);
    private final Integer x;
    private final Integer y;

    public Position(@NotNull Integer x, @NotNull Integer y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    @Override
    public String toString() {
        return "{x:" + x + "y:" + y + "}";
    }
}
