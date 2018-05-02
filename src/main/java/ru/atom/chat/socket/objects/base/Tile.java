package ru.atom.chat.socket.objects.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Tile extends GameObject {
    private final boolean destroyable;

    public Tile(String type,Position position, boolean destroyable) {
        super(type, position);
        this.destroyable = destroyable;
    }

    @JsonIgnore
    public boolean isDestroyable() {
        return destroyable;
    }

    @JsonIgnore
    @Override
    protected String getEntrails() {
        return super.getEntrails() + ",destroyable:" + destroyable;
    }
}
