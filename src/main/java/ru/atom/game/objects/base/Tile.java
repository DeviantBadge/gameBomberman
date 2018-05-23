package ru.atom.game.objects.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.atom.game.objects.base.util.Position;
import ru.atom.game.enums.ObjectType;

public abstract class Tile extends GameObject {
    private final boolean destroyable;

    public Tile(Integer id, ObjectType type, Position position, boolean destroyable) {
        super(id, type, position);
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
