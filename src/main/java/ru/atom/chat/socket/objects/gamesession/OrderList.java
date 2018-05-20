package ru.atom.chat.socket.objects.gamesession;

import ru.atom.chat.socket.objects.orders.Order;

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
        System.out.println(orders.size() + " before");
        if(!orders.add(order))
            System.out.println("CHO ZA HUYNYA");
        System.out.println(orders.size() + " after");
    }

    public int size() {
        return orders.size();
    }
}
