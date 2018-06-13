package ru.atom.game.threadpool.reflectionpool;

class ThreadWorker extends Thread {
    ThreadWorker(ThreadGroup group, Runnable task) {
        super(group, task);
    }
}
