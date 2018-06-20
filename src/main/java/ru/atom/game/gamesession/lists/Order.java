package ru.atom.game.gamesession.lists;

import org.springframework.web.socket.WebSocketSession;
import ru.atom.game.enums.Direction;
import ru.atom.game.socket.message.request.IncomingMessage;
import ru.atom.game.socket.message.request.messagedata.InGameMovement;
import ru.atom.game.enums.IncomingTopic;
import ru.atom.game.socket.message.request.messagedata.Name;
import ru.atom.game.socket.util.JsonHelper;

// приказ
// Если буду реализовывать чат, то надо будет работу ордера пересмотреть, плюс то что он хранит столько всего мне не в кайф
public class Order {
    private int playerNum;
    private IncomingTopic incomingTopic;
    private Direction movementType = null;

    public Order(int playerNum, IncomingMessage message) {
        this.playerNum = playerNum;
        this.incomingTopic = message.getTopic();
        switch (incomingTopic) {
            case MOVE:
                movementType = JsonHelper.fromJson(message.getData(), InGameMovement.class).getDirection();
                break;
            case CONNECT:
            case READY:
            case PLANT_BOMB:
            case JUMP:
                default:
                    break;

        }
    }

    public Order(int playerNum, IncomingTopic topic) {
        this.playerNum = playerNum;
        incomingTopic = topic;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public IncomingTopic getIncomingTopic() {
        return incomingTopic;
    }

    public Direction getMovementType() {
        return movementType;
    }
}
