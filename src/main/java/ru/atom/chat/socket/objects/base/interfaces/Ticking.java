package ru.atom.chat.socket.objects.base.interfaces;

public interface Ticking {
    void start();

    void tick(long ms);

    boolean isReady();
}
