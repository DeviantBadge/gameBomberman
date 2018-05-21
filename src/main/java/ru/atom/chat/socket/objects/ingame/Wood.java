package ru.atom.chat.socket.objects.ingame;

import org.jetbrains.annotations.NotNull;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.objects.base.Tile;
import ru.atom.chat.socket.enums.ObjectType;

public class Wood extends Tile {

    Wood(Integer id, Position position) {
        super(id, ObjectType.Wood, position, true);
    }
}
