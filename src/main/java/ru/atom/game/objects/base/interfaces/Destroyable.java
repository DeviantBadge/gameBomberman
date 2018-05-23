package ru.atom.game.objects.base.interfaces;

public interface Destroyable {
    boolean isDestroyable();

    boolean destroy();

    boolean isDestroyed();
}
