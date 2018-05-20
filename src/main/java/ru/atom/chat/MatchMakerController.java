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
import ru.atom.chat.socket.objects.gamesession.GameSession;
import ru.atom.chat.socket.services.repos.GameSessionRepo;

@Controller
@RequestMapping("matchmaker")
public class MatchMakerController {
    private static final Logger log = LoggerFactory.getLogger(MatchMakerController.class);

    private GameSessionRepo gameSessionRepo = GameSessionRepo.getInstance();

    @RequestMapping(
            path = "join",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> join(@RequestParam("name") String name) {
        System.out.println(gameSessionRepo);
        GameSession session = gameSessionRepo.pollFreeSession();
        session.addPlayer(name);
        gameSessionRepo.putSessionBack(session);

        log.warn(session.getId().toString());
        return new ResponseEntity<>(session.getId().toString(), HttpStatus.OK);
    }
}
