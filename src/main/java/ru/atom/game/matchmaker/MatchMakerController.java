package ru.atom.game.matchmaker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.atom.game.gamesession.properties.GameSessionProperties;
import ru.atom.game.gamesession.session.GameSession;
import ru.atom.game.gamesession.properties.GameSessionPropertiesCreator;
import ru.atom.game.repos.GameSessionRepo;

@Controller
@RequestMapping("matchmaker")
public class MatchMakerController {
    private static final Logger log = LoggerFactory.getLogger(MatchMakerController.class);

    @Autowired
    private BeanFactory beans;

    @Autowired
    private GameSessionRepo gameSessionRepo;

    @RequestMapping(
            path = "join",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> join(@RequestParam("name") String name) {
        GameSessionPropertiesCreator creator = beans.getBean(GameSessionPropertiesCreator.class)
                .setMaxPlayerAmount(3)
                .setBlowStopsOnWall(false)
                .setSpeedOnStart(3)
                .setProbabilities(1,1,1);
        GameSession session = gameSessionRepo.pollOrCreateSession(creator.createProperties());
        session.addPlayer(name);
        gameSessionRepo.putSessionBack(session);

        log.warn(session.getId().toString());
        return new ResponseEntity<>(session.getId().toString(), HttpStatus.OK);
    }
}
