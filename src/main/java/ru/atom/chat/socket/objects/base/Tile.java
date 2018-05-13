package ru.atom.chat.socket.objects.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.enums.ObjectType;

public abstract class Tile extends GameObject {
    private final boolean destroyable;

    public Tile(ObjectType type, Position position, boolean destroyable) {
        super(type, position);
        this.destroyable = destroyable;
    }

    @JsonIgnore
    @Override
    public boolean isDestroyable() {
        return destroyable;
    }

    @JsonIgnore
    @Override
    protected String getEntrails() {
        return super.getEntrails() + ",destroyable:" + destroyable;
    }
}
