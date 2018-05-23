package ru.atom.game.objects.ingame;

import ru.atom.game.objects.base.GameObject;
import ru.atom.game.objects.base.util.Position;
import ru.atom.game.enums.ObjectType;

public class Fire extends GameObject {
    Fire(Integer id, Position position) {
        super(id, ObjectType.Fire, position);
    }

}
