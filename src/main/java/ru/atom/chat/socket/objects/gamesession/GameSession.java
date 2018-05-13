package ru.atom.chat.socket.objects.gamesession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;
import ru.atom.chat.socket.enums.Direction;
import ru.atom.chat.socket.message.request.IncomingMessage;
import ru.atom.chat.socket.message.response.messagedata.Replica;
import ru.atom.chat.socket.objects.base.Cell;
import ru.atom.chat.socket.objects.base.GameObject;
import ru.atom.chat.socket.objects.base.util.IdGen;
import ru.atom.chat.socket.objects.base.util.Mover;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.objects.ingame.Bomb;
import ru.atom.chat.socket.objects.ingame.Fire;
import ru.atom.chat.socket.objects.ingame.Pawn;
import ru.atom.chat.socket.objects.orders.Order;
import ru.atom.chat.socket.enums.IncomingTopic;
import ru.atom.chat.socket.util.JsonHelper;

import java.util.List;

public class GameSession extends Ticker {

    private static final Logger log = LoggerFactory.getLogger(GameSession.class);
    private static final IdGen idGen = new IdGen();
    private static final int MAX_PLAYER_AMOUNT = 2;

    // @Autowired
    private Mover mover = new Mover();

    private GameState gameState;

    // just now if we want new features we have to add it manually
    private PlayersList playersList;
    private OrderList orderList;

    private final Integer id;

    // add objects to replica where we change them
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
        if (mes.getTopic() == IncomingTopic.CONNECT) {
            // TODO add CONNECT handler to performOrders()
            PlayersList.PlayerPawn playerPawn = playersList.createNewPlayer(session);
            if (playerPawn == null)
                return;
            replica.addAllToReplica(gameState.createGameField());
            /* TODO Game state must generate starting player position, then we give it to players
               TODO Smth like positions array and we add player only by add(playerPawn); */
            addPlayer(playerPawn);
            return;
        }
        int playerNum = playersList.playerNum(session);
        if (playerNum < 0) {
            log.warn("Player isn`t in this group. Group id - " + getId());
            return;
        }
        orderList.add(new Order(playerNum, mes));
    }

    @Override
    protected void act(long ms) {
        performOrders();
        // TODO do it in cycle (that loops every time)
        performTick(ms);
        addAliveObjectsToReplica();
        playersList.sendAll(replica.getData());
    }

    public void performOrders() {
        if (!gameState.isReady())
            return;
        int size = orderList.size();
        for (int i = 0; i < size; i++) {
            Order order = orderList.getOrder();
            if (order == null)
                return;
            switch (order.getIncomingTopic()) {
                case JUMP:
                    break;
                case CONNECT:
                    break;
                case MOVE:
                    Pawn curPlayer = gameState.getPawns().get(order.getPlayerNum());
                    if (curPlayer.isMoving())
                        break;
                    switch (order.getMovementType()) {
                        case UP:
                            curPlayer.setDirection(Direction.UP);
                            break;
                        case RIGHT:
                            curPlayer.setDirection(Direction.RIGHT);
                            break;
                        case DOWN:
                            curPlayer.setDirection(Direction.DOWN);
                            break;
                        case LEFT:
                            curPlayer.setDirection(Direction.LEFT);
                            break;
                    }
                    curPlayer.setMoving(true);
                    break;
                case PLANT_BOMB:
                    plantBombBy(playersList.getPlayerPawn(order.getPlayerNum()));
                    break;
            }
        }
    }

    private void plantBombBy(Pawn playerPawn) {
        //TODO some checks (such as "is cell clear")
        gameState.getBombs().add(new Bomb(playerPawn.getPosition(), playerPawn));
    }

    private void performTick(long ms) {
        // TODO players start moving here
        movePlayers(ms);
        tickBomb(ms);
        // TODO ticking bombs (if bomb destroy smth we add it to replica)
        // TODO destroy objects also here (as bombs and so on)
        // TODO while changing states we also add changed objects to replica
    }

    private void tickBomb(long ms) {
        for (int i = 0; i < gameState.getBombs().size(); i ++) {
            Bomb bomb = gameState.getBombs().get(i);
            if (bomb.isReady())
                continue;
            bomb.tick(ms);
            if (bomb.isReady()) {
                blowBomb(bomb);
                i --;
            }
        }
    }

    private void blowBomb(Bomb bomb) {
        gameState.getBombs().remove(bomb);
        gameState.get(bomb.getPosition()).removeObject(bomb);

        int x = (int) (bomb.getPosition().getX() / 32);
        int y = (int) (bomb.getPosition().getY() / 32);
        destroy(gameState.get(bomb.getPosition()));
        int blowRange = bomb.getOwner().getBlowRange();
        for (int j = 1; j <= blowRange && (y + j) < gameState.getSizeY(); j++) {
            if (!destroy(gameState.get(x, y + j)))
                break;
        }
        for (int j = 1; j <= blowRange && (x + j) < gameState.getSizeX(); j++) {
            if (!destroy(gameState.get(x + j, y)))
                break;
        }
        for (int j = 1; j <= blowRange && (y - j) >= 0; j++) {
            if (!destroy(gameState.get(x, y - j)))
                break;
        }
        for (int j = 1; j <= blowRange && (x - j) >= 0; j++) {
            if (!destroy(gameState.get(x - j, y)))
                break;
        }
    }

    private boolean destroy(Cell cell) {
        List<GameObject> cellObjects = cell.getObjects();
        boolean destroyed = true;
        for (int i = 0; i < cellObjects.size(); i++) {
            switch (cellObjects.get(i).getType()) {
                case Bomb:
                    Bomb bomb = (Bomb) cellObjects.get(i);
                    if(bomb.isReady()) {
                        // TODO check can we delete this ?
                        cellObjects.remove(bomb);
                        gameState.getBombs().remove(bomb);
                        break;
                    }
                    bomb.stop();
                    blowBomb(bomb);
                    break;
                case Fire:
                case Wall:
                    destroyed = false;
                    break;
                case Pawn:
                    Pawn player = (Pawn) cellObjects.get(i);
                    player.die();
                    break;
                case Bonus:
                case Wood:
                    addObjectToReplica(cellObjects.get(i));
                    cellObjects.remove(i);
                    i--;
                    break;
            }
        }
        if (destroyed)
            addObjectToReplica(new Fire(cell.getPosition()));
        return destroyed;
    }

    // TODO i dont like it, but now i cant do better, remake it
    private void movePlayers(long ms) {
        for (Pawn pawn : gameState.getPawns()) {
            if (!pawn.isAlive())
                continue;
            if (pawn.isMoving()) {
                Position newPosition = mover.move(pawn.getPosition(), pawn.getDirection(), ms);
                //TODO here we check if our new cell is empty or not
                newPosition = gameState.checkBorders(newPosition);
                Cell prevCell = gameState.get(pawn.getPosition().getCenter());
                Cell newCell = gameState.get(newPosition.getCenter());
                if (newCell.getObjects().isEmpty() || prevCell == newCell) {
                    if (newCell != prevCell) {
                        prevCell.removeObject(pawn);
                        newCell.addObject(pawn);
                    }
                    pawn.setPosition(newPosition);
                } else {
                    System.out.println(prevCell.getPosition() + "      " + newCell.getPosition());
                    // Just now i orient on center
                    double deltaX = newCell.getPosition().getX() - prevCell.getPosition().getX();
                    double deltaY = newCell.getPosition().getY() - prevCell.getPosition().getY();
                    double x = newPosition.getX();
                    double y = newPosition.getY();
                    if (Math.abs(deltaX) > 0.001) {
                        x = prevCell.getPosition().getX() + deltaX / 2.01;
                    }
                    if (Math.abs(deltaY) > 0.001) {
                        y = prevCell.getPosition().getY() + deltaY / 2.01;
                    }
                    pawn.setPosition(new Position(x, y));
                }
                pawn.setMoving(false);
            }
        }
    }

    private void addAliveObjectsToReplica() {
        replica.addAllToReplica(gameState.getBombs());
        replica.addAllToReplica(gameState.getPawns());
    }

    private void addObjectToReplica(GameObject object) {
        replica.addToReplica(object);
    }

    public GameState getGameState() {
        return gameState;
    }

    public Integer getId() {
        return id;
    }

    public void addPlayer(PlayersList.PlayerPawn playerPawn) {
        // TODO adding player here, then checking it while player connecting to room
        gameState.addPlayer(new Position(0, 0), playerPawn.getPawn());
    }
}
