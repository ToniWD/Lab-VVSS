package drinkshop.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductTest {

    Product product;

    @BeforeEach
    void setUp() {
        product =new Product(100, "Limonada", 10.0, DrinkCategory.JUICE, DrinkBase.WATER_BASED);
    }

    @AfterEach
    void tearDown() {
        product = null;
    }

    @Test
    void getId() {
        assert 100 == product.getId();
    }

    @Test
    void getNume() {
        assert "Limonada".equals(product.getName());
    }

    @Test
    void getPret() {
        assert 10.0 == product.getPrice();
    }

    @Test
    void getCategorie() {
        assert DrinkCategory.JUICE.equals(product.getCategory());
    }

    @Test
    void setCategorie() {
        product.setCategory(DrinkCategory.SMOOTHIE);
        assert DrinkCategory.SMOOTHIE.equals(product.getCategory());
    }

    @Test
    void getTip() {
        assert DrinkBase.WATER_BASED.equals(product.getDrinkBase());
    }

    @Test
    void setTip() {
        product.setDrinkBase(DrinkBase.BASIC);
        assert DrinkBase.BASIC.equals(product.getDrinkBase());
    }

    @Test
    void setNume() {
        product.setName("newLimonada");
        assert "newLimonada".equals(product.getName());
    }

    @Test
    void setPret() {
        product.setPrice(10.05);
        assert 10.05 == product.getPrice();
    }

    @Test
    void testToString() {
        System.out.println(product.toString());
        assert "Limonada (JUICE, WATER_BASED) - 10.0 lei".equals(product.toString());
    }
}