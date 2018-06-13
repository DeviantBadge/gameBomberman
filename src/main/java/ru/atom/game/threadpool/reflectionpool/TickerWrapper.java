package ru.atom.game.threadpool.reflectionpool;

import ru.atom.game.gamesession.session.Ticker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class TickerWrapper {
    private static Method onStop;

    static {
        try {
            onStop = Ticker.class.getDeclaredMethod("onStop");
            onStop.setAccessible(true);
        } catch (NoSuchMethodException e) {
            onStop = null;
            e.printStackTrace();
        }
    }

    private Ticker ticker;
    private long prevFrameStart;

    TickerWrapper(Ticker ticker) {
        this.ticker = ticker;
        prevFrameStart = ticker.getPastLoopStarted();
    }


    boolean isReady(long currentTime) {
        return currentTime - prevFrameStart >= ticker.getFRAME_TIME();
    }

    Long getDeltaMS(long currentTime) {
        return ticker.getFRAME_TIME() * (long) ((currentTime - prevFrameStart) / (double) ticker.getFRAME_TIME());
    }

    Ticker getTicker() {
        return ticker;
    }

    void setUpCurrentFrameTime(long currentTime) {
        prevFrameStart = prevFrameStart + ticker.getFRAME_TIME() * (long) ((currentTime - prevFrameStart) / (double) ticker.getFRAME_TIME());
    }

    public boolean isFinished() {
        return ticker.isStopped();
    }

    void onStop() {
        try {
            onStop.invoke(ticker);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
