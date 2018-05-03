package ru.atom.chat.socket.objects.base.interfaces;

public interface Ticking {
    void tick(int ms);

    boolean isReady();
}
