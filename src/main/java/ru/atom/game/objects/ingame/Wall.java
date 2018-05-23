package ru.atom.game.objects.ingame;

import ru.atom.game.objects.base.util.Position;
import ru.atom.game.objects.base.Tile;
import ru.atom.game.enums.ObjectType;

public class Wall extends Tile {

    Wall(Integer id, Position position) {
        super(id, ObjectType.Wall, position, false);
    }
}
