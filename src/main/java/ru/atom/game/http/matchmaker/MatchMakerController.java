package ru.atom.game.http.matchmaker;

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
import ru.atom.game.databases.player.PlayerData;
import ru.atom.game.databases.player.PlayerDataRepository;
import ru.atom.game.http.message.Credentials;
import ru.atom.game.http.util.ResponseFactory;
import ru.atom.game.socket.util.JsonHelper;

import java.util.Map;

@Controller
@RequestMapping("matchmaker")
public class MatchMakerController {
    private static final Logger log = LoggerFactory.getLogger(MatchMakerController.class);

    @Autowired
    private MatchMaker matchMaker;

    @Autowired
    private PlayerDataRepository playerRepo;

    @RequestMapping(
            path = "casual",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> casual(@RequestBody String userData) {
        ResponseEntity<String> response;
        Map<String, Object> data;
        Credentials credentials;
        PlayerData playerData;

        data = JsonHelper.fromJson(userData, Map.class);
        credentials = JsonHelper.fromJson(JsonHelper.toJson(data.get("credentials")), Credentials.class);


        response = MatchMakerChecker.checkUserData(data, credentials);
        if (response != null)
            return response;

        playerData = playerRepo.findByName(credentials.getName());
        response = MatchMakerChecker.checkSignInPayerData(credentials, playerData);
        if (response != null)
            return response;

        Integer id = matchMaker.getCommonSessionID(playerData, data);
        return ResponseFactory.generateOkResponse(id.toString(), HttpStatus.OK);
    }

    @RequestMapping(
            path = "ranked",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> rating(@RequestBody String userData) {
        System.out.println("hahahahaha");
        ResponseEntity<String> response;
        Map<String, Object> data;
        Credentials credentials;
        PlayerData playerData;

        data = JsonHelper.fromJson(userData, Map.class);
        credentials = JsonHelper.fromJson(JsonHelper.toJson(data.get("credentials")), Credentials.class);
        System.out.println("hahahahaha");


        response = MatchMakerChecker.checkUserData(data, credentials);
        if (response != null)
            return response;

        playerData = playerRepo.findByName(credentials.getName());
        System.out.println(playerData);
        response = MatchMakerChecker.checkSignInPayerData(credentials, playerData);
        if (response != null)
            return response;

        Integer id = matchMaker.getRatingSessionID(playerData, data);
        System.out.println(id);
        return ResponseFactory.generateOkResponse(id.toString(), HttpStatus.OK);
    }

    private ResponseEntity<String> auth(String name) {
        return null;
    }
}
