package ru.atom.chat.socket.objects.gamesession;

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
    private final List<List<Cell>> cells;
    private final List<Bomb> bombs;

    // TODO mb add pawns to here ? it could be faster and more comfortable
    private final List<Pawn> pawns;

    private final int sizeX;
    private final int sizeY;
    private boolean prepared;


    public GameState() {
        this(27, 17);
    }

    public GameState(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        cells = new ArrayList<>();
        bombs = new ArrayList<>();
        pawns = new ArrayList<>();
        prepared = false;
    }

    public List<? extends Replicable> createGameField(/*for example field pattern*/) {
        Cell cell;
        GameObject object;
        List<GameObject> replicables = new ArrayList<>();
        for (int i = 0; i < sizeX; i++) {
            cells.add(new ArrayList<>());
            for (int j = 0; j < sizeY; j++) {
                cell = new Cell(new Position(i * X_CELL_SIZE, j * Y_CELL_SIZE));

                cells.get(i).add(cell);
                if ((i > 0) && (j > 0) && (i < (sizeX - 1)) && (j < (sizeY - 1))) {
                    if (((i % 2) & (j % 2)) == 1) {
                        object = new Wall(cell.getPosition());
                        replicables.add(object);
                        cell.addObject(object);
                    }
                }
                if ((i > 1) && (i < (sizeX - 2)) || (j > 1) && (j < (sizeY - 2))) {
                    if (((i % 2) ^ (j % 2)) == 1){
                        object = new Wood(cell.getPosition());
                        replicables.add(object);
                        cell.addObject(object);
                    }
                }
            }
        }
        prepared = true;
        return replicables;
    }

    public Cell get(int x, int y) {
        return cells.get(x).get(y);
    }

    public void put(int x, int y, GameObject gameObject) {
        get(x, y).addObject(gameObject);
    }

    public void put(Position position, GameObject gameObject) {
        get(position).addObject(gameObject);
    }

    public Cell getCell(GameObject gameObject) {
        Position center = gameObject.getPosition().getCenter();
        return get(center.getIntX() / 32, center.getIntY() / 32);
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public boolean isReady() {
        return prepared;
    }

    public void addPlayer(Position position, Pawn player) {
        pawns.add(player);
        get(player.getPosition()).addObject(player);
    }

    public void addBomb(Position position, Bomb bomb) {
        bombs.add(bomb);
    }

    public List<Pawn> getPawns() {
        return pawns;
    }

    public Position checkBorders(Position newPosition) {
        double x = newPosition.getX(), y = newPosition.getY();
        if(newPosition.getX() < 0)
            x = 0;
        if(newPosition.getY() < 0)
            y = 0;
        if(newPosition.getX() + SizeParam.CELL_SIZE_X > sizeX * X_CELL_SIZE)
            x = sizeX * X_CELL_SIZE - SizeParam.CELL_SIZE_X;
        if(newPosition.getY() + SizeParam.CELL_SIZE_Y > sizeY * Y_CELL_SIZE)
            y = sizeY * Y_CELL_SIZE - SizeParam.CELL_SIZE_Y;
        return new Position(x, y);
    }

    public Position checkCellIsEmpty(Position position) {
        Cell cell = get(position.getCenter());
        if(cell.getObjects().isEmpty())
            return position;
        return position;
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
}
