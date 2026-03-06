package drinkshop.domain;

public class Stock {

    private int id;
    private String ingredient;
    private double quantity;
    private double minimumStock;

    public Stock(int id, String ingredient, double quantity, double minimumStock) {
        this.id = id;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.minimumStock = minimumStock;
    }

    // --- getters ---
    public int getId() {
        return id;
    }

    public String getIngredient() {
        return ingredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getMinimumStock() {
        return minimumStock;
    }

    // --- setters ---
    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setMinimumStock(int minimumStock) {
        this.minimumStock = minimumStock;
    }

    // --- helper methods (safe to keep in entity) ---
    public boolean isSubMinim() {
        return quantity < minimumStock;
    }

    @Override
    public String toString() {
        return ingredient + " (" + quantity + " / minim: " + minimumStock + ")";
    }
}