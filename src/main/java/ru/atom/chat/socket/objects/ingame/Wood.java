package ru.atom.chat.socket.objects.ingame;

import org.jetbrains.annotations.NotNull;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.objects.base.Tile;
import ru.atom.chat.socket.enums.ObjectType;

public class Wood extends Tile {

    public Wood(@NotNull Position position) {
        super(ObjectType.Wood, position, true);
    }

    public Wood(@NotNull Integer x, @NotNull Integer y) {
        super(ObjectType.Wood, new Position(x, y), true);
    }
}
