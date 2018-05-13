package ru.atom.chat.socket.objects.base.util;

import org.springframework.stereotype.Service;
import ru.atom.chat.socket.enums.Direction;

@Service
public class Mover {
    private final static int SPEED_PER_SECOND_X = 60;
    private final static int SPEED_PER_SECOND_Y = 60;
    private final int speedX;
    private final int speedY;

    public Mover() {
        speedX = SPEED_PER_SECOND_X;
        speedY = SPEED_PER_SECOND_Y;
    }

    public Position move(Position target, Direction direction, long ms) {
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
}
