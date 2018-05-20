package ru.atom.chat.socket.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.atom.chat.socket.message.request.IncomingMessage;
import ru.atom.chat.socket.objects.gamesession.GameSession;
import ru.atom.chat.socket.objects.ingame.*;
import ru.atom.chat.socket.services.repos.GameSessionRepo;
import ru.atom.chat.socket.util.JsonHelper;
import ru.atom.chat.socket.util.SessionsList;

import java.io.IOException;

@Service
public class SockEventHandler extends TextWebSocketHandler {
    private static Pawn pawn = new Pawn(32, 32);

    private static Logger log = LoggerFactory.getLogger(SockEventHandler.class);

    private GameSessionRepo sessions = GameSessionRepo.getInstance();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        SessionsList.addSession(session);
        // int inGameNum = gameService.findGameById(gameId).playerReady(session);
        // session.send(new TextMessage(String.valueOf(inGameNum)));
        log.info("Socket Connected: " + session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // log.error(message.getPayload());
        IncomingMessage mes = JsonHelper.fromJson(message.getPayload(), IncomingMessage.class);
        GameSession gameSession = sessions.getSession(mes.getGameId());
        if (gameSession != null)
            gameSession.addOrder(mes, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("Socket Closed: [" + closeStatus.getCode() + "] " + closeStatus.getReason());
        SessionsList.deleteSession(session);
        super.afterConnectionClosed(session, closeStatus);
    }
}
