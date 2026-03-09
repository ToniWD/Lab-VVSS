package drinkshop.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {

    private int id;
    private List<OrderItem> items;
    private double totalPrice;
    private LocalDateTime orderDateTime;

    public Order(int id) {
        this.id = id;
        this.items = new ArrayList<>();
        this.totalPrice = 0.0;
        this.orderDateTime = LocalDateTime.now();
    }

    public Order(int id, List<OrderItem> items, double totalPrice) {
        this.id = id;
        this.items = new ArrayList<>(items);
        this.totalPrice = totalPrice;
        this.orderDateTime = LocalDateTime.now();
    }

    public Order(int id, List<OrderItem> items, double totalPrice, LocalDateTime orderDateTime) {
        this.id = id;
        this.items = new ArrayList<>(items);
        this.totalPrice = totalPrice;
        this.orderDateTime = orderDateTime;
    }

    public int getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    public void removeItem(OrderItem item) {
        this.items.remove(item);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", items=" + items +
                ", totalPrice=" + totalPrice +
                ", orderDateTime=" + orderDateTime +
                '}';
    }

    public double getTotal() {
        return totalPrice;
    }

    public void computeTotalPrice() {
        this.totalPrice=items.stream().mapToDouble(OrderItem::getTotal).sum();
    }
}