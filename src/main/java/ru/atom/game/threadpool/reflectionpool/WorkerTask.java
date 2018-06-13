package ru.atom.game.threadpool.reflectionpool;


import ru.atom.game.gamesession.session.Ticker;

import javax.el.MethodNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentLinkedQueue;

// TODO how can we act in Ticker? (i will leave it here, just for science (and fun:P))
// it works bad for small amount of tickers, but very good for a lot of tickers.
// 1 - I will call act() by reflection
class WorkerTask implements Runnable {
    static Method tickerAct;
    ConcurrentLinkedQueue<TickerWrapper> tasks;

    static {
        try {
            tickerAct = Ticker.class.getDeclaredMethod("act", long.class);
            tickerAct.setAccessible(true);
        } catch (NoSuchMethodException e) {
            tickerAct = null;
            e.printStackTrace();
            throw new MethodNotFoundException("Ticker doesnt have method act!");
        }
    }

    WorkerTask(ConcurrentLinkedQueue<TickerWrapper> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void run() {
        TickerWrapper task;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                task = tasks.poll();
                if (task == null)
                    continue;
                long currentTime = System.currentTimeMillis();
                if (task.isReady(currentTime)) {
                    tickerAct.invoke(task.getTicker(), task.getDeltaMS(currentTime));
                    if (task.isFinished()) {
                        task.onStop();
                        continue;
                    } else {
                        tasks.add(task);
                    }
                    task.setUpCurrentFrameTime(currentTime);
                    task.getTicker().incTickAmount();
                } else {
                    tasks.add(task);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
