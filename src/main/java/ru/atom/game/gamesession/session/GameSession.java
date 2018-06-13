package ru.atom.game.gamesession.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketSession;
import ru.atom.game.enums.Direction;
import ru.atom.game.enums.MessageType;
import ru.atom.game.socket.message.request.IncomingMessage;
import ru.atom.game.socket.message.request.messagedata.Name;
import ru.atom.game.socket.message.response.OutgoingMessage;
import ru.atom.game.socket.message.response.messagedata.Possess;
import ru.atom.game.socket.message.response.messagedata.Replica;
import ru.atom.game.objects.ingame.ObjectCreator;
import ru.atom.game.objects.base.Cell;
import ru.atom.game.objects.base.GameObject;
import ru.atom.game.gamesession.state.GameState;
import ru.atom.game.objects.ingame.Bomb;
import ru.atom.game.objects.ingame.Bonus;
import ru.atom.game.objects.ingame.Pawn;
import ru.atom.game.objects.orders.Order;
import ru.atom.game.enums.IncomingTopic;
import ru.atom.game.gamesession.properties.GameSessionProperties;
import ru.atom.game.repos.GameSessionRepo;
import ru.atom.game.socket.util.JsonHelper;
import ru.atom.game.socket.util.SessionsList;

import java.util.ArrayList;
import java.util.List;


// TODO mb i can add some bots :)
public class GameSession extends OnlineSession {
    private static final Logger log = LoggerFactory.getLogger(GameSession.class);

    @Autowired
    private GameSessionRepo sessionRepo;

    private GameState gameState;
    private List<Cell> changedCells;

    // i wanna create game settings, for this i have to create some properties
    private GameSessionProperties properties;
    private ObjectCreator creator;

    private MovingProcessor movingProcessor;
    // private BombProcessor bombProcessor;

    // add objects to replica where we change them
    private Replica replica;

    //TODO mb add something like game type - deathMatch singleLife - or it could be made by properties but how ?

    // TODO i dont know how, but part of this code we have to move to another class

    // TODO when player disconnects, we have to remember his state and if he reconnects, we check if his sessions are active or not

    public GameSession(GameSessionProperties properties) {
        super(properties.getMaxPlayerAmount());
        this.properties = properties;
        creator = new ObjectCreator(properties);
        gameState = new GameState(properties, creator);
        replica = new Replica();
        changedCells = new ArrayList<>();
        movingProcessor = new MovingProcessor(properties, gameState);
    }

    //TODO here we can create order builder - some factory !!
    protected Order buildOrder(IncomingMessage message, WebSocketSession session) {
        if (message.getTopic() == IncomingTopic.CONNECT) {
            String name = JsonHelper.fromJson(message.getData(), Name.class).getName();
            int playerNum = playerNum(name);
            if (playerNum == -1) {
                log.warn("Connecting to lobby where we does not logged in");
                return null;
            }
            return new Order(playerNum, name, session);
        }
        int playerNum = playerNum(session);
        if (playerNum < 0) {
            log.warn("Player isn`t in this group. Group id - " + getId());
            return null;
        }
        return new Order(playerNum, message);
    }

    @Override
    protected void act(long ms) {
        super.act(ms);
        addAliveObjectsToReplica();
        sendReplica();
    }

    @Override
    protected void performTick(long ms) {
        changedCells.addAll(movingProcessor.movePlayers(ms)) ;
        tickBomb(ms);
        clearCells();
    }

    @Override
    protected void carryOut(Order order) {
        switch (order.getIncomingTopic()) {
            case CONNECT:
                // sendTo(order.getPlayerNum(), getPossess());
                if (playersAmount() < properties.getMaxPlayerAmount()) {
                    connectPlayerWithSocket(order.getPlayerNum(), order.getSession());
                    replica.addAllToReplica(gameState.getFieldReplica());
                    sendReplicaTo(order.getPlayerNum());
                } else {
                    replica.addAllToReplica(gameState.getFieldReplica());
                    sendReplica();
                    gameState.recreate();
                    replica.addAllToReplica(gameState.getFieldReplica());
                    connectPlayerWithSocket(order.getPlayerNum(), order.getSession());
                }

                // TODO make connect from socketHandler
                SessionsList.matchSessionWithGame(order.getSession(), this);
                break;

            case JUMP:
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
                plantBombBy(gameState.getPawns().get(order.getPlayerNum()));
                break;
        }
    }

    private void plantBombBy(Pawn playerPawn) {
        Cell standingOn = gameState.get(playerPawn.getCenter());
        if (playerPawn.getBombCount() < playerPawn.getMaxBombAmount()) {
            boolean canPlant = true;
            List<GameObject> objects = standingOn.getObjects();
            for (GameObject object : objects) {
                switch (object.getType()) {
                    case Pawn:
                    case Fire:
                        break;
                    default:
                        canPlant = false;
                        break;
                }
            }
            if (canPlant) {
                gameState.addBomb(creator.createBomb(standingOn.getPosition(), playerPawn));
                playerPawn.incBombCount();
            }
        }
    }

    private void clearCells() {
        Cell cell;

        for (int i = 0; i < changedCells.size(); i++) {
            cell = changedCells.get(i);
            for (int j = 0; j < cell.getObjects().size(); j++) {
                onDestroy(cell.getObjects().get(j));
            }
            cell.deleteDestroyed();
        }
        changedCells.clear();
    }

    private void onDestroy(GameObject object) {
        if (!object.isDestroyed())
            return;
        switch (object.getType()) {
            case Bonus:
                if (((Bonus) object).isPickedUp()) {
                    addObjectToReplica(object);
                    return;
                }
            case Wood:
                Cell cell = gameState.get(object.getPosition());
                addObjectToReplica(object);
                Bonus bonus = creator.createBonus(cell.getPosition(), true);
                if (bonus != null) {
                    cell.addObject(bonus);
                    addObjectToReplica(bonus);
                }
                break;

            case Pawn:
                if (!gameState.isWarmUp()) {
                    onPlayerDeath((Pawn) object);
                    if (areAllDead()) {
                        stop();
                    }
                } else {
                    ((Pawn) object).riseAgain();
                }
                break;
        }
    }

    private boolean areAllDead() {
        return gameState.deadPawnsAmount() >= properties.getMaxPlayerAmount() - 1;
    }

    private void onPlayerDeath(Pawn object) {
        String message = JsonHelper.toJson(new OutgoingMessage(MessageType.GAME_OVER, "YOU DIED"));
        int playerNum = gameState.playerNum(object);
        sendTo(playerNum, message);
        SessionsList.unfastenSessionWithGame(getPlayersSocket(playerNum));
    }

    private void tickBomb(long ms) {
        List<Bomb> bombs = gameState.getBombs();
        for (Bomb bomb : bombs) {
            if (bomb.isReady())
                continue;
            bomb.tick(ms);
            if (bomb.isReady()) {
                blowBomb(bomb);
            }
        }
        bombs.removeIf(Bomb::isDestroyed);
    }

    private void blowBomb(Bomb bomb) {
        creator.destroy(bomb);

        int x = bomb.getIntX() / 32;
        int y = bomb.getIntY() / 32;
        blow(gameState.get(x, y));
        int blowRange = bomb.getOwner().getBlowRange();

        for (int j = 1; j <= blowRange && (y + j) < gameState.getSizeY(); j++) {
            if (!blow(gameState.get(x, y + j)))
                break;
        }
        for (int j = 1; j <= blowRange && (x + j) < gameState.getSizeX(); j++) {
            if (!blow(gameState.get(x + j, y)))
                break;
        }
        for (int j = 1; j <= blowRange && (y - j) >= 0; j++) {
            if (!blow(gameState.get(x, y - j)))
                break;
        }
        for (int j = 1; j <= blowRange && (x - j) >= 0; j++) {
            if (!blow(gameState.get(x - j, y)))
                break;
        }
        bomb.getOwner().decBombCount();
    }

    private boolean blow(Cell cell) {
        boolean destroyed = true;
        boolean stopDestroying = false;
        boolean changed = false;

        for (GameObject object : cell.getObjects()) {
            switch (object.getType()) {
                case Bomb:
                    changed = true;
                    if (object.isDestroyed())
                        continue;
                    Bomb bomb = (Bomb) object;
                    bomb.stop();
                    blowBomb(bomb);
                    break;

                case Bonus:
                case Wood:
                    stopDestroying = properties.isBlowStopsOnWall();
                    changed = true;
                    break;

                case Pawn:
                    if (gameState.isWarmUp())
                        continue;
                    changed = true;
                    break;
            }
            // todo here we get parameter from property, like we wanna stop 2 bombs blowing near, or not
            destroyed = destroyed && creator.destroy(object);
        }
        if (changed)
            addChangedCell(cell);

        if (destroyed)
            addObjectToReplica(creator.createFire(cell.getPosition()));
        return destroyed && !stopDestroying;
    }

    private void addChangedCell(Cell cell) {
        if (!changedCells.contains(cell))
            changedCells.add(cell);
    }

    private void addAliveObjectsToReplica() {
        replica.addAllToReplica(gameState.getBombs());
        replica.addAllToReplica(gameState.getPawns());
    }

    private void addObjectToReplica(GameObject object) {
        replica.addToReplica(object);
    }

    private void sendReplica() {
        String message = JsonHelper.toJson(new OutgoingMessage(MessageType.REPLICA, replica.toString()));
        sendAll(message);
    }

    private void sendReplicaTo(int playerNum) {
        String message = JsonHelper.toJson(new OutgoingMessage(MessageType.REPLICA, replica.toString()));
        sendTo(playerNum, message);
    }

    private String getPossess() {
        Possess possess = new Possess(gameState.getSizeY(), gameState.getSizeX(), 0);
        return JsonHelper.toJson(new OutgoingMessage(MessageType.POSSESS, JsonHelper.toJson(possess)));
    }

    @Override
    protected void onStop() {
        super.onStop();
        int lastPlayer = gameState.getAliveNum();
        if (lastPlayer != -1) {
            String message = JsonHelper.toJson(new OutgoingMessage(MessageType.GAME_OVER, "YOU WON"));
            sendTo(lastPlayer, message);
            // TODO thats what i dislike, it doesnt belong to game process, we have to do it smwhere else
            // It will be solved when i will create rules to classes and their rights
            SessionsList.unfastenSessionWithGame(getPlayersSocket(lastPlayer));
            sessionRepo.endGame(this);
        }
    }

    @Override
    public void addPlayer(String name) {
        super.addPlayer(name);
        gameState.addPlayer();
    }

    // TODO надо четко определить фукционирование и области действия классов, один удаляет сокеты, другой не имеет права с ними работать
    // TODO третий наоборот все делает с сокетами а первым двум только позволяет произвести какие то действия (реакция на событие, не больше)
    @Override
    public void onPlayerDisconnect(WebSocketSession session) {
        int playerNum = playerNum(session);
        if (gameState.isWarmUp()) {
            gameState.getPawns().remove(playerNum);
            removePlayer(playerNum);
            // TODO if we will store sessions, what player were in, we have to do smth here, i forgot what i wanted to do
        } else {
            gameState.getPawns().get(playerNum).die();
            super.onPlayerDisconnect(session);
            if (areAllDead()) {
                stop();
            }
        }
    }

    @Override
    protected void stop() {
        super.stop();
    }
}
