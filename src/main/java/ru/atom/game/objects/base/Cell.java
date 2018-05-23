package ru.atom.game.objects.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.atom.game.enums.ObjectType;
import ru.atom.game.objects.base.util.Position;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private static final Logger log = LoggerFactory.getLogger(Cell.class);
    private final List<GameObject> objects = new ArrayList<>();
    private Position position;


    public Cell(Position position) {
        this.position = position;
    }

    // TODO mb it isnt good to do such things :3
    public void addObject(GameObject object) {
        if (!objects.contains(object)) {
            objects.add(object);
        } else
            log.warn("You trying to add existing object to cell");

        if (object.getType() != ObjectType.Pawn) {
            if(!position.equals(object.getPosition())) {
                log.warn("Incorrect object position, it must be equals to cell position");
            }
            object.setPosition(position);
        }

    }

    public void removeObject(GameObject object) {
        objects.remove(object);
    }

    public void removeObject(int object) {
        objects.remove(object);
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
        Position center = objectPosition.getCenter();

        return  (center.getX() < position.getX() + 32) &&
                (center.getX() >= position.getX()) &&
                (center.getY() < position.getY() + 32) &&
                (center.getY() >= position.getY());
    }

    public void deleteDestroyed() {
        objects.removeIf(GameObject::isDestroyed);
    }

    public boolean isEmpty() {
        return objects.isEmpty();
    }
}
