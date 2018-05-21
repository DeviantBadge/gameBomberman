package ru.atom.chat.socket.objects.gamesession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;
import ru.atom.chat.socket.enums.Direction;
import ru.atom.chat.socket.enums.MessageType;
import ru.atom.chat.socket.message.request.IncomingMessage;
import ru.atom.chat.socket.message.request.messagedata.Name;
import ru.atom.chat.socket.message.response.OutgoingMessage;
import ru.atom.chat.socket.message.response.messagedata.Replica;
import ru.atom.chat.socket.objects.ObjectCreator;
import ru.atom.chat.socket.objects.base.Cell;
import ru.atom.chat.socket.objects.base.GameObject;
import ru.atom.chat.socket.objects.base.util.Mover;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.objects.gamesession.state.GameState;
import ru.atom.chat.socket.objects.ingame.Bomb;
import ru.atom.chat.socket.objects.ingame.Bonus;
import ru.atom.chat.socket.objects.ingame.Fire;
import ru.atom.chat.socket.objects.ingame.Pawn;
import ru.atom.chat.socket.objects.orders.Order;
import ru.atom.chat.socket.enums.IncomingTopic;
import ru.atom.chat.socket.properties.GameSessionProperties;
import ru.atom.chat.socket.services.repos.GameSessionRepo;
import ru.atom.chat.socket.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;


// TODO move some functions to super, such as addOrder, act and so on (make another class mb OnlineSession that is abstract)
public class GameSession extends OnlineSession{

    private static final Logger log = LoggerFactory.getLogger(GameSession.class);

    private GameSessionRepo sessionRepo = GameSessionRepo.getInstance();
    private Mover mover = new Mover();

    private GameState gameState;
    private List<Cell> changedCells;

    // i wanna create game settings, for this i have to create some properties
    private GameSessionProperties properties;
    private ObjectCreator creator;

    // add objects to replica where we change them
    private Replica replica;

    //TODO add game session properties
    //TODO create array of destroyed objects for reason - we can kill player if we plant 2 bombs near crate (bombs must destroy together like one big bomb!)
    //TODO mb add something like game type - deathMatch singleLife

    // TODO i dont know how, but part of this code we have to move to another class

    public GameSession(GameSessionProperties properties) {
        super(properties.getMaxPlayerAmount());
        this.properties = properties;
        creator = new ObjectCreator(properties);
        gameState = new GameState(creator, properties.getMaxPlayerAmount());
        replica = new Replica();
        changedCells = new ArrayList<>();
    }

    protected Order buildOrder(IncomingMessage message, WebSocketSession session) {
        if (message.getTopic() == IncomingTopic.CONNECT) {
            String name = JsonHelper.fromJson(message.getData(), Name.class).getName();
            int playerNum = playerNum(name);
            if(playerNum == -1) {
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
    protected void carryOut(Order order) {
        switch (order.getIncomingTopic()) {
            case CONNECT:

                // TODO we can create game state when we create game, but then only get its replica
                // TODO Or when every body connected (we wait for it) we add it to replica
                // TODO bla bla, i dont know how to do it now
                if(playersAmount() < properties.getMaxPlayerAmount()) {
                    replica.addAllToReplica(gameState.getFieldReplica());
                } else {
                    sendReplica();
                    replica.addAllToReplica(gameState.recreate());
                }

                connectPlayerWithSocket(order.getPlayerNum(), order.getSession());


                    /* TODO Game state must generate starting player position, then we give it to players
                       TODO Smth like positions array and we add player only by add(playerPawn); */
                gameState.addPlayer(new Pawn(0,0));
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
        if (playerPawn.getBombCount() < playerPawn.getMaxBombAmount()) {
            boolean canPlant = true;
            for (GameObject object : gameState.get(playerPawn.getPosition().getCenter()).getObjects()) {
                switch (object.getType()) {
                    case Pawn:
                    case Fire:
                        break;
                    default:
                        canPlant = false;
                }
            }
            if (canPlant) {
                gameState.addBomb(new Bomb(playerPawn.getPosition().getCenter(), playerPawn));
                playerPawn.incBombCount();
            }
        }
    }

    protected void performTick(long ms) {
        movePlayers(ms);
        tickBomb(ms);
        clearCells();
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
            case Wood:
                addObjectToReplica(object);
                Bonus bonus = createBonus();
                if (bonus != null) {
                    gameState.get(object.getPosition()).addObject(bonus);
                    addObjectToReplica(bonus);
                }
                break;

            case Pawn:
                if(!gameState.isWarmUp()) {
                    onPlayerDeath((Pawn) object);

                    if (allDead()) {
                        stop();
                    }
                }
                break;
        }
    }

    private void onPlayerDeath(Pawn object) {
        String message = JsonHelper.toJson(new OutgoingMessage(MessageType.GAME_OVER, "YOU DIED"));
        sendTo(gameState.playerNum(object), message);
    }

    private void sendReplica() {
        String message = JsonHelper.toJson(new OutgoingMessage(MessageType.REPLICA, replica.toString()));
        sendAll(message);
    }

    private boolean allDead() {
        return gameState.deadPawnsAmount() >= properties.getMaxPlayerAmount() - 1;
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
        bomb.destroy();

        int x = bomb.getPosition().getIntX() / 32;
        int y = bomb.getPosition().getIntY() / 32;
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
                    stopDestroying = true;
                    changed = true;
                    break;

                case Pawn:
                    if(gameState.isWarmUp())
                        continue;
                    changed = true;
                    break;
            }
            destroyed = destroyed && object.destroy();
        }
        if (changed && !changedCells.contains(cell))
            changedCells.add(cell);

        if (destroyed)
            addObjectToReplica(new Fire(cell.getPosition()));
        return destroyed && !stopDestroying;
    }

    private Bonus createBonus() {
        int randomNum = (int) (Math.random() * 1000);
        if (randomNum <= 300) {
            if (randomNum >= 200) {
                return new Bonus(0, 0, Bonus.BonusType.SPEED);
            }
            if (randomNum >= 100) {
                return new Bonus(0, 0, Bonus.BonusType.RANGE);
            }
            if (randomNum >= 0) {
                return new Bonus(0, 0, Bonus.BonusType.BOMBS);
            }
        }
        return null;
    }


    // TODO i dont like it, but now i cant do better, remake it
    private void movePlayers(long ms) {
        for (Pawn pawn : gameState.getPawns()) {
            if (!pawn.isAlive())
                continue;
            if (pawn.isMoving()) {
                Position newPosition = mover.move(pawn, ms);
                newPosition = gameState.checkFieldBorders(newPosition);
                Cell prevCell = gameState.get(pawn.getPosition().getCenter());
                Cell newCell = gameState.get(newPosition.getCenter());

                if (prevCell == newCell || newCell.isEmpty()) {
                    if (newCell != prevCell) {
                        prevCell.removeObject(pawn);
                        newCell.addObject(pawn);
                    }
                    pawn.setPosition(newPosition);
                } else {
                    if (collide(pawn, newCell)) {
                        prevCell.removeObject(pawn);
                        newCell.addObject(pawn);
                        pawn.setPosition(newPosition);
                    } else {
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
                }
                pawn.setMoving(false);
            }
        }
    }

    private boolean collide(Pawn pawn, Cell newCell) {
        boolean canChangeCell = true;
        for (int i = 0; i < newCell.getObjects().size(); i++) {
            GameObject curObject = newCell.getObjects().get(i);
            switch (curObject.getType()) {
                case Bonus:
                    switch (((Bonus) curObject).getBonusType()) {
                        case BOMBS:
                            pawn.incMaxBombAmount();
                            break;
                        case RANGE:
                            pawn.incBlowRange();
                            break;
                        case SPEED:
                            pawn.incSpeedBonus();
                            break;
                    }
                    newCell.removeObject(i);
                    addObjectToReplica(curObject);
                    i--;
                case Pawn:
                    break;
                case Bomb:
                case Fire:
                case Wall:
                case Wood:
                    canChangeCell = false;
                    break;
            }
        }
        return canChangeCell;
    }

    private void addAliveObjectsToReplica() {
        replica.addAllToReplica(gameState.getBombs());
        replica.addAllToReplica(gameState.getPawns());
    }

    private void addObjectToReplica(GameObject object) {
        replica.addToReplica(object);
    }

    @Override
    protected void onStop() {
        super.onStop();
        String message = JsonHelper.toJson(new OutgoingMessage(MessageType.GAME_OVER, "YOU WON"));
        sendTo(gameState.getAliveNum(), message);
        sessionRepo.endGame(this);
    }
}
