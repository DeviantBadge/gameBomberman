package ru.atom.game.gamesession.state;

import ru.atom.game.enums.FieldType;
import ru.atom.game.enums.ObjectType;
import ru.atom.game.objects.base.Cell;
import ru.atom.game.objects.base.GameObject;
import ru.atom.game.objects.base.interfaces.Replicable;
import ru.atom.game.objects.base.util.Position;
import ru.atom.game.objects.ingame.ObjectCreator;

import java.util.ArrayList;
import java.util.List;

public class GameField {
    private static final int X_CELL_SIZE = 32;
    private static final int Y_CELL_SIZE = 32;

    // TODO make another class Field, create it by patterns (it will create by creator)
    // TODO if we want to add new maps we add it here, but i think we could do it better
    private final List<List<Cell>> cells;
    private List<Position> initialPositions;
    private List<Position> freePositions;
    private final int sizeX;
    private final int sizeY;

    private ObjectCreator creator;

    GameField(ObjectCreator creator, int sizeX, int sizeY) {
        this.creator = creator;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        cells = new ArrayList<>();

        initialPositions = new ArrayList<>();
        freePositions = new ArrayList<>();
    }

    public void createField(FieldType type) {
        switch (type) {
            case WARM_UP:
                createWarmUpField();
                break;
            case GAME_LOW_DENSITY:
                createGameFieldLow();
                break;
            case GAME_HIGH_DENSITY:
                createGameFieldHigh();
                break;
            case BONUS_VEIN:
                createBonusVein();
                break;
            default:
                break;
        }
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
        generateCommonPlayerPositions();
    }

    private void createGameFieldLow(/*for example field pattern*/) {
        Cell cell;
        cells.clear();

        for (int i = 0; i < sizeX; i++) {
            cells.add(new ArrayList<>());
            for (int j = 0; j < sizeY; j++) {
                cell = new Cell(new Position(i * X_CELL_SIZE, j * Y_CELL_SIZE));
                cells.get(i).add(cell);

                if ((i > 0) && (j > 0) && (i < (sizeX - 1)) && (j < (sizeY - 1))) {
                    if (((i % 2) & (j % 2)) == 1)
                        cell.addObject(creator.createWall(cell.getPosition()));
                }
                if ((i > 1) && (i < (sizeX - 2)) || (j > 1) && (j < (sizeY - 2))) {
                    if (((i % 2) ^ (j % 2)) == 1)
                        cell.addObject(creator.createWood(cell.getPosition()));
                }
            }
        }
        generateCommonPlayerPositions();
    }

    private void createGameFieldHigh() {
        Cell cell;
        cells.clear();

        for (int i = 0; i < sizeX; i++) {
            cells.add(new ArrayList<>());
            for (int j = 0; j < sizeY; j++) {
                cell = new Cell(new Position(i * X_CELL_SIZE, j * Y_CELL_SIZE));
                cells.get(i).add(cell);

                if ((i > 0) && (i < (sizeX - 1)) || (j > 0) && (j < (sizeY - 1))) {
                    if (((i % 2) & (j % 2)) == 1) {
                        cell.addObject(creator.createWall(cell.getPosition()));
                        continue;
                    }
                }
                if ((i > 1) && (i < (sizeX - 2)) || (j > 1) && (j < (sizeY - 2))) {
                    cell.addObject(creator.createWood(cell.getPosition()));
                }
            }
        }
        generateCommonPlayerPositions();
    }

    private void createBonusVein() {
        Cell cell;
        cells.clear();
        int veinSizeX = Math.max(sizeX, sizeY) / 5;
        int veinSizeY = Math.max(sizeX, sizeY) / 5;
        if ((veinSizeX & 1) != (sizeX & 1))
            --veinSizeX;
        if ((veinSizeY & 1) != (sizeY & 1))
            --veinSizeY;
        int veinPosX = (sizeX - veinSizeX) / 2;
        int veinPosY = (sizeY - veinSizeY) / 2;

        for (int i = 0; i < sizeX; i++) {
            cells.add(new ArrayList<>());
            for (int j = 0; j < sizeY; j++) {
                cell = new Cell(new Position(i * X_CELL_SIZE, j * Y_CELL_SIZE));
                cells.get(i).add(cell);

                if ((i >= veinPosX) && (i < veinPosX + veinSizeX)
                        && (j >= veinPosY) && (j < veinPosY + veinSizeY)) {
                    cell.addObject(creator.createBonus(cell.getPosition(), false));
                    continue;
                }
                if ((i > 0) && (i < (sizeX - 1)) || (j > 0) && (j < (sizeY - 1))) {
                    if (((i % 2) & (j % 2)) == 1) {
                        cell.addObject(creator.createWall(cell.getPosition()));
                        continue;
                    }
                }
                if ((i > 1) && (i < (sizeX - 2)) || (j > 1) && (j < (sizeY - 2))) {
                    cell.addObject(creator.createWood(cell.getPosition()));
                }
            }
        }
        generateCommonPlayerPositions();
    }

    private void generateCommonPlayerPositions() {
        initialPositions.clear();
        freePositions.clear();
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    initialPositions.add(get(0, 0).getPosition());
                    break;
                case 1:
                    initialPositions.add(get(sizeX - 1, sizeY - 1).getPosition());
                    break;
                case 2:
                    initialPositions.add(get(0, sizeY - 1).getPosition());
                    break;
                case 3:
                    initialPositions.add(get(sizeX - 1, 0).getPosition());
                    break;
            }
        }
        freePositions.addAll(initialPositions);
    }

    Position getRandomStartPos() {
        if (freePositions.size() == 0) {
            freePositions.addAll(initialPositions);
        }
        int num = ((int) (freePositions.size() * Math.random()));
        return freePositions.remove(num);
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public Cell get(int x, int y) {
        return cells.get(x).get(y);
    }

    // sometimes player will be under field
    public List<Replicable> getFieldReplica() {
        List<Replicable> replicables = new ArrayList<>();
        List<Cell> column;
        Cell cell;
        GameObject object;
        for (int i = 0; i < cells.size(); i++) {
            column = cells.get(i);
            for (int j = 0; j < column.size(); j++) {
                cell = column.get(j);
                for (int k = 0; k < cell.getObjects().size(); k++) {
                    object = cell.get(k);
                    if (object.getType() != ObjectType.Pawn && object.getType() != ObjectType.Bomb)
                        replicables.add(object);
                }
            }
        }
        return replicables;
    }
}
