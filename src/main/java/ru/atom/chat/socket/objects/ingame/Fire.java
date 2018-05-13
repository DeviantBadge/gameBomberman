package ru.atom.chat.socket.objects.ingame;

import org.jetbrains.annotations.NotNull;
import ru.atom.chat.socket.objects.base.GameObject;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.enums.ObjectType;

public class Fire extends GameObject {
    public Fire(@NotNull Position position) {
        super(ObjectType.Fire, position);
    }

    public Fire(@NotNull Integer x, @NotNull Integer y) {
        super(ObjectType.Fire, new Position(x, y));
    }

}
