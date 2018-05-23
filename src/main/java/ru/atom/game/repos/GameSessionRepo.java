package ru.atom.game.repos;

import ru.atom.game.gamesession.GameSession;
import ru.atom.game.gamesession.properties.GameSessionProperties;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameSessionRepo {
    private final ConcurrentHashMap<String, Thread> threadList;
    private final ConcurrentHashMap<String, GameSession> allSessions;
    private final ConcurrentLinkedQueue<GameSession> notReadySessions;

    private static GameSessionRepo instance;

    static {
        instance = new GameSessionRepo();
    }

    public static GameSessionRepo getInstance() {
        return instance;
    }

    private GameSessionRepo() {
        allSessions = new ConcurrentHashMap<>();
        notReadySessions = new ConcurrentLinkedQueue<>();
        threadList = new ConcurrentHashMap<>();
    }

    public GameSession getSession(String id) {
        return allSessions.get(id);
    }

    protected GameSession createSession(GameSessionProperties properties) {
        GameSession session = new GameSession(properties);
        Thread game = new Thread(session);

        allSessions.put(session.getId().toString(), session);
        threadList.put(session.getId().toString(), game);
        game.start();
        return session;
    }

    public GameSession pollOrCreateSession(GameSessionProperties properties) {
        GameSession session = notReadySessions.poll();
        if(session==null) {
            return createSession(properties);
        }
        return session;
    }

    public void putSessionBack(GameSession session) {
        if (session.isFull()) {
            // smth
        } else {
            notReadySessions.add(session);
        }
    }

    @Override
    public String toString() {
        return threadList.size() + "   " + allSessions.size() + "   " + notReadySessions.size() + "   ";
    }

    public void endGame(GameSession session) {
        threadList.remove(session.getId().toString());
        allSessions.remove(session.getId().toString());
    }
}
