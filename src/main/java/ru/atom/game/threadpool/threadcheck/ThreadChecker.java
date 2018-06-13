package ru.atom.game.threadpool.threadcheck;

import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ru.atom.game.gamesession.session.Ticker;
import ru.atom.game.threadpool.reflectionpool.TickerExecutor;

import java.util.ArrayList;

// Compare common and new Pool impl
public class ThreadChecker {
    private static TickerExecutor executor = new TickerExecutor(16);
    private static int lol;


    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(2000);
        executeMineExecutor(500);
        Thread.sleep(5000);
        executor.stop();
        System.out.println(lol);

        System.out.println("_-----------------------------------------");
        System.out.println("_-----------------------------------------");
        System.out.println("_-----------------------------------------");
        System.out.println("_-----------------------------------------");

        Thread.sleep(2000);
        executeSimpleExecutor(500);
        Thread.sleep(5000);
        System.out.println(lol);
    }

    private static void executePoolTaskExecutor(int amount) {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(8);
        pool.initialize();
        long time = System.nanoTime();

        lol = 0;
        for (int i = 0; i < amount; i++) {
            pool.execute(getRunnable(60, 1, i));
        }


    }

    private static void executeMineExecutor(int amount) {
        executor.initialize();

        lol = 0;
        for (int i = 0; i < amount; i++) {
            executor.execute(getRunnable(60, 2, i));
        }
    }

    private static void executeSimpleExecutor(int amount) {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();

        lol = 0;
        for (int i = 0; i < amount; i++) {
            executor.execute(getRunnable(60, 3, i));
        }
    }

    private static Ticker getRunnable(int FPS, int systemGroup, int taskNumber) {
        return
                new Ticker(FPS) {
                    int taskNum = taskNumber;
                    long fps = FPS;
                    long startTime = -1;
                    long processDuration = 2_000;
                    long lagAmount = 0;
                    long actTime = -1;

                    @Override
                    protected void onStop() {
                        lol += lagAmount;
                        // System.out.println(lagAmount);
                        /*
                        System.out.println(getTickNumber() + " ticks; "
                                + fps + " fps; " + (System.currentTimeMillis() - startTime) + " time when stop processed; "
                                + actTime + " time when stop asked; " + "taskNumber: " + taskNum + "; " + lagAmount + " lag amount; "
                        );
                         */
                    }

                    @Override
                    protected void act(long timeFromPastLoop) {
                        if (startTime < 0) {
                            startTime = System.currentTimeMillis();
                        }
                        long ttt = System.currentTimeMillis() - startTime;
                        long amount = ((long) ((ttt - actTime) / (1000.0 / fps)));

                        double a = 2;
                        for(int i = 0; i < 100; i ++) {
                            a = Math.pow(a, 2);
                        }

                        if (amount > 1) {
                            lagAmount += amount - 1;
                        }
                        actTime = ttt;
                        if (actTime > processDuration) {
                            stop();
                        }
                    }
                };
    }
}
