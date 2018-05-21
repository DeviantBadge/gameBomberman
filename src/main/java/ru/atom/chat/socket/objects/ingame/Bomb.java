package ru.atom.chat.socket.objects.ingame;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;
import ru.atom.chat.socket.objects.base.GameObject;
import ru.atom.chat.socket.objects.base.interfaces.Ticking;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.enums.ObjectType;
import ru.atom.chat.socket.objects.gamesession.GameSession;

public class Bomb extends GameObject implements Ticking {

    private static long LOOP_TIME = 5_000; // 5 seconds
    private Pawn owner;
    @JsonIgnore
    private long timeLeft = LOOP_TIME;

    public Bomb(@NotNull Position position, @NotNull Pawn owner) {
        super(ObjectType.Bomb, position);
        this.owner = owner;
    }

    public Bomb(@NotNull Integer x, @NotNull Integer y, @NotNull Pawn owner) {
        super(ObjectType.Bomb, new Position(x, y));
        this.owner = owner;
    }

    @Override
    public void start() {
        timeLeft = LOOP_TIME;
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
