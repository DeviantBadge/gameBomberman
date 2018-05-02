package ru.atom.chat.socket.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.atom.chat.socket.message.response.OneMoreMessageLvl;
import ru.atom.chat.socket.message.response.OutgoingMessage;
import ru.atom.chat.socket.services.game.GameService;
import ru.atom.chat.socket.topics.MessageType;
import ru.atom.chat.socket.util.JsonHelper;
import ru.atom.chat.socket.util.SessionsList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class SockEventHandler extends TextWebSocketHandler {
    private static Logger log = LoggerFactory.getLogger(SockEventHandler.class);
    private static int amount = 0;
    private static long time;

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

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String json = message.getPayload();
        System.out.println(json);
        Map<String, Object> object = new HashMap<>();
        object.put("type", "Bomb");
        object.put("x", 30);
        object.put("y", 60);
        String st = JsonHelper.toJson(object);
        OutgoingMessage message1 = new OutgoingMessage(MessageType.REPLICA, st);
        OneMoreMessageLvl messageLvl = new OneMoreMessageLvl(JsonHelper.toJson(message1));
        try {
            session.sendMessage(new TextMessage(JsonHelper.toJson(messageLvl)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("Socket Closed: [" + closeStatus.getCode() + "] " + closeStatus.getReason());
        SessionsList.deleteSession(session);
        super.afterConnectionClosed(session, closeStatus);
    }
}
