package ru.atom.game.objects.base.util;

public class SizeParam {
    public static final int CELL_SIZE_X = 32;
    public static final int CELL_SIZE_Y = 32;

    private SizeParam() throws IllegalAccessException{
        throw new IllegalAccessException("You cant create it");
    }
}
