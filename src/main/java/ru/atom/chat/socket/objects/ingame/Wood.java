package ru.atom.chat.socket.objects.ingame;

import org.jetbrains.annotations.NotNull;
import ru.atom.chat.socket.objects.base.Position;
import ru.atom.chat.socket.objects.base.Tile;

public class Wood extends Tile {

    public Wood(@NotNull Position position) {
        super("Wood", position, true);
    }

    public Wood(@NotNull Integer x, @NotNull Integer y) {
        super("Wood", new Position(x, y), true);
    }
}
