package drinkshop.domain;

public class Product {

    private int id;
    private String name;
    private double price;
    private DrinkCategory category;
    private DrinkBase drinkBase;

    public Product(int id, String name, double price,
                   DrinkCategory category,
                   DrinkBase drinkBase) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.drinkBase = drinkBase;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public DrinkCategory getCategory() { return category; }

    public void setCategory(DrinkCategory category) {
        this.category = category;
    }

    public DrinkBase getDrinkBase() { return drinkBase; }

    public void setDrinkBase(DrinkBase drinkBase) {
        this.drinkBase = drinkBase;
    }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return name + " (" + category + ", " + drinkBase + ") - " + price + " lei";
    }
}