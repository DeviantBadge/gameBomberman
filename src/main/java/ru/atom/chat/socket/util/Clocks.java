package ru.atom.chat.socket.util;

public class Clocks {
    private Clocks() throws IllegalAccessException{
        throw new IllegalAccessException("Haha, you cant create it :P");
    }

    // Later mb i will update it, so
    // TODO: 04.05.2018 optimize it

    public static long getTimeMS() {
        return System.currentTimeMillis();
    }

    public static long getTimeNano() {
        return System.nanoTime();
    }
}
