package ru.atom.game.http.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.atom.game.databases.player.PlayerDataRepository;
import ru.atom.game.http.matchmaker.MatchMaker;
import ru.atom.game.socket.util.JsonHelper;

import java.util.Map;

@Controller
@RequestMapping("registry")
public class Register {
    private static final Logger log = LoggerFactory.getLogger(Register.class);

    @Autowired
    private MatchMaker matchMaker;

    @Autowired
    private PlayerDataRepository playerDataRepository;

    @RequestMapping(
            path = "signIn",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signIn(@RequestBody String userData) {
        System.out.println(JsonHelper.fromJson(userData, Map.class) + " si");
        return new ResponseEntity<>("0", HttpStatus.OK);
    }

    @RequestMapping(
            path = "register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@RequestBody String userData) {
        System.out.println(JsonHelper.fromJson(userData, Map.class) + " rg");
        ResponseEntity<String> response = new ResponseEntity<>(userData, HttpStatus.OK);
        return response;
    }

    private ResponseEntity<String> auth(String name) {
        /*
        List<PlayerData> player = playerDataRepository.findByName(name);
        if(player.size() == 0) {
            return new ResponseEntity<>("Cant find this user", HttpStatus.NOT_FOUND);
        }
        */
        return null;
    }
}
