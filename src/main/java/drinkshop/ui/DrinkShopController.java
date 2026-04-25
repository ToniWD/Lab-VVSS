package drinkshop.ui;

import drinkshop.domain.*;
import drinkshop.export.CsvExporter;
import drinkshop.receipt.ReceiptGenerator;
import drinkshop.reports.DailyReportService;
import drinkshop.service.*;
import drinkshop.service.validator.ValidationException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DrinkShopController {
    private ProductService productService;
    private OrderService orderService;
    private RecipeService recipeService;
    private StockService stockService;
    private DailyReportService report;

    // ---------- PRODUCT ----------
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> colProdId;
    @FXML private TableColumn<Product, String> colProdName;
    @FXML private TableColumn<Product, Double> colProdPrice;
    @FXML private TableColumn<Product, DrinkCategory> colProdCategorie;
    @FXML private TableColumn<Product, DrinkBase> colProdTip;
    @FXML private TextField txtProdName, txtProdPrice;
    @FXML private ComboBox<DrinkCategory> comboProdCategorie;
    @FXML private ComboBox<DrinkBase> comboProdTip;

    // ---------- RETETE ----------
    @FXML private TableView<Recipe> retetaTable;
    @FXML private TableColumn<Recipe, Integer> colRetetaId;
    @FXML private TableColumn<Recipe, String> colRetetaDesc;

    @FXML private TableView<RecipeIngredient> newRetetaTable;
    @FXML private TableColumn<RecipeIngredient, String> colNewIngredName;
    @FXML private TableColumn<RecipeIngredient, Double> colNewIngredCant;
    @FXML private TextField txtNewIngredName, txtNewIngredCant;

    // ---------- ORDER (CURRENT) ----------
    @FXML private TableView<OrderItem> currentOrderTable;
    @FXML private TableColumn<OrderItem, String> colOrderProdName;
    @FXML private TableColumn<OrderItem, Integer> colOrderQty;

    @FXML private ComboBox<Integer> comboQty;
    @FXML private Label lblOrderTotal;
    @FXML private TextArea txtReceipt;

    @FXML private Label lblTotalRevenue;


    // ---------- STOCK ----------
    @FXML private TableView<Stock> stockTable;
    @FXML private TableColumn<Stock, String> colStockIngred;
    @FXML private TableColumn<Stock, Double> colStockQty;

    private ObservableList<Stock> stockList = FXCollections.observableArrayList();

    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private ObservableList<Recipe> recipeList = FXCollections.observableArrayList();
    private ObservableList<RecipeIngredient> newRetetaList = FXCollections.observableArrayList();
    private ObservableList<OrderItem> currentOrderItems = FXCollections.observableArrayList();

    private Order currentOrder = new Order(1);

    public void setServices(ProductService productService, OrderService orderService, RecipeService recipeService, StockService stockService, DailyReportService report) {
        this.productService = productService;
        this.orderService = orderService;
        this.recipeService = recipeService;
        this.stockService = stockService;
        this.report = report;
        initData();
    }

    @FXML
    private void initialize() {

        // STOCK
        colStockIngred.setCellValueFactory(new PropertyValueFactory<>("ingredient"));
        colStockQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        stockTable.setItems(stockList);

        // PRODUCTS
        colProdId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProdName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colProdPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colProdCategorie.setCellValueFactory(new PropertyValueFactory<>("category"));
        colProdTip.setCellValueFactory(new PropertyValueFactory<>("drinkBase"));
        productTable.setItems(productList);

        comboProdCategorie.getItems().setAll(DrinkCategory.values());
        comboProdTip.getItems().setAll(DrinkBase.values());

        // RETETE
        colRetetaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRetetaDesc.setCellValueFactory(data -> {
            Recipe r = data.getValue();
            String desc = r.getIngredients().stream()
                    .map(i -> i.getName() + " (" + i.getQuantity() + ")")
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(desc);
        });
        retetaTable.setItems(recipeList);

        colNewIngredName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNewIngredCant.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        newRetetaTable.setItems(newRetetaList);

        // CURRENT ORDER TABLE
        colOrderProdName.setCellValueFactory(data -> {
            int prodId = data.getValue().getProduct().getId();
            Product p = productList.stream().filter(pr -> pr.getId() == prodId).findFirst().orElse(null);
            return new SimpleStringProperty(p != null ? p.getName() : "N/A");
        });
        colOrderQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        currentOrderTable.setItems(currentOrderItems);

        comboQty.setItems(FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10));
    }

    private void initData() {
        productList.setAll(productService.getAllProducts());
        recipeList.setAll(recipeService.getAll());
        stockList.setAll(stockService.getAll());
        lblTotalRevenue.setText("Daily Revenue: " + report.getTotalRevenueToday());
        updateOrderTotal();

        AtomicInteger orderMaxId = new AtomicInteger(1);
        orderService.getAllOrders().forEach(order -> {if(orderMaxId.get() < order.getId())
            orderMaxId.set(order.getId());});

        currentOrder = new Order(orderMaxId.get() + 1);
    }

    // ---------- PRODUCT ----------
    @FXML
    private void onAddProduct() {
        Recipe r=retetaTable.getSelectionModel().getSelectedItem();

        if (r == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Selectati o reteta pentru care adugati un produs");
            alert.showAndWait();
            return;
        }else
        if (productService.getAllProducts().stream().filter(p->p.getId()==r.getId()).collect(Collectors.toList()).size()>0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Exista un produs cu reteta adaugata.");
            alert.showAndWait();
            return;
        }
        Product p = new Product(r.getId(),
                txtProdName.getText(),
                Double.parseDouble(txtProdPrice.getText()),
                comboProdCategorie.getValue(),
                comboProdTip.getValue());
        try {
            productService.addProduct(p);
            initData();
        } catch (ValidationException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onUpdateProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try {
            productService.updateProduct(selected.getId(), txtProdName.getText(),
                    Double.parseDouble(txtProdPrice.getText()),
                    comboProdCategorie.getValue(), comboProdTip.getValue());
            initData();
        } catch (ValidationException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onDeleteProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        productService.deleteProduct(selected.getId());
        initData();
    }

    @FXML
    private void onFilterCategorie() {
        productList.setAll(productService.filterByCategory(comboProdCategorie.getValue()));
    }

    @FXML
    private void onFilterTip() {
        productList.setAll(productService.filterByTip(comboProdTip.getValue()));
    }

    // ---------- RETETA NOUA ----------
    @FXML
    private void onAddNewIngred() {
        newRetetaList.add(new RecipeIngredient(txtNewIngredName.getText(),
                Double.parseDouble(txtNewIngredCant.getText())));
    }

    @FXML
    private void onDeleteNewIngred() {
        RecipeIngredient sel = newRetetaTable.getSelectionModel().getSelectedItem();
        if (sel != null) newRetetaList.remove(sel);
    }

    @FXML
    private void onAddNewReteta() {
        Recipe r = new Recipe(recipeService.getAll().size()+1, new ArrayList<>(newRetetaList));
        try {
            recipeService.addRecipe(r);
            newRetetaList.clear();
            initData();
        } catch (ValidationException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onClearNewRetetaIngredients() {
        newRetetaTable.getItems().clear();
        txtNewIngredName.clear();
        txtNewIngredCant.clear();
    }

    // ---------- CURRENT ORDER ----------
    @FXML
    private void onAddOrderItem() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        Integer qty = comboQty.getValue();

        if (selected == null) {
            showError("Selectează un produs din listă.");
            return;
        }
        if (qty == null) {
            showError("Selectează cantitatea.");
            return;
        }

        currentOrderItems.add(new OrderItem(selected, qty));
        updateOrderTotal();
    }

    @FXML
    private void onDeleteOrderItem() {
        OrderItem sel = currentOrderTable.getSelectionModel().getSelectedItem();
        if (sel != null) {
            currentOrderItems.remove(sel);
            updateOrderTotal();
        }
    }

    @FXML
    private void onFinalizeOrder() {
        currentOrder.getItems().clear();
        currentOrder.getItems().addAll(currentOrderItems);
        currentOrder.computeTotalPrice();

        try {
            for (OrderItem item : currentOrderItems) {
                Recipe recipe = recipeService.getAll().stream()
                        .filter(r -> r.getId() == item.getProduct().getId())
                        .findFirst()
                        .orElse(null);

                if (recipe != null) {
                    stockService.consume(recipe);
                }
            }

            orderService.addOrder(currentOrder);
            txtReceipt.setText(ReceiptGenerator.generate(currentOrder, productService.getAllProducts()));

            currentOrderItems.clear();
            currentOrder = new Order(currentOrder.getId() + 1);
            updateOrderTotal();

            stockList.setAll(stockService.getAll());

        } catch (ValidationException e) {
            showError(e.getMessage());
        } catch (IllegalStateException e) {
            showError("Stoc insuficient pentru comanda.");
        }
    }

    private void updateOrderTotal() {
        currentOrder.getItems().clear();
        currentOrder.getItems().addAll(currentOrderItems);
        double total = currentOrder.getItems().isEmpty()? 0 : orderService.computeTotal(currentOrder);
        lblOrderTotal.setText("Total: " + total);
    }

    // ---------- EXPORT + REVENUE ----------
    @FXML
    private void onExportOrdersCsv() {
        CsvExporter.exportOrders(productService.getAllProducts(), orderService.getAllOrders(), "orders.csv");
    }

    @FXML
    private void onDailyRevenue() {
        lblTotalRevenue.setText("Daily Revenue: " + report.getTotalRevenueToday());
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
}