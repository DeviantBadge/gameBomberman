package ru.atom.chat.socket.objects.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class GameObject {
    private static IdGen idGen = new IdGen();
    private final Integer id;
    private final String type;
    private final Position position;

    public GameObject(String type, Position position) {
        id = idGen.generateId();
        this.type = type;
        this.position = position;
    }

    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    @JsonIgnore
    protected String getEntrails() {
        return "id:" + id + ",type:" + type + ",position:" + position;
    }

    @Override
    public String toString() {
        return "{" + getEntrails() + "}";
    }
}
