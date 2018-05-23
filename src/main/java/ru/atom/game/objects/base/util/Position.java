package ru.atom.game.objects.base.util;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.atom.game.socket.message.request.messagedata.InGameMovement;

public class Position {
    private static Logger log = LoggerFactory.getLogger(InGameMovement.class);
    private final Double x;
    private final Double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Position(@NotNull Position position) {
        this.x = position.x;
        this.y = position.y;
    }

    @JsonGetter("x")
    public Integer getIntX() {
        return x.intValue();
    }

    @JsonGetter("y")
    public Integer getIntY() {
        return y.intValue();
    }

    @JsonIgnore
    public Double getX() {
        return x;
    }

    @JsonIgnore
    public Double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "{x:" + x + "y:" + y + "}";
    }

    @Override
    public boolean equals(Object anObject) {
        if(anObject == null) {
            return false;
        }
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof Position) {
            Position pos = (Position) anObject;
            return pos.x.equals(this.x) && pos.y.equals(this.y);
        }
        return false;
    }

    @JsonIgnore
    public Position getCenter() {
        return new Position(
                getX() + SizeParam.CELL_SIZE_X / 2,
                getY() + SizeParam.CELL_SIZE_Y / 2
        );
    }
}
