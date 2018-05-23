package ru.atom.game.gamesession.session;


import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public abstract class Ticker implements Runnable {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Ticker.class);
    private static final int FPS = 60;
    private static final long FRAME_TIME = 1000 / FPS;

    private long pastLoopStarted;
    private long tickNumber = 0;
    private boolean stopped = false;

    public Ticker() {
        pastLoopStarted = System.currentTimeMillis();
    }

    @Override
    final public void run() {
        while (!Thread.currentThread().isInterrupted() && !isStopped()) {
            long started = System.currentTimeMillis();
            long delta = started - pastLoopStarted;
            // System.out.println(delta);
            pastLoopStarted = started;
            act(delta);
            long elapsed = System.currentTimeMillis() - started;
            if (elapsed < FRAME_TIME) {
                // log.info("All tick finish at {} ms", elapsed);
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(FRAME_TIME - elapsed));
            } else {
                log.warn("tick lag {} ms", elapsed - FRAME_TIME);
            }
            // log.info("{}: tick ", tickNumber);
            tickNumber++;
        }
        onStop();
    }

    protected abstract void onStop();

    public boolean isStopped() {
        return stopped;
    }

    protected void stop() {
        stopped = true;
    }

    protected abstract void act(long timeFromPastLoop);

    public long getTickNumber() {
        return tickNumber;
    }
}
