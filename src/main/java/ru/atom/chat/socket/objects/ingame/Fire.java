package ru.atom.chat.socket.objects.ingame;

import org.jetbrains.annotations.NotNull;
import ru.atom.chat.socket.objects.base.GameObject;
import ru.atom.chat.socket.objects.base.Position;

public class Fire extends GameObject {
    public Fire(@NotNull Position position) {
        super("Fire", position);
    }

    public Fire(@NotNull Integer x, @NotNull Integer y) {
        super("Fire", new Position(x, y));
    }
}
