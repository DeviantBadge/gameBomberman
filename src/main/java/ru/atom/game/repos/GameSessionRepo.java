package ru.atom.game.repos;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Repository;
import ru.atom.game.gamesession.session.GameSession;
import ru.atom.game.gamesession.properties.GameSessionProperties;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Repository
public class GameSessionRepo {
    private final ConcurrentHashMap<String, GameSession> allSessions;
    private final ConcurrentLinkedQueue<GameSession> notReadySessions;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Autowired
    private BeanFactory beans;

    protected GameSessionRepo() {
        allSessions = new ConcurrentHashMap<>();
        notReadySessions = new ConcurrentLinkedQueue<>();
    }

    public GameSession getSession(String id) {
        return allSessions.get(id);
    }

    protected GameSession createSession(GameSessionProperties properties) {
        GameSession session = beans.getBean(GameSession.class, properties);

        allSessions.put(session.getId().toString(), session);
        executor.execute(session);
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
            /* smth */
        } else {
            notReadySessions.add(session);
        }
    }

    @Override
    public String toString() {
        return allSessions.size() + "   " + notReadySessions.size() + "   ";
    }

    public void endGame(GameSession session) {
        allSessions.remove(session.getId().toString());
    }
}
