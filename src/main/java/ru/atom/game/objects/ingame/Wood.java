package ru.atom.game.objects.ingame;

import ru.atom.game.objects.base.util.Position;
import ru.atom.game.objects.base.Tile;
import ru.atom.game.enums.ObjectType;

public class Wood extends Tile {

    Wood(Integer id, Position position) {
        super(id, ObjectType.Wood, position, true);
    }
}
