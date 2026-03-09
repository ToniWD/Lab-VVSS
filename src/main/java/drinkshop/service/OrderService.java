package drinkshop.service;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.repository.Repository;
import drinkshop.service.validator.Validator;

import java.time.LocalDateTime;
import java.util.List;

public class OrderService {

    private final Repository<Integer, Order> orderRepo;
    private final Repository<Integer, Product> productRepo;
    private final Validator<Order> orderValidator;
    private final Validator<OrderItem> orderItemValidator;

    public OrderService(Repository<Integer, Order> orderRepo, Repository<Integer,
                                Product> productRepo, Validator<Order> orderValidator,
                        Validator<OrderItem> orderItemValidator) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.orderValidator = orderValidator;
        this.orderItemValidator = orderItemValidator;

    }

    public void addOrder(Order o) {
        orderValidator.validate(o);
        o.setOrderDateTime(LocalDateTime.now());
        orderRepo.save(o);
    }

    public void updateOrder(Order o) {
        orderValidator.validate(o);
        orderRepo.update(o);
    }

    public void deleteOrder(int id) {
        orderRepo.delete(id);
    }

    public List<Order> getAllOrders() {
//        return StreamSupport.stream(orderRepo.findAll().spliterator(), false)
//                .collect(Collectors.toList());
        return orderRepo.findAll();
    }

    public Order findById(int id) {
        return orderRepo.findOne(id);
    }

    public double computeTotal(Order o) {
        orderValidator.validate(o);
        return o.getItems().stream()
                .mapToDouble(i -> productRepo.findOne(i.getProduct().getId()).getPrice() * i.getQuantity())
                .sum();
    }

    public void addItem(Order o, OrderItem item) {
        orderValidator.validate(o);
        orderItemValidator.validate(item);
        o.getItems().add(item);
        orderRepo.update(o);
    }

    public void removeItem(Order o, OrderItem item) {
        orderValidator.validate(o);
        orderItemValidator.validate(item);
        o.getItems().remove(item);
        orderRepo.update(o);
    }
}