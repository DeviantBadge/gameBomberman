package ru.atom.chat.socket.objects.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.atom.chat.socket.enums.ObjectType;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.objects.base.util.SizeParam;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private static final Logger log = LoggerFactory.getLogger(Cell.class);
    private final List<GameObject> objects = new ArrayList<>();
    private Position position;


    public Cell(Position position) {
        this.position = position;
    }

    public void addObject(GameObject object) {
        if (!objects.contains(object)) {
            objects.add(object);
        } else
            log.warn("You trying to add existing object to cell");

        if (object.getType() != ObjectType.Pawn) {
            object.setPosition(position);
        } else {
            log.warn("You trying to add pawn to cell");
        }
    }

    public GameObject get(int i) {
        return objects.get(i);
    }

    public int size() {
        return objects.size();
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public Position getPosition() {
        return position;
    }

    public boolean contains(Position objectPosition) {
        int centerX = objectPosition.getX() + SizeParam.CELL_SIZE_X / 2;
        int centerY = objectPosition.getY() + SizeParam.CELL_SIZE_Y / 2;

        return  (centerX < position.getX() + 32) &&
                (centerX >= position.getX()) &&
                (centerY < position.getY() + 32) &&
                (centerY >= position.getY());
    }
}
