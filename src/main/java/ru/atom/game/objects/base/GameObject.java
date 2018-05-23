package ru.atom.game.objects.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.atom.game.objects.base.interfaces.Destroyable;
import ru.atom.game.objects.base.interfaces.Replicable;
import ru.atom.game.objects.base.util.Position;
import ru.atom.game.enums.ObjectType;

public abstract class GameObject implements Destroyable, Replicable {
    protected final static Logger log = LoggerFactory.getLogger(GameObject.class);
    // TODO mb its better to give object id from game session
    // TODO make GameObject positionable
    private final Integer id;
    private final ObjectType type;

    private Position position;
    private boolean destroyed = false;

    public GameObject(Integer id, ObjectType type, Position position) {
        this.id = id;
        this.type = type;
        this.position = position;
    }

    public Integer getId() {
        return id;
    }

    public ObjectType getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @JsonIgnore
    protected String getEntrails() {
        return "id:" + id + ",type:" + type + ",position:" + position;
    }

    @Override
    public String toString() {
        return "{" + getEntrails() + "}";
    }

    @Override
    @JsonIgnore
    public boolean isDestroyable() {
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    // for example we have player, it could be destroyed (killed) but it doesn`t mean that we have to delete it from game
    // but for common objects its wrong
    @JsonIgnore
    public boolean isDeleted() {
        return destroyed;
    }

    @Override
    public boolean destroy() {
        if(isDestroyable() && !isDestroyed()) {
            return destroyed = true;
        }
        return false;
    }
}
