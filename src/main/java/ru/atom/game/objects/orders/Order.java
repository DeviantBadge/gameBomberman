package ru.atom.game.objects.orders;

import org.springframework.web.socket.WebSocketSession;
import ru.atom.game.socket.message.request.IncomingMessage;
import ru.atom.game.socket.message.request.messagedata.InGameMovement;
import ru.atom.game.enums.IncomingTopic;
import ru.atom.game.enums.MovementType;
import ru.atom.game.socket.message.request.messagedata.Name;
import ru.atom.game.socket.util.JsonHelper;

public class Order {
    private int playerNum;
    private IncomingTopic incomingTopic;
    private MovementType movementType = null;
    private String name = null;
    private WebSocketSession session = null;

    public Order(int playerNum, IncomingMessage message) {
        this.playerNum = playerNum;
        this.incomingTopic = message.getTopic();
        if (incomingTopic == IncomingTopic.MOVE) {
            movementType = JsonHelper.fromJson(message.getData(), InGameMovement.class).getDirection();
        }
        if (incomingTopic == IncomingTopic.CONNECT) {
            name = JsonHelper.fromJson(message.getData(), Name.class).getName();
        }
    }

    public Order(int playerNum, String name, WebSocketSession session) {
        this.playerNum = playerNum;
        this.name = name;
        this.session = session;
        incomingTopic = IncomingTopic.CONNECT;
    }

    public String getName() {
        return name;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    public WebSocketSession getSession() {
        return session;
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
