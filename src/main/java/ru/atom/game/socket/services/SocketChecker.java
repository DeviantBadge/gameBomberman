package ru.atom.game.socket.services;

import ru.atom.game.databases.player.PlayerData;
import ru.atom.game.http.util.RegularChecks;
import ru.atom.game.socket.util.UriHelper;

import java.net.URI;
import java.util.Map;

class SocketChecker {
    static String connectionUriPreCheck(Map<String, String> uriParams) {
        StringBuilder result = new StringBuilder();
        String name = uriParams.get("name");
        String password = uriParams.get("password");
        String gameId = uriParams.get("gameId");

        if (name == null)
            result.append("Sent connection request without \"name\" parameter\n");

        if (password == null)
            result.append("Sent connection request without \"gameId\" parameter");

        if (gameId == null)
            result.append("Sent connection request without \"gameId\" parameter");

        if (!RegularChecks.isNameCorrect(name))
            result.append("Sent connection request with incorrect \"name\"\n");
        if(!RegularChecks.isPasswordCorrect(password))
            result.append("Sent connection request with incorrect \"password\"\n");

        return result.length() == 0 ? null : result.toString();
    }

    static String connectionUriPreCheck(URI connectionUri) {
        Map<String, String> uriParams = UriHelper.getParamsFromUri(connectionUri.getQuery());
        return connectionUriPreCheck(uriParams);
    }
}
