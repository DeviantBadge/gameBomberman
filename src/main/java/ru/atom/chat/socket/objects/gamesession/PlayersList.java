package ru.atom.chat.socket.objects.gamesession;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.atom.chat.socket.enums.MessageType;
import ru.atom.chat.socket.message.response.OutgoingMessage;
import ru.atom.chat.socket.objects.base.interfaces.Replicable;
import ru.atom.chat.socket.objects.ingame.Pawn;
import ru.atom.chat.socket.util.JsonHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayersList {

    public static class PlayerPawn {
        private final Pawn pawn;
        private final WebSocketSession session;

        PlayerPawn(Pawn pawn, WebSocketSession session) {
            this.pawn = pawn;
            this.session = session;
        }

        public boolean contain(WebSocketSession session) {
            return this.session.equals(session);
        }

        public Pawn getPawn() {
            return pawn;
        }

        public WebSocketSession getSession() {
            return session;
        }
    }

    private List<PlayerPawn> players;
    private int maxAmount;

    public PlayersList(int maxPlayerAmount) {
        players = new CopyOnWriteArrayList<>();
        this.maxAmount = maxPlayerAmount;
    }

    int playerNum(WebSocketSession session) {
        int playerNum = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).contain(session)) {
                playerNum = i;
                break;
            }
        }
        return playerNum;
    }

    public PlayerPawn createNewPlayer(WebSocketSession player) {
        PlayerPawn playerPawn = null;
        if (players.size() < maxAmount) {
            playerPawn = new PlayerPawn(new Pawn(0, 0), player);
            players.add(playerPawn);
        }
        return playerPawn;
    }

    public Pawn getPlayerPawn(int playerNum) {
        return players.get(playerNum).getPawn();
    }

    public List<? extends Replicable> getPawns() {
        List<Replicable> pawns = new ArrayList<>();
        players.forEach(playerPawn -> pawns.add(playerPawn.getPawn()));
        return pawns;
    }

    public WebSocketSession getPlayerSocket(int playerNum) {
        return players.get(playerNum).getSession();
    }

    public void sendAll(ArrayList<Replicable> data) {
        OutgoingMessage message1 = new OutgoingMessage(MessageType.REPLICA, JsonHelper.toJson(data));
        players.forEach(playerPawn -> {
            try {
                playerPawn.getSession().sendMessage(new TextMessage(JsonHelper.toJson(message1)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
