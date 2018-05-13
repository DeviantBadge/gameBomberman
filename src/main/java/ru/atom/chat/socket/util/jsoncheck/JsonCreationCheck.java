package ru.atom.chat.socket.util.jsoncheck;

import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.util.JsonHelper;

public class JsonCreationCheck {

    public static void main(String[] args) {
        System.out.println(JsonHelper.toJson(new Position(10,20)));
    }
}
