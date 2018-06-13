package ru.atom.game.objects.ingame;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.atom.game.enums.Direction;
import ru.atom.game.objects.base.Cell;
import ru.atom.game.objects.base.GameObject;
import ru.atom.game.objects.base.util.CellsSpace;
import ru.atom.game.objects.base.util.Point;
import ru.atom.game.enums.ObjectType;

import java.util.ArrayList;

public class Pawn extends GameObject {
    private Direction direction;
    private boolean alive = true;
    private boolean moving = false;
    private boolean moved = false;

    // BONUSES
    private int blowRange;
    private int maxBombAmount;
    private int speedBonus;

    // BOMBS
    private int bombCount = 0;

    Pawn(Integer id, Point point, Point colliderShift, Double colSizeX, Double colSizeY, boolean blocking,
         int blowRange, int maxBombAmount, int speedBonus) {
        super(id, ObjectType.Pawn, point, colliderShift, colSizeX, colSizeY, blocking);
        this.blowRange = blowRange;
        this.maxBombAmount = maxBombAmount;
        this.speedBonus = speedBonus;
        setDirection(Direction.DOWN);
    }

    //setters
    public void die() {
        this.alive = false;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    //Getters
    @JsonIgnore
    public Direction getDirection() {
        return direction;
    }

    @JsonProperty("direction")
    private String getDirectionString() {
        if(moved) {
            moved = false;
            return direction.toString();
        }
        else
            return "";
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
    @JsonIgnore
    public boolean isDeleted() {
        return false;
    }

    @Override
    public boolean destroy() {
        die();
        return true;
    }

    public void riseAgain() {
        alive = true;
    }
}
