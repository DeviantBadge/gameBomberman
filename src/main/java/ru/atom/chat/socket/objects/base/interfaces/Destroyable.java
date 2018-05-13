package ru.atom.chat.socket.objects.base.interfaces;

public interface Destroyable {
    boolean isDestroyable();

    boolean destroy();

    boolean isDestroyed();
}
