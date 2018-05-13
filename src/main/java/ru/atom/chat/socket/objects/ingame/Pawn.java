package ru.atom.chat.socket.objects.ingame;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;
import ru.atom.chat.socket.enums.Direction;
import ru.atom.chat.socket.objects.base.GameObject;
import ru.atom.chat.socket.objects.base.interfaces.Ticking;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.enums.ObjectType;

import java.util.ArrayList;

public class Pawn extends GameObject {
    private Direction direction;
    private boolean alive = true;
    private boolean moving = false;
    private int blowRange = 1;

    public Pawn(@NotNull Position position) {
        super(ObjectType.Pawn, position);
        setDirection(Direction.DOWN);
    }

    public Pawn(@NotNull Integer x, @NotNull Integer y) {
        super(ObjectType.Pawn, new Position(x, y));
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

    public void die() {
        this.alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    @JsonIgnore
    public boolean isMoving() {
        return moving;
    }

    @JsonIgnore
    public boolean isMoved() {
        return !moving;
    }

    @JsonIgnore
    public int getBlowRange() {
        return blowRange;
    }
}
