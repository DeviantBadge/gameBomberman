package ru.atom.chat.socket.objects.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.atom.chat.socket.objects.base.interfaces.Destroyable;
import ru.atom.chat.socket.objects.base.interfaces.Replicable;
import ru.atom.chat.socket.objects.base.util.IdGen;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.enums.ObjectType;

public abstract class GameObject implements Destroyable, Replicable {
    protected final static Logger log = LoggerFactory.getLogger(GameObject.class);
    // TODO mb its better to give object id from game session
    // TODO make GameObject positionable
    private final static IdGen idGen = new IdGen(false);
    private final Integer id;
    private final ObjectType type;

    private Position position;
    private boolean deleted = false;

    public GameObject(ObjectType type, Position position) {
        id = idGen.generateId();
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

    protected void delete() {
        idGen.addDeletedId(getId());
        deleted = true;
    }

    @JsonIgnore
    final public boolean isDeleted() {
        return deleted;
    }

    @JsonIgnore
    @Override
    public boolean isDestroyed() {
        return isDeleted();
    }

    @Override
    public boolean destroy() {
        if(isDestroyable() && !isDestroyed()) {
            delete();
            return true;
        }
        return false;
    }
}
