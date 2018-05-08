package ru.atom.chat.socket.objects.gamesession;

import ru.atom.chat.socket.message.response.messagedata.Replica;
import ru.atom.chat.socket.objects.base.Cell;
import ru.atom.chat.socket.objects.base.GameObject;
import ru.atom.chat.socket.objects.base.Tile;
import ru.atom.chat.socket.objects.base.interfaces.Replicable;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.objects.base.util.SizeParam;
import ru.atom.chat.socket.objects.ingame.Bomb;
import ru.atom.chat.socket.objects.ingame.Pawn;
import ru.atom.chat.socket.objects.ingame.Wall;
import ru.atom.chat.socket.objects.ingame.Wood;
import ru.atom.chat.socket.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private static final int X_CELL_SIZE = 32;
    private static final int Y_CELL_SIZE = 32;
    private final List<List<Cell>> cells;
    private final List<Bomb> bombs;

    // TODO mb add pawns to here ? it could be faster and more comfortable
    private final List<Pawn> pawns;

    private final int lengthX;
    private final int lengthY;
    private boolean prepared;


    public GameState() {
        this(27, 17);
    }

    public GameState(int lengthX, int lengthY) {
        this.lengthX = lengthX;
        this.lengthY = lengthY;
        cells = new ArrayList<>();
        bombs = new ArrayList<>();
        pawns = new ArrayList<>();
        prepared = false;
    }

    public List<? extends Replicable> createGameField(/*for example field pattern*/) {
        Cell cell;
        GameObject object;
        List<GameObject> replicables = new ArrayList<>();
        for (int i = 0; i < lengthX; i++) {
            cells.add(new ArrayList<>());
            for (int j = 0; j < lengthY; j++) {
                cell = new Cell(new Position(i * X_CELL_SIZE, j * Y_CELL_SIZE));

                cells.get(i).add(cell);
                if ((i > 0) && (j > 0) && (i < (lengthX - 1)) && (j < (lengthY - 1))) {
                    if (((i % 2) & (j % 2)) == 1) {
                        object = new Wall(cell.getPosition());
                        replicables.add(object);
                        cell.addObject(object);
                    }
                }
                if ((i > 1) && (i < (lengthX - 2)) || (j > 1) && (j < (lengthY - 2))) {
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
        get(position.getX(), position.getY()).addObject(gameObject);
    }

    public Cell getCell(GameObject gameObject) {
        Position center = gameObject.getPosition().getCenter();
        return get(center.getX() / 32, center.getY() / 32);
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public boolean isReady() {
        return prepared;
    }
}
