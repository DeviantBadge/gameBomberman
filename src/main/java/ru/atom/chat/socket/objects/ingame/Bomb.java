package ru.atom.chat.socket.objects.ingame;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;
import ru.atom.chat.socket.objects.base.GameObject;
import ru.atom.chat.socket.objects.base.interfaces.Ticking;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.enums.ObjectType;

public class Bomb extends GameObject implements Ticking {
    private static long LOOP_TIME = 10_000; // 10 seconds
    private long timeLeft = LOOP_TIME;

    public Bomb(@NotNull Position position) {
        super(ObjectType.Bomb, position);
    }

    public Bomb(@NotNull Integer x, @NotNull Integer y) {
        super(ObjectType.Bomb, new Position(x, y));
    }

    @Override
    public void start() {
        timeLeft = LOOP_TIME;
    }

    @Override
    public void tick(long ms) {
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
}
