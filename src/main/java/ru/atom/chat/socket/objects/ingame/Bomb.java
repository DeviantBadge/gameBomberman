package ru.atom.chat.socket.objects.ingame;

import org.jetbrains.annotations.NotNull;
import ru.atom.chat.socket.objects.base.GameObject;
import ru.atom.chat.socket.objects.base.Position;

public class Bomb extends GameObject {

    public Bomb(@NotNull Position position) {
        super("Bomb", position);
    }

    public Bomb(@NotNull Integer x, @NotNull Integer y) {
        super("Bomb", new Position(x, y));
    }
}
