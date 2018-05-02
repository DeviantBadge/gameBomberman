package ru.atom.chat.socket.objects.ingame;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;
import ru.atom.chat.socket.objects.base.GameObject;
import ru.atom.chat.socket.objects.base.Position;

public class Pawn extends GameObject {
    public static enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private Direction direction;

    public Pawn(@NotNull Position position, @NotNull Direction direction) {
        super("Pawn", position);
        this.direction = direction;
    }

    public Pawn(@NotNull Integer x, @NotNull Integer y, @NotNull Direction direction) {
        this(new Position(x, y), direction);
    }

    public Pawn(@NotNull Position position) {
        this(position, Direction.DOWN);
    }

    public Pawn(@NotNull Integer x, @NotNull Integer y) {
        this(new Position(x,y),Direction.DOWN);
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


}
