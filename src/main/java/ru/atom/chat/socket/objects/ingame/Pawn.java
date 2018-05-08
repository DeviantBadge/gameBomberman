package ru.atom.chat.socket.objects.ingame;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;
import ru.atom.chat.socket.objects.base.GameObject;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.enums.ObjectType;

public class Pawn extends GameObject {
    public static enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private Direction direction;

    public Pawn(@NotNull Position position) {
        super(ObjectType.Pawn, position);
        setDirection(Direction.DOWN);
    }

    public Pawn(@NotNull Integer x, @NotNull Integer y) {
        super(ObjectType.Pawn, new Position(x,y));
        setDirection(Direction.DOWN);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    @JsonIgnore
    protected String getEntrails() {
        return super.getEntrails() + ",direction" + direction;
    }

    @Override
    @JsonIgnore
    public boolean isDestroyable() {
        return true;
    }
}
