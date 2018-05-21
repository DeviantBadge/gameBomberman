package ru.atom.chat.socket.objects.gamesession.state;

import ru.atom.chat.socket.objects.ingame.ObjectCreator;
import ru.atom.chat.socket.objects.base.Cell;
import ru.atom.chat.socket.objects.base.GameObject;
import ru.atom.chat.socket.objects.base.interfaces.Replicable;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.objects.base.util.SizeParam;
import ru.atom.chat.socket.objects.ingame.Bomb;
import ru.atom.chat.socket.objects.ingame.Pawn;
import ru.atom.chat.socket.objects.ingame.Wall;
import ru.atom.chat.socket.objects.ingame.Wood;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private static final int X_CELL_SIZE = 32;
    private static final int Y_CELL_SIZE = 32;

    private final List<Bomb> bombs;
    private final List<Pawn> pawns;
    private int playerAmount;
    private boolean warmUp;

    private ObjectCreator creator;

    // TODO make another class Field, create it by patterns (it will create by creator)
    private final List<List<Cell>> cells;
    private List<Position> playerPositions;
    private final int sizeX;
    private final int sizeY;


    public GameState(ObjectCreator creator, int playerAmount) {
        this(creator, 27, 17, playerAmount);
    }

    private GameState(ObjectCreator creator, int sizeX, int sizeY, int playerAmount) {
        this.creator = creator;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        cells = new ArrayList<>();
        bombs = new ArrayList<>();
        pawns = new ArrayList<>(playerAmount);

        playerPositions = new ArrayList<>(playerAmount);
        this.playerAmount = playerAmount;

        createWarmUpField();
    }

    private void createWarmUpField() {
        Cell cell;
        cells.clear();
        bombs.clear();

        for (int i = 0; i < sizeX; i++) {
            cells.add(new ArrayList<>());
            for (int j = 0; j < sizeY; j++) {
                cell = new Cell(new Position(i * X_CELL_SIZE, j * Y_CELL_SIZE));

                cells.get(i).add(cell);
                if ((i > 0) && (j > 0) && (i < (sizeX - 1)) && (j < (sizeY - 1))) {
                    cell.addObject(creator.createWood(cell.getPosition()));
                }
            }
        }
        generatePlayerPositions();
        warmUp = true;
    }

    public List<? extends Replicable> createGameField(/*for example field pattern*/) {
        Cell cell;
        GameObject object;
        List<GameObject> replicables = new ArrayList<>();
        cells.clear();
        bombs.clear();

        for (int i = 0; i < sizeX; i++) {
            cells.add(new ArrayList<>());
            for (int j = 0; j < sizeY; j++) {
                cell = new Cell(new Position(i * X_CELL_SIZE, j * Y_CELL_SIZE));

                cells.get(i).add(cell);
                if ((i > 0) && (j > 0) && (i < (sizeX - 1)) && (j < (sizeY - 1))) {
                    if (((i % 2) & (j % 2)) == 1) {
                        object = creator.createWall(cell.getPosition());
                        cell.addObject(object);
                        replicables.add(object);
                    }
                }
                if ((i > 1) && (i < (sizeX - 2)) || (j > 1) && (j < (sizeY - 2))) {
                    if (((i % 2) ^ (j % 2)) == 1) {
                        object = creator.createWood(cell.getPosition());
                        cell.addObject(object);
                        replicables.add(object);
                    }
                }
            }
        }
        generatePlayerPositions();
        warmUp = false;
        return replicables;
    }

    public List<? extends Replicable> recreate() {
        List<? extends Replicable> replicables = createGameField();
        resetPawns();
        return replicables;
    }

    private void resetPawns() {
        int pawnAmount = pawns.size();
        getPawns().clear();
        for(int i = 0; i < pawnAmount; i ++)
            addPlayer();
    }

    private void generatePlayerPositions() {
        playerPositions.clear();
        for (int i = 0; i < playerAmount; i++) {
            switch (i) {
                case 0:
                    playerPositions.add(get(0, 0).getPosition());
                    break;
                case 1:
                    playerPositions.add(get(sizeX - 1, sizeY - 1).getPosition());
                    break;
                case 2:
                    playerPositions.add(get(0, sizeY - 1).getPosition());
                    break;
                case 3:
                    playerPositions.add(get(sizeX - 1, 0).getPosition());
                    break;
            }
        }
    }

    public Cell get(int x, int y) {
        return cells.get(x).get(y);
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public void addPlayer() {
        Position playerPos = getRandomStartPos();
        Pawn player = creator.createPawn(playerPos);
        pawns.add(player);
        get(playerPos).addObject(player);
    }

    private Position getRandomStartPos() {
        if(playerPositions.size() == 0) {
            int i = ((int) (4 * Math.random()));
            switch (i) {
                case 0:
                    return get(0, 0).getPosition();
                case 1:
                    return get(sizeX - 1, sizeY - 1).getPosition();
                case 2:
                    return get(0, sizeY - 1).getPosition();
                case 3:
                    return get(sizeX - 1, 0).getPosition();
            }
        } else {
            int num = ((int) (playerPositions.size() * Math.random()));
            Position position = playerPositions.get(num);
            playerPositions.remove(num);
            return position;
        }
        return new Position(0,0);
    }

    public void addBomb(Bomb bomb) {
        bombs.add(bomb);
        get(bomb.getPosition()).addObject(bomb);
    }

    public List<Pawn> getPawns() {
        return pawns;
    }

    public Position checkFieldBorders(Position newPosition) {
        double x = newPosition.getX(), y = newPosition.getY();
        if (newPosition.getX() < 0)
            x = 0;
        if (newPosition.getY() < 0)
            y = 0;
        if (newPosition.getX() + SizeParam.CELL_SIZE_X > sizeX * X_CELL_SIZE)
            x = sizeX * X_CELL_SIZE - SizeParam.CELL_SIZE_X;
        if (newPosition.getY() + SizeParam.CELL_SIZE_Y > sizeY * Y_CELL_SIZE)
            y = sizeY * Y_CELL_SIZE - SizeParam.CELL_SIZE_Y;
        return new Position(x, y);
    }

    public Cell get(Position position) {
        return get(position.getIntX() / X_CELL_SIZE, position.getIntY() / Y_CELL_SIZE);
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public boolean isWarmUp() {
        return warmUp;
    }

    // sometimes player will be under field
    public List<Replicable> getFieldReplica() {
        List<Replicable> replicables = new ArrayList<>();
        cells.forEach(
                column -> column.forEach(
                        cell -> replicables.addAll(cell.getObjects())
                )
        );
        return replicables;
    }

    public int playerNum(Pawn pawn) {
        return pawns.indexOf(pawn);
    }

    public int getAliveNum() {
        for(int i = 0; i < pawns.size(); i ++) {
            if(pawns.get(i).isAlive())
                return i;
        }
        return -1;
    }

    public int deadPawnsAmount() {
        int amount = 0;
        for(int i = 0; i < pawns.size(); i ++) {
            if(!pawns.get(i).isAlive())
                amount++;
        }
        return amount;
    }
}
