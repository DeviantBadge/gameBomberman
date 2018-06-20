package ru.atom.game.gamesession.session;

import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;
import ru.atom.game.enums.IncomingTopic;
import ru.atom.game.gamesession.lists.OnlinePlayer;
import ru.atom.game.gamesession.lists.OrderList;
import ru.atom.game.gamesession.lists.PlayersList;
import ru.atom.game.socket.message.request.IncomingMessage;
import ru.atom.game.objects.base.util.IdGen;
import ru.atom.game.gamesession.lists.Order;
import ru.atom.game.socket.message.request.messagedata.Name;
import ru.atom.game.socket.util.JsonHelper;

public abstract class OnlineSession extends Ticker {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(OnlineSession.class);
    private static final IdGen idGen = new IdGen();
    private final Integer id;

    // just now if we want new features we have to add it manually
    private PlayersList playersList;
    private OrderList orderList;
    private int max;

    public OnlineSession(int maxPlayerAmount) {
        super(60);
        id = idGen.generateId();

        playersList = new PlayersList(maxPlayerAmount);
        orderList = new OrderList();
        max = maxPlayerAmount;
    }

    // **********************
    // SESSION functions
    // **********************

    public boolean onPlayerConnect(OnlinePlayer player) {
        int playerNum = playerNum(player);
        if (playerNum == -1) {
            log.warn("Connecting to lobby where we does not logged in");
            return false;
        }

        // todo think about how to create order and realise that !! (now its better, then it was, but how we will handle it in chat?)
        putOrder(new Order(playerNum, IncomingTopic.CONNECT));
        return true;
    }

    public void addOrder(OnlinePlayer player, IncomingMessage message) {
        Order order = buildOrder(player, message);
        if (order != null)
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

    protected Order buildOrder(OnlinePlayer player, IncomingMessage message) {
        int playerNum = playerNum(player);
        if (playerNum < 0) {
            log.warn("Player isn`t in this group. Group id - " + getId());
            return null;
        }
        return new Order(playerNum, message);
    }

    // **********************
    // players list functions
    // **********************

    protected int playerNum(OnlinePlayer player) {
        return playersList.playerNum(player);
    }

    protected OnlinePlayer getPlayer(int num) {
        return playersList.getPlayer(num);
    }

    protected void sendAll(String data) {
        playersList.sendAll(data);
    }

    protected void sendTo(int playerNum, String data) {
        if (playerNum < 0 || playerNum >= playersList.size())
            return;
        playersList.sendTo(playerNum, data);
    }

    protected int playersAmount() {
        return playersList.size();
    }

    public void addPlayer(OnlinePlayer player) {
        playersList.addPlayer(player);
    }

    public boolean isFull() {
        return playersAmount() == max;
    }

    public abstract void onPlayerDisconnect(OnlinePlayer player);

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
