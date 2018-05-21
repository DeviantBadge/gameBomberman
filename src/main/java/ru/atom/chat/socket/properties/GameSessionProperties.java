package ru.atom.chat.socket.properties;

public class GameSessionProperties extends GameObjectProperties {
    // ********************************
    // Session
    // ********************************
    private int maxPlayerAmount = 2;

    public GameSessionProperties setMaxPlayerAmount(int maxPlayerAmount) {
        this.maxPlayerAmount = maxPlayerAmount;
        return this;
    }

    public int getMaxPlayerAmount() {
        return maxPlayerAmount;
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
        this.speedBonusCoef = speedBonusCoef;
        return this;
    }

    public GameSessionProperties setMovingSpeed(double movingSpeed) {
        this.movingSpeedX = movingSpeed;
        this.movingSpeedY = movingSpeed;
        return this;
    }

    public GameSessionProperties setMovingSpeedX(double movingSpeedX) {
        this.movingSpeedX = movingSpeedX;
        return this;
    }

    public GameSessionProperties setMovingSpeedY(double movingSpeedY) {
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
                .setMovingSpeed(Math.random() * 30 + 45);
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
