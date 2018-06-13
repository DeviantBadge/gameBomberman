package ru.atom.game.objects.ingame;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.atom.game.objects.base.Cell;
import ru.atom.game.objects.base.GameObject;
import ru.atom.game.objects.base.interfaces.Ticking;
import ru.atom.game.objects.base.util.Point;
import ru.atom.game.enums.ObjectType;

public class Bomb extends GameObject implements Ticking {

    private Pawn owner;
    @JsonIgnore
    private final long loopTime; // 5 seconds
    @JsonIgnore
    private long timeLeft;

    Bomb(Integer id, Point point, Pawn owner, long loopTime, boolean blocking) {
        super(id, ObjectType.Bomb, point, new Point(0,0), Cell.CELL_SIZE_X, Cell.CELL_SIZE_Y, blocking);
        this.owner = owner;
        this.loopTime = loopTime;
        this.timeLeft = loopTime;
    }

    @Override
    public void start() {
        timeLeft = loopTime;
    }

    @Override
    public void stop() {
        timeLeft = 0;
    }

    @Override
    public void tick(long ms) {
        if(isReady())
            log.warn("Error in GameObject with type " + getType() + ", ticking while object is ready");
        timeLeft -= ms;
    }

    @Override
    @JsonIgnore
    public boolean isReady() {
        return (timeLeft <= 0);
    }

    @Override
    @JsonIgnore
    public boolean isDestroyable() {
        return true;
    }

    @JsonIgnore
    public Pawn getOwner() {
        return owner;
    }
}
