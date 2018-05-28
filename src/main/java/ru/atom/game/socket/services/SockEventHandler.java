package ru.atom.game.socket.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.atom.game.socket.message.request.IncomingMessage;
import ru.atom.game.gamesession.session.GameSession;
import ru.atom.game.gamesession.session.OnlineSession;
import ru.atom.game.repos.GameSessionRepo;
import ru.atom.game.socket.util.JsonHelper;
import ru.atom.game.socket.util.SessionsList;

@Service
public class SockEventHandler extends TextWebSocketHandler {

    private static Logger log = LoggerFactory.getLogger(SockEventHandler.class);

    @Autowired
    private GameSessionRepo sessions;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        SessionsList.addSession(session);
        log.info("Socket Connected: " + session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        IncomingMessage mes = JsonHelper.fromJson(message.getPayload(), IncomingMessage.class);
        GameSession gameSession = sessions.getSession(mes.getGameId());
        if (gameSession != null)
            gameSession.addOrder(mes, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("Socket Closed: [" + closeStatus.getCode() + "] " + closeStatus.getReason() + "; socket id [" + session.getId() + "]");
        OnlineSession onlineSession = SessionsList.getOnlineSession(session);
        if(onlineSession != null)
            onlineSession.onPlayerDisconnect(session);
        SessionsList.deleteSession(session);
        super.afterConnectionClosed(session, closeStatus);
    }
}
