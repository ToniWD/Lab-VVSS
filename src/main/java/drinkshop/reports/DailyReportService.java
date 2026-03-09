package drinkshop.reports;

import drinkshop.domain.Order;
import drinkshop.repository.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DailyReportService {
    private Repository<Integer, Order> repo;

    public DailyReportService(Repository<Integer, Order> repo) {
        this.repo = repo;
    }

    public double getTotalRevenue() {
        return repo.findAll().stream().mapToDouble(Order::getTotal).sum();
    }

    public int getTotalOrders() {
        return repo.findAll().size();
    }

    public double getTotalRevenueForDate(LocalDate date) {
        return repo.findAll().stream()
                .filter(order -> order.getOrderDateTime() != null)
                .filter(order -> order.getOrderDateTime().toLocalDate().equals(date))
                .mapToDouble(Order::getTotal)
                .sum();
    }

    public int getTotalOrdersForDate(LocalDate date) {
        return (int) repo.findAll().stream()
                .filter(order -> order.getOrderDateTime() != null)
                .filter(order -> order.getOrderDateTime().toLocalDate().equals(date))
                .count();
    }

    public double getTotalRevenueToday() {
        return getTotalRevenueForDate(LocalDate.now());
    }

    public int getTotalOrdersToday() {
        return getTotalOrdersForDate(LocalDate.now());
    }
}