package ru.atom.game.gamesession.session;

import org.springframework.web.socket.WebSocketSession;
import ru.atom.game.gamesession.lists.OrderList;
import ru.atom.game.gamesession.lists.PlayersList;
import ru.atom.game.socket.message.request.IncomingMessage;
import ru.atom.game.objects.base.util.IdGen;
import ru.atom.game.objects.orders.Order;

public abstract class OnlineSession extends Ticker {
    private static final IdGen idGen = new IdGen();
    private final Integer id;

    // just now if we want new features we have to add it manually
    private PlayersList playersList;
    private OrderList orderList;
    private int max;

    public OnlineSession(int maxPlayerAmount){
        id = idGen.generateId();

        playersList = new PlayersList(maxPlayerAmount);
        orderList = new OrderList();
        max = maxPlayerAmount;
    }

    // **********************
    // SESSION functions
    // **********************

    public void addOrder(IncomingMessage message, WebSocketSession session) {
        Order order = buildOrder(message, session);
        if(order != null)
            putOrder(order);
    }

    @Override
    protected void act(long ms) {
        performOrders();
        performTick(ms);
    }

    private void performOrders() {
        int size = ordersAmount();
        for (int i = 0; i < size; i++) {
            Order order = pollOrder();
            if (order == null)
                return;
            carryOut(order);
        }
    }

    protected abstract void carryOut(Order order);

    protected abstract void performTick(long ms);

    protected abstract Order buildOrder(IncomingMessage message, WebSocketSession session);

    // **********************
    // players list functions
    // **********************

    protected int playerNum(WebSocketSession session) {
        return playersList.playerNum(session);
    }

    protected int playerNum(String name) {
        return playersList.playerNum(name);
    }

    protected WebSocketSession getPlayersSocket(int playerNum) {
        return playersList.getSession(playerNum);
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

    public void addPlayer(String name) {
        // TODO adding player here, then checking it while player connecting to room
        createNewPlayer(name);
    }

    // TODO realise it better, i made it fast and inaccurately, thats because we dont match socket with GameSessions what this socket uses
    public void onPlayerDisconnect(WebSocketSession session) {
        int playerNum = playerNum(session);
        if(playerNum == -1)
            return;
        playersList.close(playerNum(session));
    }

    public boolean isFull() {
        return playersAmount() == max;
    }

    protected void closeSession(int playerNum) {
        playersList.close(playerNum);
    }

    protected void removePlayer(int playerNum) {
        playersList.removePlayer(playerNum);
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

    public Integer getId() {
        return id;
    }

    private void clearId() {
        idGen.addDeletedId(getId());
    }
}
