package ru.atom.game.objects.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.atom.game.objects.base.interfaces.Destroyable;
import ru.atom.game.objects.base.interfaces.Replicable;
import ru.atom.game.objects.base.util.ColliderFrame;
import ru.atom.game.objects.base.util.Point;
import ru.atom.game.enums.ObjectType;

import javax.swing.text.Position;

public abstract class GameObject extends ColliderFrame implements Destroyable, Replicable {
    protected final static Logger log = LoggerFactory.getLogger(GameObject.class);
    // TODO make GameObject positionable
    private final Integer id;
    private final ObjectType type;
    private final boolean blocking;
    private boolean destroyed = false;


    public GameObject(Integer id, ObjectType type, Point absolutePosition,
                      Point colliderShift, Double colliderSizeX, Double colliderSizeY, boolean blocking) {
        super(absolutePosition, colliderSizeX, colliderSizeY, colliderShift);
        this.id = id;
        this.type = type;
        this.blocking = blocking;
    }

    public Integer getId() {
        return id;
    }

    public ObjectType getType() {
        return type;
    }

    @JsonIgnore
    protected String getEntrails() {
        return "id:" + id + ",type:" + type + ",frame:" + super.toString();
    }

    @JsonIgnore
    public boolean isBlocking() {
        return blocking;
    }

    // for example we have player, it could be destroyed (killed) but it doesn`t mean that we have to delete it from game
    // but for common objects its wrong
    @JsonIgnore
    public boolean isDeleted() {
        return destroyed;
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

    @Override
    public boolean destroy() {
        if (isDestroyable() && !isDestroyed()) {
            return destroyed = true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "{" + getEntrails() + "}";
    }

    @Override
    public boolean equals(Object anObject) {
        if (anObject == null) {
            return false;
        }
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof GameObject) {
            return id.equals(((GameObject) anObject).id);
        }
        return false;
    }
}
