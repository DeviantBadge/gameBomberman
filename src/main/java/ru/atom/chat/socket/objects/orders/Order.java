package ru.atom.chat.socket.objects.orders;

import ru.atom.chat.socket.message.request.IncomingMessage;
import ru.atom.chat.socket.message.request.messagedata.InGameMovement;
import ru.atom.chat.socket.enums.IncomingTopic;
import ru.atom.chat.socket.enums.MovementType;
import ru.atom.chat.socket.util.JsonHelper;

public class Order {
    private int playerNum;
    private IncomingTopic incomingTopic;
    private MovementType movementType;

    public Order(int playerNum, IncomingMessage message) {
        this.playerNum = playerNum;
        this.incomingTopic = message.getTopic();
        if(incomingTopic == IncomingTopic.MOVE) {
            movementType = JsonHelper.fromJson(message.getData(), InGameMovement.class).getDirection();
        }
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public IncomingTopic getIncomingTopic() {
        return incomingTopic;
    }

    public MovementType getMovementType() {
        return movementType;
    }
}
