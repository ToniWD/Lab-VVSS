package drinkshop.ui;

import drinkshop.domain.*;
import drinkshop.reports.DailyReportService;
import drinkshop.repository.Repository;
import drinkshop.repository.file.FileOrderRepository;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.repository.file.FileRecipeRepository;
import drinkshop.repository.file.FileStockRepository;
import drinkshop.service.*;
import drinkshop.service.validator.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DrinkShopApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // ---------- Initializare Repository-uri care citesc din fisiere ----------
        Repository<Integer, Product> productRepo = new FileProductRepository("data/products.txt");
        Repository<Integer, Order> orderRepo = new FileOrderRepository("data/orders.txt", productRepo);
        Repository<Integer, Recipe> recipeRepo = new FileRecipeRepository("data/retete.txt");
        Repository<Integer, Stock> stockRepo = new FileStockRepository("data/stocuri.txt");

        // ---------- Initializare Services ----------

        var productService = new ProductService(productRepo, new ProductValidator());
        var orderService = new OrderService(orderRepo, productRepo, new OrderValidator(), new OrderItemValidator());
        var recipeService = new RecipeService(recipeRepo, new RecipeValidator());
        var stockService = new StockService(stockRepo, new StockValidator());
        var report = new DailyReportService(orderRepo);

        // ---------- Incarcare FXML ----------

        FXMLLoader loader = new FXMLLoader(getClass().getResource("drinkshop.fxml"));
        Scene scene = new Scene(loader.load());

        // ---------- Setare Service in Controller ----------
        DrinkShopController controller = loader.getController();
        controller.setServices(productService, orderService, recipeService, stockService, report);

        // ---------- Afisare Fereastra ----------
        stage.setTitle("Coffee Shop Management");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}