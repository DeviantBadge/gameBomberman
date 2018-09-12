package ru.atom.game.socket.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.atom.game.databases.player.PlayerData;
import ru.atom.game.databases.player.PlayerDataRepository;
import ru.atom.game.gamesession.lists.OnlinePlayer;
import ru.atom.game.repos.ConnectionPool;
import ru.atom.game.socket.message.request.IncomingMessage;
import ru.atom.game.gamesession.session.GameSession;
import ru.atom.game.repos.GameSessionRepo;
import ru.atom.game.socket.util.JsonHelper;
import ru.atom.game.socket.util.UriHelper;

import java.util.Map;

@Service
public class SockEventHandler extends TextWebSocketHandler {

    private static Logger log = LoggerFactory.getLogger(SockEventHandler.class);

    @Autowired
    private GameSessionRepo sessions;

    @Autowired
    private PlayerDataRepository playerRepo;

    @Autowired
    private ConnectionPool connections;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        String response;
        Map<String, String> uriParams = UriHelper.getParamsFromUri(session.getUri().getQuery());

        response = SocketChecker.connectionUriPreCheck(uriParams);
        if (response != null) {
            // todo send response to user and handle it
            log.error("Error while connecting player, for more information watch logs");
            log.error(response);
            session.close();
            return;
        }
        OnlinePlayer player = loginPlayer(session, uriParams);
        if (player == null) {
            log.error("Could not log in player");
            session.close();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        IncomingMessage mes = JsonHelper.fromJson(message.getPayload(), IncomingMessage.class);
        OnlinePlayer player = connections.getPlayer(session);
        GameSession gameSession = sessions.getSession(mes.getGameId());
        System.out.println(mes.getGameId());
        System.out.println(message.getPayload());

        if (!confirmPrivacy(player, mes, gameSession))
            return;
        gameSession.addOrder(player, mes);
    }

    private boolean confirmPrivacy(OnlinePlayer player, IncomingMessage mes, GameSession gameSession) {
        if (gameSession == null) {
            log.warn("Trying to send message to session with unknown id");
            return false;
        }

        if (player.getGame() != gameSession || !gameSession.contains(player)) {
            log.warn("Trying to send message to session where player doesnt logged in");
            return false;
        }

        return true;
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("Socket Closed: [" + closeStatus.getCode() + "] " + closeStatus.getReason() + "; socket id [" + session.getId() + "]");
        OnlinePlayer player = connections.getPlayer(session);
        if (player != null) {
            player.getPlayerData().setPlaying(false);
            playerRepo.savePlayer(player.getPlayerData());
            connections.disconnectFromGame(player, player.getGame());
        }
        super.afterConnectionClosed(session, closeStatus);
    }


    // todo слишком долго из базы берет данные (решение - проверять все тупо в другом процессе и затем прислать результат обратно)
    // клиент в это время останавливает загрузку игры и возобновляет только после получения сигнала
    private OnlinePlayer loginPlayer(WebSocketSession session, Map<String, String> uriParams) {
        String name = uriParams.get("name");
        String password = uriParams.get("password");
        String gameId = uriParams.get("gameId");
        // todo make auth code

        OnlinePlayer player = connections.playerConnected(name, session);
        if (player == null) {
            log.error("Unknown player name");
            return null;
        }

        GameSession gameSession = sessions.getSession(gameId);
        System.out.println(gameSession);
        if (connections.connectToGame(player, gameSession)) {
                return player;
        }
        return null;
    }
}
