package ru.atom.game.gamesession.session;

import ru.atom.game.gamesession.properties.GameSessionProperties;
import ru.atom.game.gamesession.state.GameState;
import ru.atom.game.objects.base.Cell;
import ru.atom.game.objects.base.GameObject;
import ru.atom.game.objects.ingame.Bomb;
import ru.atom.game.objects.ingame.ObjectCreator;
import ru.atom.game.objects.ingame.Pawn;
import ru.atom.game.socket.message.response.messagedata.Replica;

import java.util.ArrayList;
import java.util.List;

public class BombProcessor {
    private final GameSessionProperties properties;
    private final GameState gameState;
    private final ObjectCreator creator;
    private final Replica replica;
    private final ArrayList<Cell> changedCells;

    BombProcessor(GameSessionProperties properties, GameState gameState, ObjectCreator creator, Replica replica) {
        this.properties = properties;
        this.gameState = gameState;
        this.creator = creator;
        this.replica = replica;
        changedCells = new ArrayList<>();
    }

    public void plantBombBy(Pawn playerPawn) {
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

    public ArrayList<Cell> tickBomb(long ms) {
        List<Bomb> bombs = gameState.getBombs();
        List<Bomb> blowIt = new ArrayList<>();
        for (Bomb bomb : bombs) {
            bomb.tick(ms);
            if (bomb.isReady()) {
                blowIt.add(bomb);
            }
        }
        blowAll(blowIt);
        bombs.removeIf(Bomb::isDestroyed);
        return changedCells;
    }

    private void blowAll(List<Bomb> blowIt) {
        List<Bomb> nextBlow;
        do {
            blowIt.forEach(bomb -> bomb.setBlown(true));
            nextBlow = new ArrayList<>();

            for (Bomb bomb : blowIt) {
                blowBomb(bomb, nextBlow);
            }

            blowIt.forEach(bomb -> creator.destroy(bomb));
            blowIt = nextBlow;
        } while (blowIt.size() != 0);
    }

    private void blowBomb(Bomb bomb, List<Bomb> nextBlow) {

        int x = bomb.getIntX() / 32;
        int y = bomb.getIntY() / 32;
        blow(gameState.get(x, y), nextBlow);
        int blowRange = bomb.getOwner().getBlowRange();

        for (int j = 1; j <= blowRange && (y + j) < gameState.getSizeY(); j++) {
            if (blow(gameState.get(x, y + j), nextBlow))
                break;
        }
        for (int j = 1; j <= blowRange && (x + j) < gameState.getSizeX(); j++) {
            if (blow(gameState.get(x + j, y), nextBlow))
                break;
        }
        for (int j = 1; j <= blowRange && (y - j) >= 0; j++) {
            if (blow(gameState.get(x, y - j), nextBlow))
                break;
        }
        for (int j = 1; j <= blowRange && (x - j) >= 0; j++) {
            if (blow(gameState.get(x - j, y), nextBlow))
                break;
        }
        bomb.getOwner().decBombCount();
    }

    // todo we add a lot of fires and trying to add cells too often, need to optimise
    private boolean blow(Cell cell, List<Bomb> nextBlow) {
        // if we return true we stop blowing !
        boolean destroyed = true;
        boolean stopDestroying = false;
        boolean changed = false;

        for (GameObject object : cell.getObjects()) {
            switch (object.getType()) {
                case Bomb:
                    // todo remember that boms what blown, and destoy them after this bomb was destroyed !!!
                    // todo here we get parameter from property, like we wanna stop 2 bombs blowing near, or not
                    // if we blow as one we have to stop when we hit bomb, else, we continue blowing
                    // stopDestroying = properties.isBlowStopsOnWall() && (!object.isDestroyed() || properties.isBombBlowAsOne());

                    Bomb bomb = (Bomb) object;
                    if (bomb.isBlown()) {
                        if (bomb.isDestroyed()) {
                            stopDestroying = stopDestroying || properties.isBlowStopsOnWall() && properties.isBombBlowAsOne();
                        } else {
                            changed = true;
                            stopDestroying = properties.isBlowStopsOnWall();
                        }
                    } else {
                        if (!bomb.isReady()) {
                            bomb.stop();
                            nextBlow.add(bomb);
                            changed = true;
                            stopDestroying = properties.isBlowStopsOnWall();
                        }
                    }
                    // stopDestroying = stopDestroying || properties.isBlowStopsOnWall() && (!object.isDestroyed() || properties.isBombBlowAsOne
                    break;

                case Bonus:
                case Wood:
                    stopDestroying = stopDestroying || properties.isBlowStopsOnWall() && (!object.isDestroyed() || properties.isBombBlowAsOne());
                    changed = true;
                    destroyed = destroyed && creator.destroy(object);
                    break;

                case Pawn:
                    if (gameState.isWarmUp())
                        continue;
                    changed = true;
                    destroyed = destroyed && creator.destroy(object);
                    break;

                default:
                    destroyed = destroyed && creator.destroy(object);
            }
        }
        if (changed)
            addChangedCell(cell);

        if (destroyed)
            addObjectToReplica(creator.createFire(cell.getPosition()));
        return !destroyed || stopDestroying;
    }

    private void addObjectToReplica(GameObject object) {
        replica.addToReplica(object);
    }

    private void addChangedCell(Cell cell) {
        if (!changedCells.contains(cell))
            changedCells.add(cell);
    }
}
