package ru.atom.game.threadpool.reflectionpool;

import ru.atom.game.gamesession.session.Ticker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TickerExecutor {
    private final ConcurrentLinkedQueue<TickerWrapper> tasks;
    private final List<ThreadWorker> threadWorkers;
    private final ThreadGroup workerGroup;
    private final int workerAmount;

    public void stop() {
        workerGroup.interrupt();
    }

    public TickerExecutor(int workerAmount) {
        this.workerAmount = workerAmount;
        tasks = new ConcurrentLinkedQueue<>();
        threadWorkers = new ArrayList<>(workerAmount);
        workerGroup = new ThreadGroup("Workers");

        ThreadWorker worker;
        for (int i = 0; i < workerAmount; i++) {
            worker = new ThreadWorker(workerGroup, new WorkerTask(tasks));
            threadWorkers.add(worker);
        }
    }

    public void initialize() {
        for(ThreadWorker worker : threadWorkers) {
            worker.start();
        }
    }

    public void execute(Ticker task) {
        tasks.add(new TickerWrapper(task));
    }
}
