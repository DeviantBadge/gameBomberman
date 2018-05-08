package ru.atom.chat.socket.objects.ingame;

import org.jetbrains.annotations.NotNull;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.objects.base.Tile;
import ru.atom.chat.socket.enums.ObjectType;

public class Wall extends Tile {

    public Wall(@NotNull Position position) {
        super(ObjectType.Wall, position, false);
    }

    public Wall(@NotNull Integer x, @NotNull Integer y) {
        super(ObjectType.Wall, new Position(x, y), false);
    }
}
