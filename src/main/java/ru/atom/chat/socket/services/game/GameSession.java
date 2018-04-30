package ru.atom.chat.socket.services.game;

import org.springframework.web.socket.WebSocketSession;

public class GameSession {
    private final Integer id = 0;

    public GameSession(String playerName) {
        // if you want we can make players without names at first, you can change it
    }

    public Integer getId() {
        return id;
    }

    public void addPlayer(String name) {
    }

    public int playerReady(WebSocketSession session) {
        // here we match session with player

        return 0;
    }
}
