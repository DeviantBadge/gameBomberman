package ru.atom.chat.socket.objects.ingame;

import org.jetbrains.annotations.NotNull;
import ru.atom.chat.socket.objects.base.GameObject;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.enums.ObjectType;

public class Fire extends GameObject {
    Fire(Integer id, Position position) {
        super(id, ObjectType.Fire, position);
    }

}
