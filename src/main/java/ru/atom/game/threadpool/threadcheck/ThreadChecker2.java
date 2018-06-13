package ru.atom.game.threadpool.threadcheck;

import ru.atom.game.gamesession.session.Ticker;

import javax.el.MethodNotFoundException;
import java.lang.reflect.Method;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

// Compare common and new Pool impl
public class ThreadChecker2 {

    public static void main(String[] args) {
        LinkedBlockingQueue<Integer> integers = new LinkedBlockingQueue<>();
        final long time = System.currentTimeMillis();
        Thread thread1 = new Thread(() -> {
            try {
                Thread.currentThread().sleep(1000);
                integers.poll(10, TimeUnit.SECONDS);
                System.out.println(System.currentTimeMillis() - time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                Thread.currentThread().sleep(5500);
                integers.put(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        thread2.start();


        Method act;
        try {
            act = Ticker.class.getMethod("act", long.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new MethodNotFoundException("Ticker doesnt have method act!");
        }
        System.out.println(act);
    }
}
