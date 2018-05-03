package ru.atom.chat.socket.objects.base;

import ru.atom.chat.socket.objects.base.interfaces.Destroyable;
import ru.atom.chat.socket.objects.base.interfaces.Ticking;

import java.util.ArrayList;
import java.util.List;

public class Cell implements Ticking , Destroyable {
    private final List<GameObject> objects = new ArrayList<>();
    private final List<GameObject> changedObjects = new ArrayList<>();
    private Position position;

    public Cell(Position position) {
        this.position = position;
    }

    public void addObject(GameObject object) {
        if (!objects.contains(object)) {
            objects.add(object);
            changedObjects.add(object);
        }
        if (!object.getType().equals("Pawn")) {
            if (!position.equals(object.getPosition())) {
                object.setPosition(position);
                if (!changedObjects.contains(object))
                    changedObjects.add(object);
            }
        }
    }

    public void removeObject(GameObject object) {
        objects.remove(object);
        if (!object.getType().equals("Pawn")) {
            if (!changedObjects.contains(object))
                changedObjects.add(object);
        }
    }

    public List<GameObject> getChangedObjects() {
        List<GameObject> update = new ArrayList<>(changedObjects);
        changedObjects.clear();
        return update;
    }

    public GameObject get(int i) {
        return objects.get(i);
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public void tick(int ms) {

    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public boolean isDestroyable() {
        return false;
    }

    @Override
    public boolean destroy() {
        return false;
    }
}
