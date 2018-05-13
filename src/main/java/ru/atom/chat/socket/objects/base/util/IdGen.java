package ru.atom.chat.socket.objects.base.util;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class IdGen {
    private final ConcurrentLinkedQueue<Integer> deletedId = new ConcurrentLinkedQueue<Integer>();
    private AtomicInteger lastId = new AtomicInteger(0);

    public Integer generateId() {
        Integer id = deletedId.poll();
        if (id != null)
            return id;
        return lastId.getAndIncrement();
    }

    public void addDeletedId(Integer id) {
        if(!deletedId.contains(id))
            deletedId.add(id);
    }
}
