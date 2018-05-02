package ru.atom.chat.socket.objects.ingame;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;
import ru.atom.chat.socket.objects.base.GameObject;
import ru.atom.chat.socket.objects.base.Position;

public class Bonus extends GameObject {
    public static enum  BonusType {
        SPEED,
        BOMBS,
        RANGE
    }

    private final BonusType bonusType;

    public Bonus(@NotNull Position position, @NotNull BonusType bonusType) {
        super("Bonus", position);
        this.bonusType = bonusType;
    }

    public Bonus(@NotNull Integer x, @NotNull Integer y, @NotNull BonusType bonusType) {
        super("Bonus", new Position(x, y));
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
}
