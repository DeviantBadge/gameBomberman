package ru.atom.chat.socket.objects.ingame;

import ru.atom.chat.socket.objects.base.GameObject;
import ru.atom.chat.socket.objects.base.util.IdGen;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.properties.GameObjectProperties;

public class ObjectCreator {
    private final IdGen idGen;
    private final GameObjectProperties properties;

    public ObjectCreator(GameObjectProperties properties) {
        this.properties = properties;
        idGen = new IdGen(false);
    }

    public Bomb createBomb(Position position, Pawn owner) {
        return new Bomb(idGen.generateId(), position, owner, properties.getBombBlowTimeMs());
    }

    public Bonus createBonus(Position position, boolean considerProbability) {
        double bonusCast = Math.random();
        double bonusTypeCast = Math.random();
        if (!considerProbability || bonusCast < properties.getBonusProbability()) {
            if (bonusTypeCast < properties.getSpeedProbability())
                return new Bonus(idGen.generateId(), position, Bonus.BonusType.SPEED);
            else
                bonusTypeCast -= properties.getSpeedProbability();
            if (bonusTypeCast < properties.getBombsProbability())
                return new Bonus(idGen.generateId(), position, Bonus.BonusType.BOMBS);
            return new Bonus(idGen.generateId(), position, Bonus.BonusType.RANGE);
        }
        return null;
    }

    public Bonus createBonus(Position position, Bonus.BonusType type) {
        return new Bonus(idGen.generateId(), position, type);
    }

    public Fire createFire(Position position) {
        return new Fire(idGen.generateId(), position);
    }

    public Pawn createPawn(Position position) {
        return new Pawn(idGen.generateId(), position,
                properties.getRangeOnStart(),
                properties.getBombsOnStart(),
                properties.getSpeedOnStart());
    }

    public Wall createWall(Position position) {
        return new Wall(idGen.generateId(), position);
    }

    public Wood createWood(Position position) {
        return new Wood(idGen.generateId(), position);
    }


    public boolean destroy(GameObject object) {
        object.destroy();
        if (object.isDeleted())
            idGen.addDeletedId(object.getId());
        return object.isDestroyed();
    }
}
