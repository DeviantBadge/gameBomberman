package ru.atom.game.gamesession.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.atom.game.enums.Direction;
import ru.atom.game.enums.MessageType;
import ru.atom.game.gamesession.lists.OnlinePlayer;
import ru.atom.game.repos.ConnectionPool;
import ru.atom.game.socket.message.response.OutgoingMessage;
import ru.atom.game.socket.message.response.messagedata.Possess;
import ru.atom.game.socket.message.response.messagedata.Replica;
import ru.atom.game.objects.ingame.ObjectCreator;
import ru.atom.game.objects.base.Cell;
import ru.atom.game.objects.base.GameObject;
import ru.atom.game.gamesession.state.GameState;
import ru.atom.game.objects.ingame.Bonus;
import ru.atom.game.objects.ingame.Pawn;
import ru.atom.game.gamesession.lists.Order;
import ru.atom.game.gamesession.properties.GameSessionProperties;
import ru.atom.game.repos.GameSessionRepo;
import ru.atom.game.socket.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;


// TODO mb i can add some bots :)
public class GameSession extends OnlineSession {
    private static final Logger log = LoggerFactory.getLogger(GameSession.class);

    @Autowired
    private GameSessionRepo sessionRepo;

    @Autowired
    private ConnectionPool connections;

    private GameState gameState;
    private List<Cell> changedCells;

    // i wanna create game settings, for this i have to create some properties
    private GameSessionProperties properties;
    private ObjectCreator creator;

    private final MovingProcessor movingProcessor;
    private final BombProcessor bombProcessor;

    // add objects to replica where we change them
    private Replica replica;
    // todo - idea, objects have hp

    //TODO mb add something like game type - deathMatch singleLife - or it could be made by properties but how ?

    // TODO i dont know how, but part of this code we have to move to another class

    // TODO when player disconnects, we have to remember his state and if he reconnects, we check if his sessions are active or not - make it by data bases

    // TODO after death, player have to see how he was killed, so, we have to disconnect him from server after small delay

    // TODO add chat

    public GameSession(GameSessionProperties properties) {
        super(properties.getMaxPlayerAmount());
        this.properties = properties;
        creator = new ObjectCreator(properties);
        gameState = new GameState(properties, creator);
        replica = new Replica();
        changedCells = new ArrayList<>();

        movingProcessor = new MovingProcessor(properties, gameState);
        bombProcessor = new BombProcessor(properties, gameState, creator, replica);
    }

    //**************************
    // SESSION LOGIC
    //**************************


    @Override
    protected void act(long ms) {
        super.act(ms);
        addAliveObjectsToReplica();
        sendReplica();
    }

    @Override
    protected void performTick(long ms) {
        // todo how to optimize it
        changedCells.addAll(movingProcessor.movePlayers(ms));
        changedCells.addAll(bombProcessor.tickBomb(ms));
        clearCells();
    }

    @Override
    protected void carryOut(Order order) {
        switch (order.getIncomingTopic()) {
            case CONNECT:
            case READY:
                // todo implement ready statement
                // sendTo(order.getPlayerNum(), getPossess());
                if (playersAmount() < properties.getMaxPlayerAmount()) {
                    sendReplicaTo(order.getPlayerNum(), JsonHelper.toJson(gameState.getFieldReplica()));
                } else {
                    String rep = JsonHelper.toJson(gameState.getFieldReplica());
                    for (int i = 0; i < playersAmount(); i++)
                        if (i != order.getPlayerNum())
                            sendReplicaTo(i, rep);
                    gameState.recreate();
                    replica.addAllToReplica(gameState.getFieldReplica());
                }
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
                bombProcessor.plantBombBy(gameState.getPawns().get(order.getPlayerNum()));
                break;
        }
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
            connections.unlink(getPlayer(lastPlayer), this);
            sessionRepo.onSessionEnd(this);
        }
    }

    @Override
    public void addPlayer(OnlinePlayer player) {
        super.addPlayer(player);
        gameState.addPlayer();
    }

    // TODO надо четко определить фукционирование и области действия классов, один удаляет сокеты, другой не имеет права с ними работать
    // TODO третий наоборот все делает с сокетами а первым двум только позволяет произвести какие то действия (реакция на событие, не больше)
    @Override
    public void onPlayerDisconnect(OnlinePlayer player) {
        int playerNum = playerNum(player);
        if (gameState.isWarmUp()) {
            gameState.getPawns().remove(playerNum);
            removePlayer(playerNum);
        } else {
            gameState.getPawns().get(playerNum).die();
            if (areAllDead()) {
                stop();
            }
        }
    }

    @Override
    protected void stop() {
        super.stop();
    }

    //**************************
    // GAME LOGIC
    //**************************

    // todo - problem with double destroy (if object will be in two destroyed cells we will destroy him twice)
    private void clearCells() {
        Cell cell;

        for (int i = 0; i < changedCells.size(); i++) {
            cell = changedCells.get(i);
            for (int j = 0; j < cell.getObjects().size(); j++) {
                onDestroy(cell.getObjects().get(j));
            }
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
                return;

            case Pawn:
                if (!gameState.isWarmUp()) {
                    onPlayerDeath((Pawn) object);
                    if (areAllDead()) {
                        stop();
                    }
                } else {
                    ((Pawn) object).riseAgain();
                }
                return;
            default:
        }
    }

    private void onPlayerDeath(Pawn object) {
        String message = JsonHelper.toJson(new OutgoingMessage(MessageType.GAME_OVER, "YOU DIED"));
        int playerNum = gameState.playerNum(object);
        sendTo(playerNum, message);
        connections.unlink(getPlayer(playerNum), this);
    }

    private boolean areAllDead() {
        return gameState.deadPawnsAmount() >= properties.getMaxPlayerAmount() - 1;
    }

    //********************************
    // REPLICA
    //********************************

    private void addAliveObjectsToReplica() {
        replica.addAllToReplica(gameState.getBombs());
        replica.addAllToReplica(gameState.getPawns());
    }

    private void addObjectToReplica(GameObject object) {
        replica.addToReplica(object);
    }

    private void sendReplica() {
        String message = JsonHelper.toJson(new OutgoingMessage(MessageType.REPLICA, replica.toStringAndClear()));
        sendAll(message);
    }

    private void sendReplicaTo(int playerNum, String replica) {
        replica = JsonHelper.toJson(new OutgoingMessage(MessageType.REPLICA, replica));
        sendTo(playerNum, replica);
    }

    private String getPossess() {
        Possess possess = new Possess(gameState.getSizeY(), gameState.getSizeX(), 0);
        return JsonHelper.toJson(new OutgoingMessage(MessageType.POSSESS, JsonHelper.toJson(possess)));
    }
}
