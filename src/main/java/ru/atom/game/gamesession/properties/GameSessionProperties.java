package ru.atom.game.gamesession.properties;

import ru.atom.game.gamesession.state.FieldType;

public class GameSessionProperties extends GameObjectProperties {
    // ********************************
    // Session
    // ********************************
    private int maxPlayerAmount = 2;
    private FieldType warmUpFieldType = FieldType.WARM_UP;
    private FieldType gameFieldType = FieldType.BONUS_VEIN;

    private int fieldSizeX = 27;
    private int fieldSizeY = 17;

    public GameSessionProperties setMaxPlayerAmount(int maxPlayerAmount) {
        maxPlayerAmount = intervalCheck(maxPlayerAmount, 2, 4);
        this.maxPlayerAmount = maxPlayerAmount;
        return this;
    }

    public GameSessionProperties setWarmUpFieldType(FieldType warmUpFieldType) {
        this.warmUpFieldType = warmUpFieldType;
        return this;
    }

    public GameSessionProperties setGameFieldType(FieldType gameFieldType) {
        this.gameFieldType = gameFieldType;
        return this;
    }

    public GameSessionProperties setFieldSizeX(int fieldSizeX) {
        fieldSizeX = intervalCheck(fieldSizeX, 10, 50);
        this.fieldSizeX = fieldSizeX;
        return this;
    }

    public GameSessionProperties setFieldSizeY(int fieldSizeY) {
        fieldSizeY = intervalCheck(fieldSizeY, 10, 50);
        this.fieldSizeY = fieldSizeY;
        return this;
    }

    public int getMaxPlayerAmount() {
        return maxPlayerAmount;
    }

    public FieldType getWarmUpFieldType() {
        return warmUpFieldType;
    }

    public FieldType getGameFieldType() {
        return gameFieldType;
    }

    public int getFieldSizeX() {
        return fieldSizeX;
    }

    public int getFieldSizeY() {
        return fieldSizeY;
    }

    // ********************************
    // Bombs
    // ********************************
    private boolean blowStopsOnWall = true;

    // not implemented
    private boolean bombBlowAsOne = true;
    private boolean additiveBombRadius = false;

    public GameSessionProperties setBlowStopsOnWall(boolean blowStopsOnWall) {
        this.blowStopsOnWall = blowStopsOnWall;
        return this;
    }

    public GameSessionProperties setBombBlowAsOne(boolean bombBlowAsOne) {
        this.bombBlowAsOne = bombBlowAsOne;
        return this;
    }

    public GameSessionProperties setAdditiveBombRadius(boolean additiveBombRadius) {
        this.additiveBombRadius = additiveBombRadius;
        return this;
    }

    public boolean isBlowStopsOnWall() {
        return blowStopsOnWall;
    }

    public boolean isBombBlowAsOne() {
        return bombBlowAsOne;
    }

    public boolean isAdditiveBombRadius() {
        return additiveBombRadius;
    }


    // ********************************
    // Pawn
    // ********************************
    private double speedBonusCoef = 1 / 3.0;
    private double movingSpeedX = 60;
    private double movingSpeedY = 60;

    public GameSessionProperties setSpeedBonusCoef(double speedBonusCoef) {
        speedBonusCoef = intervalCheck(speedBonusCoef, 0, 1);
        this.speedBonusCoef = speedBonusCoef;
        return this;
    }

    public GameSessionProperties setMovingSpeed(double movingSpeed) {
        movingSpeed = intervalCheck(movingSpeed, 10, 1000);
        this.movingSpeedX = movingSpeed;
        this.movingSpeedY = movingSpeed;
        return this;
    }

    public GameSessionProperties setMovingSpeedX(double movingSpeedX) {
        movingSpeedX = intervalCheck(movingSpeedX, 10, 1000);
        this.movingSpeedX = movingSpeedX;
        return this;
    }

    public GameSessionProperties setMovingSpeedY(double movingSpeedY) {
        movingSpeedY = intervalCheck(movingSpeedY, 10, 1000);
        this.movingSpeedY = movingSpeedY;
        return this;
    }

    public double getSpeedBonusCoef() {
        return speedBonusCoef;
    }

    public double getMovingSpeedX() {
        return movingSpeedX;
    }

    public double getMovingSpeedY() {
        return movingSpeedY;
    }

    // ************************
    // Super methods
    // ************************

    @Override
    public GameSessionProperties random() {
        return ((GameSessionProperties) super.random())
                .setMaxPlayerAmount((int) (Math.random() * 2) + 2)
                .setBlowStopsOnWall(((int) (Math.random() * 2)) > 0)
                .setSpeedBonusCoef(Math.random() / 2)
                .setMovingSpeed(Math.random() * 30 + 45)
                .setGameFieldType(FieldType.values()[(int) (Math.random() * FieldType.values().length)])
                .setWarmUpFieldType(FieldType.values()[(int) (Math.random() * FieldType.values().length)])
                .setFieldSizeX(27 + (int) (Math.random() * 5))
                .setFieldSizeY(17 + (int) (Math.random() * 5));
    }

    @Override
    public GameSessionProperties setBombBlowTimeMs(long bombBlowTimeMs) {
        return (GameSessionProperties) super.setBombBlowTimeMs(bombBlowTimeMs);
    }

    @Override
    public GameSessionProperties setBonusProbability(double bonusProbability) {
        return (GameSessionProperties) super.setBonusProbability(bonusProbability);
    }

    @Override
    public GameSessionProperties setProbabilities(double speedProbability, double bombsProbability, double rangeProbability) {
        return (GameSessionProperties) super.setProbabilities(speedProbability, bombsProbability, rangeProbability);
    }

    @Override
    public GameSessionProperties setSpeedOnStart(int speedOnStart) {
        return (GameSessionProperties) super.setSpeedOnStart(speedOnStart);
    }

    @Override
    public GameSessionProperties setBombsOnStart(int bombsOnStart) {
        return (GameSessionProperties) super.setBombsOnStart(bombsOnStart);
    }

    @Override
    public GameSessionProperties setRangeOnStart(int rangeOnStart) {
        return (GameSessionProperties) super.setRangeOnStart(rangeOnStart);
    }
}
