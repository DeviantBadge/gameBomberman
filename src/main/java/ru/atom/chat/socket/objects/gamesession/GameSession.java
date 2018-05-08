package ru.atom.chat.socket.objects.gamesession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;
import ru.atom.chat.socket.message.request.IncomingMessage;
import ru.atom.chat.socket.message.response.messagedata.Replica;
import ru.atom.chat.socket.objects.base.util.IdGen;
import ru.atom.chat.socket.objects.ingame.Bomb;
import ru.atom.chat.socket.objects.ingame.Pawn;
import ru.atom.chat.socket.objects.orders.Order;
import ru.atom.chat.socket.enums.IncomingTopic;
import ru.atom.chat.socket.util.JsonHelper;

public class GameSession {

    private static final Logger log = LoggerFactory.getLogger(GameSession.class);
    private static final IdGen idGen = new IdGen();
    private static final int MAX_PLAYER_AMOUNT = 2;

    private GameState gameState ;
    // just now if we want new features we have to add it manually
    private PlayersList playersList;
    private OrderList orderList;

    private final Integer id;
    private Replica replica;

    public GameSession() {
        gameState = new GameState();
        replica = new Replica();

        playersList = new PlayersList(MAX_PLAYER_AMOUNT);
        orderList = new OrderList();
        id = idGen.generateId();
    }

    public void addOrder(String message, WebSocketSession session) {
        IncomingMessage mes = JsonHelper.fromJson(message, IncomingMessage.class);
        if(mes.getTopic() == IncomingTopic.CONNECT) {
            // TODO add CONNECT handler to performOrders()
            PlayersList.PlayerPawn playerPawn =  playersList.createNewPlayer(session);
            if(playerPawn == null)
                return;
            replica.addAllToReplica(gameState.createGameField());
            gameState.put(0,0, playerPawn.getPawn());
            return;
        }
        int playerNum = playersList.playerNum(session);
        if(playerNum < 0) {
            log.warn("Player isn`t in this group. Group id - " + getId());
            return;
        }
        orderList.add(new Order(playerNum, mes));
    }

    public Integer getId() {
        return id;
    }

    public void performOrders() {
        if(!gameState.isReady())
            return;
        int size = orderList.size();
        for(int i = 0; i < size; i ++) {
            Order order = orderList.getOrder();
            if(order == null)
                return;
            switch (order.getIncomingTopic()) {
                case JUMP:
                    break;
                case CONNECT:
                    break;
                case MOVE:
                    switch (order.getMovementType()) {
                        case UP:
                            playersList.getPlayerPawn(order.getPlayerNum()).setDirection(Pawn.Direction.UP);
                            break;
                        case RIGHT:
                            playersList.getPlayerPawn(order.getPlayerNum()).setDirection(Pawn.Direction.RIGHT);
                            break;
                        case DOWN:
                            playersList.getPlayerPawn(order.getPlayerNum()).setDirection(Pawn.Direction.DOWN);
                            break;
                        case LEFT:
                            playersList.getPlayerPawn(order.getPlayerNum()).setDirection(Pawn.Direction.LEFT);
                            break;
                    }
                    break;
                case PLANT_BOMB:
                    plantBombBy(playersList.getPlayerPawn(order.getPlayerNum()));
                    break;
            }
        }
        performTick();
        buildReplica();
        playersList.sendAll(replica.getData());
    }

    private void plantBombBy(Pawn playerPawn) {
        //TODO some checks (such as cell is clear)
        gameState.getBombs().add(new Bomb(playerPawn.getPosition()));
    }

    private void performTick() {
        // TODO ticking bombs
        // TODO destoy objects also here
        // TODO players start moving here
        // TODO while changing states we also add changed objects to replica
    }

    private void buildReplica() {
        replica.addAllToReplica(gameState.getBombs());
        replica.addAllToReplica(playersList.getPawns());
    }

    public GameState getGameState() {
        return gameState;
    }
}
