package ru.atom.chat.socket.services.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GameService {
    private static final Logger log = LoggerFactory.getLogger(GameService.class);

    public GameSession createGame(String playerName) {
        return new GameSession(playerName);
    }
}
