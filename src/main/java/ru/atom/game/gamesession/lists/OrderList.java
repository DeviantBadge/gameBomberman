package ru.atom.game.gamesession.lists;

import ru.atom.game.objects.orders.Order;

import java.util.concurrent.ConcurrentLinkedQueue;

public class OrderList {
    private final ConcurrentLinkedQueue<Order> orders;

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
