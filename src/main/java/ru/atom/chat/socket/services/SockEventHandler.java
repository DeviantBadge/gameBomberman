package ru.atom.chat.socket.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.atom.chat.socket.services.game.GameService;
import ru.atom.chat.socket.util.SessionsList;

@Service
public class SockEventHandler extends TextWebSocketHandler {
    private Logger log = LoggerFactory.getLogger(SockEventHandler.class);

    @Autowired
    private GameService gameService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        SessionsList.addSession(session);
        String gameId = "";
        int inGameNum = gameService.findGameById(gameId).playerReady(session);
        // session.send(new TextMessage(String.valueOf(inGameNum)));
        log.info("Socket Connected: " + session);
    }

    public void afterConnectionEstablished(WebSocketSession session, @RequestParam("gameId") String id, @RequestParam("name") String name) throws Exception {
        super.afterConnectionEstablished(session);
        SessionsList.addSession(session);
        log.info("Socket Connected: " + session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String json = message.getPayload();

        try {
        } catch (IllegalArgumentException exception) {
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("Socket Closed: [" + closeStatus.getCode() + "] " + closeStatus.getReason());
        SessionsList.deleteSession(session);
        super.afterConnectionClosed(session, closeStatus);
    }
}
