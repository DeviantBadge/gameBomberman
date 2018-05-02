package ru.atom.chat.socket.objects.ingame;

import org.jetbrains.annotations.NotNull;
import ru.atom.chat.socket.objects.base.Position;
import ru.atom.chat.socket.objects.base.Tile;

public class Wall extends Tile {

    public Wall(@NotNull Position position) {
        super("Wall", position, false);
    }

    public Wall(@NotNull Integer x, @NotNull Integer y) {
        super("Wall", new Position(x, y), false);
    }
}
