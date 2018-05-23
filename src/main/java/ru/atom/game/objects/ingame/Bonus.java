package ru.atom.game.objects.ingame;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.atom.game.objects.base.GameObject;
import ru.atom.game.objects.base.util.Position;
import ru.atom.game.enums.ObjectType;

public class Bonus extends GameObject {
    public static enum  BonusType {
        SPEED,
        BOMBS,
        RANGE
    }

    private final BonusType bonusType;

    Bonus(Integer id, Position position, BonusType bonusType) {
        super(id, ObjectType.Bonus, position);
        this.bonusType = bonusType;
    }

    public BonusType getBonusType() {
        return bonusType;
    }

    @Override
    @JsonIgnore
    protected String getEntrails() {
        return super.getEntrails() + ",bonusType" + bonusType;
    }

    @Override
    @JsonIgnore
    public boolean isDestroyable() {
        return true;
    }
}
