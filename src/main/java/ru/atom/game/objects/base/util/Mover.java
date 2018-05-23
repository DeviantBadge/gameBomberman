package ru.atom.game.objects.base.util;

import ru.atom.game.enums.Direction;
import ru.atom.game.objects.ingame.Pawn;
import ru.atom.game.gamesession.properties.GameSessionProperties;

public class Mover {
    private GameSessionProperties properties;

    public Mover(GameSessionProperties properties) {
        this.properties = properties;
    }

    private Position move(Position target, double speedX, double speedY, Direction direction, long ms) {
        switch (direction) {
            case UP:
                return new Position(target.getX(), target.getY() + speedY * ms / 1000.0);

            case RIGHT:
                return new Position(target.getX() + speedX * ms / 1000.0, target.getY());

            case DOWN:
                return new Position(target.getX(), target.getY() - speedY * ms / 1000.0);

            case LEFT:
                return new Position(target.getX() - speedX * ms / 1000.0, target.getY());

            default:
                return target;
        }
    }

    public Position move(Pawn player, long ms) {
        double speedX = properties.getMovingSpeedX()
                * (1 + properties.getSpeedBonusCoef() * player.getSpeedBonus());
        double speedY = properties.getMovingSpeedY()
                * (1 + properties.getSpeedBonusCoef() * player.getSpeedBonus());

        return move(player.getPosition(), speedX, speedY, player.getDirection(), ms);
    }
}
