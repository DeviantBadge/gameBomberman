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
import ru.atom.game.databases.player.PlayerData;
import ru.atom.game.databases.player.PlayerDataRepository;
import ru.atom.game.http.message.Credentials;
import ru.atom.game.http.util.ResponseFactory;
import ru.atom.game.socket.util.JsonHelper;

@Controller
@RequestMapping("registry")
public class Register {
    private static final Logger log = LoggerFactory.getLogger(Register.class);

    @Autowired
    private PlayerDataRepository playerRepo;

    @RequestMapping(
            path = "signIn",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signIn(@RequestBody String userData) {
        Credentials credentials = JsonHelper.fromJson(userData, Credentials.class);
        ResponseEntity<String> response;
        PlayerData playerData;

        response = RegisterChecker.checkCredentialsSignIn(credentials);
        if (response != null)
            return response;

        playerData = playerRepo.findByName(credentials.getName());
        response = RegisterChecker.checkSignInPayerData(credentials, playerData);
        if (response != null)
            return response;

        if (playerRepo.savePlayer(playerData))
            return ResponseFactory.generateOkResponse("You are signed in", HttpStatus.OK);
        else
            return ResponseFactory.generateErrorResponse("Failed to log in", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(
            path = "register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@RequestBody String userData) {
        Credentials credentials = JsonHelper.fromJson(userData, Credentials.class);
        ResponseEntity<String> response;
        PlayerData playerData;

        response = RegisterChecker.checkCredentialsRegisterIn(credentials);
        if (response != null)
            return response;

        playerData = playerRepo.findByName(credentials.getName());
        response = RegisterChecker.checkRegisterPayerData(credentials, playerData);
        if (response != null)
            return response;

        playerData = new PlayerData(credentials.getName(), credentials.getPassword());
        response = addNewPlayerToDB(playerData);
        if (response != null)
            return response;
        return ResponseFactory.generateOkResponse("You are registred", HttpStatus.OK);
    }

    private ResponseEntity<String> addNewPlayerToDB(PlayerData playerData) {
        try {
            playerRepo.save(playerData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.generateErrorResponse(
                    "Failed to create new player",
                    "Try again later.",
                    HttpStatus.CONFLICT
            );
        }
        return null;
    }
}
