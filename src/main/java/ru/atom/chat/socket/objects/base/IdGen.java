package ru.atom.chat.socket.objects.base;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

class IdGen {
    private final ConcurrentLinkedQueue<Integer> deletedId = new ConcurrentLinkedQueue<Integer>();
    private AtomicInteger lastId = new AtomicInteger(0);

    Integer generateId() {
        Integer id = deletedId.poll();
        if (id != null)
            return id;
        return lastId.getAndIncrement();
    }

    void addDeletedId(Integer id) {
        deletedId.add(id);
    }
}
