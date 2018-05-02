package ru.atom.chat.socket.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.atom.chat.socket.message.response.OutgoingMessage;
import ru.atom.chat.socket.objects.base.Position;
import ru.atom.chat.socket.objects.ingame.*;
import ru.atom.chat.socket.services.game.GameService;
import ru.atom.chat.socket.topics.MessageType;
import ru.atom.chat.socket.util.JsonHelper;
import ru.atom.chat.socket.util.SessionsList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SockEventHandler extends TextWebSocketHandler {
    static Pawn pawn = new Pawn(60,60);

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
        Map <String , Object> mes = JsonHelper.fromJson(json, Map.class);
        System.out.println(mes.get("data").toString());
        String  dir = ((Map<String , Object>)mes.get("data")).get("direction").toString();
        mes.forEach((key,obj)-> System.out.println(key + ":" + obj));

        Map<String, Object> object = new HashMap<>();
        Map<String, Object> position = new HashMap<>();
        List<Object> objects = new ArrayList<>();
        String st;
        Bomb bomb;

        switch(mes.get("topic").toString()) {
            case "MOVE":
                System.out.println(json);

                bomb = new Bomb(new Position(96, 0));
                objects.add(bomb);

                Wood wood = new Wood(new Position(0, 0));
                objects.add(wood);

                Wall wall = new Wall(new Position(32, 0));
                objects.add(wall);

                Fire fire = new Fire(new Position(64, 0));
                objects.add(fire);

                Bonus bonus = new Bonus(new Position(128, 0), Bonus.BonusType.BOMBS);
                objects.add(bonus);

                wall = new Wall(new Position(0, 32));
                objects.add(wall);
                wall = new Wall(new Position(32, 32));
                objects.add(wall);
                wall = new Wall(new Position(64, 32));
                objects.add(wall);
                wall = new Wall(new Position(96, 32));
                objects.add(wall);
                wall = new Wall(new Position(128, 32));
                objects.add(wall);
                wall = new Wall(new Position(160, 0));
                objects.add(wall);
                wall = new Wall(new Position(192, 0));
                objects.add(wall);
                wall = new Wall(new Position(224, 0));
                objects.add(wall);
                wall = new Wall(new Position(256, 0));
                objects.add(wall);
                wall = new Wall(new Position(288, 0));
                objects.add(wall);
                wall = new Wall(new Position(320, 0));
                objects.add(wall);
                wall = new Wall(new Position(352, 0));
                objects.add(wall);
                wall = new Wall(new Position(384, 0));
                objects.add(wall);
                wall = new Wall(new Position(416, 0));
                objects.add(wall);
                wall = new Wall(new Position(448, 32));
                objects.add(wall);
                wall = new Wall(new Position(480, 0));
                objects.add(wall);
                wall = new Wall(new Position(512, 0));
                objects.add(wall);
                wall = new Wall(new Position(544, 0));
                objects.add(wall);
                wall = new Wall(new Position(576, 0));
                objects.add(wall);
                wall = new Wall(new Position(608, 0));
                objects.add(wall);
                wall = new Wall(new Position(640, 0));
                objects.add(wall);

                pawn.setDirection(Pawn.Direction.valueOf(dir));
                objects.add(pawn);

                st = JsonHelper.toJson(objects);
                break;

            case "JUMP":
                System.out.println(json);

                bomb = new Bomb(new Position(200, 60));
                objects.add(bomb);

                st = JsonHelper.toJson(objects);
                break;

            default:
                st = "{}";

        }

        try {
            OutgoingMessage message1 = new OutgoingMessage(MessageType.REPLICA, st);
            session.sendMessage(new TextMessage(JsonHelper.toJson(message1)));
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
