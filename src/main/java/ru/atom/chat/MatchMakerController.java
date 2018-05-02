package ru.atom.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.atom.chat.socket.services.game.GameService;
import ru.atom.chat.socket.services.game.GameSession;

import java.util.concurrent.ConcurrentLinkedQueue;

@Controller
@RequestMapping("matchmaker")
public class MatchMakerController {
    private static final Logger log = LoggerFactory.getLogger(MatchMakerController.class);

    @Autowired
    private GameService gameService;

    private final ConcurrentLinkedQueue<GameSession> sessions = new ConcurrentLinkedQueue<>();

    @RequestMapping(
            path = "join",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> join(@RequestParam("name") String name) {
        GameSession session;
        if ((session = sessions.poll()) == null) {
            session = gameService.createGame(name);
            sessions.add(session);
        }
        session.addPlayer(name);

        log.warn(session.getId().toString());
        return new ResponseEntity<>(session.getId().toString(), HttpStatus.OK);
    }
}
