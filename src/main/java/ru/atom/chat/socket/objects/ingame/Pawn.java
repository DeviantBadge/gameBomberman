package ru.atom.chat.socket.objects.ingame;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;
import ru.atom.chat.socket.enums.Direction;
import ru.atom.chat.socket.objects.base.GameObject;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.enums.ObjectType;

public class Pawn extends GameObject {
    private Direction direction;
    private boolean alive = true;
    private boolean moving = false;
    // BONUSES
    private int blowRange = 1;
    private int maxBombAmount = 1;
    private int speedBonus = 1;
    // BOMBS
    private int bombCount = 0;

    public Pawn(@NotNull Position position) {
        super(ObjectType.Pawn, position);
        setDirection(Direction.DOWN);
    }

    public Pawn(@NotNull Integer x, @NotNull Integer y) {
        super(ObjectType.Pawn, new Position(x, y));
        setDirection(Direction.DOWN);
    }

    //setters
    public void die() {
        this.alive = false;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    //Getters
    public Direction getDirection() {
        return direction;
    }

    public boolean isAlive() {
        return alive;
    }

    //Also getters, but ignored
    @JsonIgnore
    public boolean isMoving() {
        return moving;
    }

    @JsonIgnore
    public boolean isMoved() {
        return !moving;
    }

    public void incMaxBombAmount() {
        maxBombAmount++;
    }

    public void incSpeedBonus() {
        speedBonus++;
    }

    public void incBlowRange() {
        blowRange++;
    }

    public void incBombCount() {
        bombCount++;
    }

    public void decBombCount() {
        bombCount--;
    }

    @JsonIgnore
    public int getBlowRange() {
        return blowRange;
    }

    @JsonIgnore
    public int getMaxBombAmount() {
        return maxBombAmount;
    }

    @JsonIgnore
    public int getSpeedBonus() {
        return speedBonus;
    }

    @JsonIgnore
    public int getBombCount() {
        return bombCount;
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

    @Override
    @JsonIgnore
    public boolean isDestroyed() {
        return !isAlive();
    }

    @Override
    public boolean destroy() {
        die();
        return true;
    }
}
