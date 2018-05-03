package ru.atom.chat.socket.objects;

import ru.atom.chat.socket.objects.base.Cell;
import ru.atom.chat.socket.objects.base.GameObject;
import ru.atom.chat.socket.objects.base.Position;
import ru.atom.chat.socket.objects.ingame.Wall;
import ru.atom.chat.socket.objects.ingame.Wood;
import ru.atom.chat.socket.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private static final int X_CELL_SIZE = 32;
    private static final int Y_CELL_SIZE = 32;
    private final List<List<Cell>> cells;
    private final int lengthX;
    private final int lengthY;


    public GameState() {
        this(27, 17);
    }

    public GameState(int lengthX, int lengthY) {
        this.lengthX = lengthX;
        this.lengthY = lengthY;
        cells = new ArrayList<>();
        Cell cell;
        for (int i = 0; i < lengthX; i++) {
            cells.add(new ArrayList<>());
            for (int j = 0; j < lengthY; j++) {
                cell = new Cell(new Position(i * X_CELL_SIZE, j * Y_CELL_SIZE));
                cells.get(i).add(cell);
                if ((i > 0) && (j > 0) && (i < (lengthX - 1)) && (j < (lengthY - 1))) {
                    if (((i % 2) & (j % 2)) == 1)
                        cell.addObject(new Wall(cell.getPosition()));
                }
                if ((i > 1) && (i < (lengthX - 2)) || (j > 1) && (j < (lengthY - 2))) {
                    if (((i % 2) ^ (j % 2)) == 1)
                        cell.addObject(new Wood(cell.getPosition()));
                }
            }
        }
    }

    public String getReplica() {
        List<GameObject> allObjects = new ArrayList<>(lengthX * lengthY);
        cells.forEach(
                column -> column.forEach(
                        cell -> allObjects.addAll(cell.getChangedObjects())
                )
        );
        return JsonHelper.toJson(allObjects);
    }

    public Cell get(int x, int y) {
        return cells.get(x).get(y);
    }
}
