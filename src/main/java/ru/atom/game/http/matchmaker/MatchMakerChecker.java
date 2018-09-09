package ru.atom.game.http.matchmaker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.atom.game.databases.player.PlayerData;
import ru.atom.game.http.message.Credentials;
import ru.atom.game.http.util.RegularChecks;
import ru.atom.game.http.util.ResponseFactory;

import java.util.Map;

class MatchMakerChecker {
    static ResponseEntity<String> checkUserData(Map<String, Object> data, Credentials credentials) {
        if (credentials == null || credentials.getName() == null || credentials.getPassword() == null
                || !RegularChecks.isNameCorrect(credentials.getName())
                || !RegularChecks.isPasswordCorrect(credentials.getPassword()))
            return ResponseFactory.generateErrorResponse(
                    "Wrong request data.",
                    "Reload pege and try again",
                    HttpStatus.BAD_REQUEST
            );
        return null;
    }

    static ResponseEntity<String> checkSignInPayerData(Credentials credentials, PlayerData playerData) {
        if (playerData == null)
            return ResponseFactory.generateErrorResponse(
                    "We dont have any player with such name.",
                    "Create player with this name.",
                    HttpStatus.BAD_REQUEST
            );
        if (!credentials.getPassword().equals(playerData.getPassword()))
            return ResponseFactory.generateErrorResponse(
                    "Wrong password.",
                    "Remember your password and try again.",
                    HttpStatus.BAD_REQUEST
            );
        if (playerData.getPlaying())
            return ResponseFactory.generateErrorResponse(
                    "Player with such name already playing.",
                    "Login and then again later.",
                    HttpStatus.CONFLICT
            );

        return null;
    }
}
