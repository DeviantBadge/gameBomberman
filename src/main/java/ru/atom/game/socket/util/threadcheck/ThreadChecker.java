package ru.atom.game.socket.util.threadcheck;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class ThreadChecker {

    public static void main(String[] args) {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        int cores = Runtime.getRuntime().availableProcessors();
        pool.setCorePoolSize(4);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        pool.initialize();

        System.out.println(pool.getCorePoolSize());
        System.out.println(pool.getMaxPoolSize());

        pool.execute(getRunnable("Task 1"));
        pool.execute(getRunnable("Task 2"));
        pool.execute(getRunnable("Task 3"));
        pool.execute(getRunnable("Task 4"));
        pool.execute(getRunnable("Task 5"));
        pool.execute(getRunnable("Task 6"));
        pool.execute(getRunnable("Task 11"));
        pool.execute(getRunnable("Task 21"));
        pool.execute(getRunnable("Task 31"));
        pool.execute(getRunnable("Task 41"));
        pool.execute(getRunnable("Task 51"));
        pool.execute(getRunnable("Task 61"));
    }

    private static Runnable getRunnable(String str) {
        return () -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " " + str);
        };
    }
}
