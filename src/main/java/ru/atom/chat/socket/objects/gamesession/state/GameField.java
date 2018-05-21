package ru.atom.chat.socket.objects.gamesession.state;

import ru.atom.chat.socket.objects.base.Cell;
import ru.atom.chat.socket.objects.base.GameObject;
import ru.atom.chat.socket.objects.base.interfaces.Replicable;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.objects.ingame.ObjectCreator;

import java.util.ArrayList;
import java.util.List;

public class GameField {
    private static final int X_CELL_SIZE = 32;
    private static final int Y_CELL_SIZE = 32;

    // TODO make another class Field, create it by patterns (it will create by creator)
    private final List<List<Cell>> cells;
    private List<Position> playerPositions;
    private final int sizeX;
    private final int sizeY;

    private ObjectCreator creator;

    GameField (ObjectCreator creator, int sizeX, int sizeY, int playerAmount) {
        this.creator = creator;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        cells = new ArrayList<>();

        playerPositions = new ArrayList<>(playerAmount);
    }

    private void createWarmUpField() {
        Cell cell;
        cells.clear();

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
    }


    public List<? extends Replicable> createGameField() {
        Cell cell;
        GameObject object;
        List<GameObject> replicables = new ArrayList<>();
        cells.clear();

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
        return replicables;
    }
}
