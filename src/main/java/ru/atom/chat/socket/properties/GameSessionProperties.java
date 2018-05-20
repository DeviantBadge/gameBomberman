package ru.atom.chat.socket.properties;

public class GameSessionProperties extends GameObjectProperties {
    private int maxPlayerAmount = 2;

    public int getMaxPlayerAmount() {
        return maxPlayerAmount;
    }
}
