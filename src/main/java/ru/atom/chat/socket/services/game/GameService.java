package ru.atom.chat.socket.services.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.objects.gamesession.GameSession;
import ru.atom.chat.socket.util.JsonHelper;

@Component
public class GameService {
    private static final Logger log = LoggerFactory.getLogger(GameService.class);

    public GameSession createGame() {
        return new GameSession();
    }

    public GameSession findGameById(String id) {
        return new GameSession();
    }
}
