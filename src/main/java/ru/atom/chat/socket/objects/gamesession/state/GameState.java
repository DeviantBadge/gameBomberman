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
import ru.atom.chat.socket.properties.GameSessionProperties;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private static final int X_CELL_SIZE = 32;
    private static final int Y_CELL_SIZE = 32;

    private final List<Bomb> bombs;
    private final List<Pawn> pawns;
    private boolean warmUp;

    private ObjectCreator creator;
    private GameField gameField;
    private GameSessionProperties properties;

    public GameState(GameSessionProperties properties, ObjectCreator creator) {
        this.properties = properties;
        this.creator = creator;
        gameField = new GameField(creator, properties.getFieldSizeX(), properties.getFieldSizeY());
        bombs = new ArrayList<>();
        pawns = new ArrayList<>();

        createWarmUpField();
    }

    private void createWarmUpField() {
        bombs.clear();
        gameField.createField(properties.getWarmUpFieldType());
        warmUp = true;
    }

    private void createGameField() {
        bombs.clear();
        gameField.createField(properties.getGameFieldType());
        warmUp = false;
    }

    public void recreate() {
        createGameField();
        resetPawns();
    }

    public List<Pawn> getPawns() {
        return pawns;
    }

    private void resetPawns() {
        int pawnAmount = pawns.size();
        getPawns().clear();
        for (int i = 0; i < pawnAmount; i++)
            addPlayer();
    }

    public void addPlayer() {
        Position playerPos = gameField.getRandomStartPos();
        Pawn player = creator.createPawn(playerPos);
        pawns.add(player);
        get(playerPos).addObject(player);
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public void addBomb(Bomb bomb) {
        bombs.add(bomb);
        Cell cell = get(bomb.getPosition());
        bomb.setPosition(cell.getPosition());
        cell.addObject(bomb);
    }

    public Position checkFieldBorders(Position newPosition) {
        double x = newPosition.getX(), y = newPosition.getY();
        if (newPosition.getX() < 0)
            x = 0;
        if (newPosition.getY() < 0)
            y = 0;
        if (newPosition.getX() + SizeParam.CELL_SIZE_X > getSizeX() * X_CELL_SIZE)
            x = getSizeX() * X_CELL_SIZE - SizeParam.CELL_SIZE_X;
        if (newPosition.getY() + SizeParam.CELL_SIZE_Y > getSizeY() * Y_CELL_SIZE)
            y = getSizeY() * Y_CELL_SIZE - SizeParam.CELL_SIZE_Y;
        return new Position(x, y);
    }

    public Cell get(int x, int y) {
        return gameField.get(x, y);
    }

    public Cell get(Position position) {
        return get(position.getIntX() / X_CELL_SIZE, position.getIntY() / Y_CELL_SIZE);
    }

    public int getSizeX() {
        return gameField.getSizeX();
    }

    public int getSizeY() {
        return gameField.getSizeY();
    }

    public boolean isWarmUp() {
        return warmUp;
    }

    public int playerNum(Pawn pawn) {
        return pawns.indexOf(pawn);
    }

    public int getAliveNum() {
        for (int i = 0; i < pawns.size(); i++) {
            if (pawns.get(i).isAlive())
                return i;
        }
        return -1;
    }

    public int deadPawnsAmount() {
        int amount = 0;
        for (int i = 0; i < pawns.size(); i++) {
            if (!pawns.get(i).isAlive())
                amount++;
        }
        return amount;
    }

    // sometimes player will be under field
    public List<Replicable> getFieldReplica() {
        return gameField.getFieldReplica();
    }
}
