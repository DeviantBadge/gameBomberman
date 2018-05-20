package ru.atom.chat.socket.objects.gamesession;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.atom.chat.socket.enums.MessageType;
import ru.atom.chat.socket.message.response.OutgoingMessage;
import ru.atom.chat.socket.objects.base.util.IdGen;
import ru.atom.chat.socket.objects.orders.Order;
import ru.atom.chat.socket.util.JsonHelper;

import java.io.IOException;

public abstract class OnlineSession extends Ticker {
    private static final IdGen idGen = new IdGen();
    private final Integer id;

    // just now if we want new features we have to add it manually
    // TODO refactor playersList
    private PlayersList playersList;
    private OrderList orderList;

    public OnlineSession(int maxPlayerAmount){
        id = idGen.generateId();

        playersList = new PlayersList(maxPlayerAmount);
        orderList = new OrderList();
    }

    public Integer getId() {
        return id;
    }

    private void clearId() {
        idGen.addDeletedId(getId());
    }

    // **********************
    // players list functions
    // **********************

    protected int playerNum(WebSocketSession session) {
        return playersList.playerNum(session);
    }

    protected int playerNum(String name) {
        return playersList.playerNum(name);
    }

    protected String getPlayerName(int num) {
        return playersList.getName(num);
    }

    protected void createNewPlayer(String name) {
        playersList.createNewPlayer(name);
    }

    protected void connectPlayerWithSocket(int playerNum, WebSocketSession session) {
        playersList.connectPlayerWithSocket(playerNum, session);
    }

    protected void sendAll(String data) {
        playersList.sendAll(data);
    }

    protected void sendTo(int playerNum, String data) {
        if(playerNum < 0 || playerNum >= playersList.size())
            return;
        playersList.sendTo(playerNum, data);
    }

    protected int playersAmount() {
        return playersList.size();
    }


    // **********************
    // orders list functions
    // **********************

    protected Order pollOrder() {
        return orderList.getOrder();
    }

    protected void putOrder(Order order) {
        orderList.add(order);
    }

    protected int ordersAmount() {
        return orderList.size();
    }

    @Override
    protected void onStop() {
        clearId();
    }

    @Override
    protected void act(long timeFromPastLoop) {

    }
}
