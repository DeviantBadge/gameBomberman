package ru.atom.chat.socket.objects.gamesession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.atom.chat.socket.enums.MessageType;
import ru.atom.chat.socket.message.response.OutgoingMessage;
import ru.atom.chat.socket.objects.base.interfaces.Replicable;
import ru.atom.chat.socket.objects.ingame.Pawn;
import ru.atom.chat.socket.util.JsonHelper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayersList {
    private static final Logger log = LoggerFactory.getLogger(PlayersList.class);

    public static class OnlinePlayer {
        private final String name;
        private WebSocketSession session;

        OnlinePlayer(String name, WebSocketSession session) {
            this.session = session;
            this.name = name;
        }

        OnlinePlayer(String name) {
            this(name, null);
        }

        public void setSession(WebSocketSession session) {
            if (this.session != null && session != null)
                log.warn("Smbdy want to connect to existing player");
            else
                this.session = session;
        }

        public WebSocketSession getSession() {
            return session;
        }

        public String getName() {
            return name;
        }
    }

    private List<OnlinePlayer> players;
    private int maxAmount;

    public PlayersList(int maxPlayerAmount) {
        players = new CopyOnWriteArrayList<>();
        this.maxAmount = maxPlayerAmount;
    }

    int playerNum(WebSocketSession session) {
        int playerNum = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getSession() == session) {
                playerNum = i;
                break;
            }
        }
        return playerNum;
    }

    public int playerNum(String name) {
        int playerNum = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals(name)) {
                playerNum = i;
                break;
            }
        }
        return playerNum;
    }

    public String getName(int num) {
        return players.get(num).getName();
    }

    public OnlinePlayer createNewPlayer(String name) {
        OnlinePlayer onlinePlayer = null;
        if (players.size() < maxAmount) {
            onlinePlayer = new OnlinePlayer(name);
            players.add(onlinePlayer);
        }
        return onlinePlayer;
    }

    public void connectPlayerWithSocket(int playerNum, WebSocketSession session) {
        players.get(playerNum).setSession(session);
    }

    public void sendAll(String data) {
        players.forEach(onlinePlayer -> {
            try {
                WebSocketSession socketSession = onlinePlayer.getSession();
                if (socketSession != null && socketSession.isOpen())
                    socketSession.sendMessage(new TextMessage(data));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // TODO why here synchronized dont help us?
    public void sendTo(int playerNum, String data) {
        try {
            WebSocketSession socketSession = players.get(playerNum).getSession();
            if (socketSession != null && socketSession.isOpen())
                socketSession.sendMessage(new TextMessage(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int size() {
        return players.size();
    }

    public void close(int playerNum) {
        synchronized (players.get(playerNum)) {
            players.get(playerNum).setSession(null);
        }
    }

    public WebSocketSession getSession(int playerNum) {
        return players.get(playerNum).getSession();
    }

    public void removePlayer(int playerNum) {
        players.remove(playerNum);
    }
}
