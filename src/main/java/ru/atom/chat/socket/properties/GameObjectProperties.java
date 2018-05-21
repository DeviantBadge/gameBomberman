package ru.atom.chat.socket.properties;

public class GameObjectProperties {
    // ********************************
    // Bombs
    // ********************************
    private long bombBlowTimeMs = 5000;

    public GameObjectProperties setBombBlowTimeMs(long bombBlowTimeMs) {
        this.bombBlowTimeMs = bombBlowTimeMs;
        return this;
    }
    public long getBombBlowTimeMs() {
        return bombBlowTimeMs;
    }


    // ********************************
    // Bonuses
    // ********************************
    private double bonusProbability = 0.3;
    private double speedProbability = 1 / 3.0;
    private double bombsProbability = 1 / 3.0;

    private double rangeProbability() {
        return 1 - speedProbability - bombsProbability;
    }

    public GameObjectProperties setBonusProbability(double bonusProbability) {
        if (bonusProbability < 0)
            this.bonusProbability = 0;
        else {
            if (bombsProbability > 1) {
                this.bonusProbability = 1;
            } else
                this.bonusProbability = bonusProbability;
        }
        return this;
    }

    public GameObjectProperties setProbabilities(double speedProbability, double bombsProbability, double rangeProbability) {
        speedProbability = Math.abs(speedProbability);
        bombsProbability = Math.abs(bombsProbability);
        rangeProbability = Math.abs(rangeProbability);
        double sum = speedProbability + bombsProbability + rangeProbability;
        speedProbability = speedProbability / sum;
        bombsProbability = bombsProbability / sum;
        this.speedProbability = speedProbability;
        this.bombsProbability = bombsProbability;
        return this;
    }

    public double getBonusProbability() {
        return bonusProbability;
    }

    public double getSpeedProbability() {
        return speedProbability;
    }

    public double getBombsProbability() {
        return bombsProbability;
    }

    public double getRangeProbability() {
        return rangeProbability();
    }


    // ********************************
    // Pawn
    // ********************************
    private int speedOnStart = 0;
    private int bombsOnStart = 1;
    private int rangeOnStart = 1;

    public GameObjectProperties setSpeedOnStart(int speedOnStart) {
        this.speedOnStart = speedOnStart;
        return this;
    }

    public GameObjectProperties setBombsOnStart(int bombsOnStart) {
        this.bombsOnStart = bombsOnStart;
        return this;
    }

    public GameObjectProperties setRangeOnStart(int rangeOnStart) {
        this.rangeOnStart = rangeOnStart;
        return this;
    }

    public int getSpeedOnStart() {
        return speedOnStart;
    }

    public int getBombsOnStart() {
        return bombsOnStart;
    }

    public int getRangeOnStart() {
        return rangeOnStart;
    }


    // ********************************
    // Wall
    // ********************************
    // mb destroyable


    // ********************************
    // Wood
    // ********************************
    // mb destroyable

    // ********************************
    // For some fun
    // ********************************
    public GameObjectProperties random() {
        return this.setBonusProbability(Math.random())
                .setProbabilities(Math.random(), Math.random(), Math.random())
                .setSpeedOnStart((int) (Math.random() * 3) + 1)
                .setBombsOnStart((int) (Math.random() * 3) + 1)
                .setRangeOnStart((int) (Math.random() * 3) + 1);
    }
}
