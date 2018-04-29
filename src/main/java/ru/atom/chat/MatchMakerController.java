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
import ru.atom.chat.socket.services.game.GameService;

@Controller
@RequestMapping("chat")
public class MatchMakerController {
    private static final Logger log = LoggerFactory.getLogger(MatchMakerController.class);

    @Autowired
    private GameService gameService;

    /**
     * curl -X GET -i localhost:8080/chat/chat
     */
    @RequestMapping(
            path = "chat",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> chat() {
        String messageList = gameService.allMessages();
        log.info(messageList);

        return new ResponseEntity<>(messageList, HttpStatus.OK);
    }

    /**
     * curl -i localhost:8080/chat/users
     */
    @RequestMapping(
            path = "users",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> users() {
        String userList = gameService.allUsers();
        log.info(userList);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }
}
