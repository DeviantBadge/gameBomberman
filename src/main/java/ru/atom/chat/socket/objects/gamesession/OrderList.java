package ru.atom.chat.socket.objects.gamesession;

import ru.atom.chat.socket.objects.orders.Order;

import java.util.concurrent.ConcurrentLinkedQueue;

public class OrderList {
    private ConcurrentLinkedQueue<Order> orders;

    OrderList() {
        orders = new ConcurrentLinkedQueue<>();
    }

    public Order getOrder() {
        return orders.poll();
    }

    public void add(Order order) {
        orders.add(order);
    }

    public int size() {
        return orders.size();
    }
}
