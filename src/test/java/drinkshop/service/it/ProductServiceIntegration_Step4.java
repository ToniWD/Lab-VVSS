package drinkshop.service.it;

import drinkshop.domain.DrinkBase;
import drinkshop.domain.DrinkCategory;
import drinkshop.domain.Product;
import drinkshop.repository.InMemoryProductRepository;
import drinkshop.repository.Repository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductServiceIntegration_Step4 {
    private ProductService service;
    private ProductValidator validator;
    private Repository<Integer, Product> realRepo; // Presupunem o implementare reală

    @BeforeEach
    void setUp() {
        validator = new ProductValidator();
        realRepo = new InMemoryProductRepository(); // Implementarea ta reală
        service = new ProductService(realRepo, validator);
    }

    @Test
    void testAddProduct_FullIntegration() {
        Product p = new Product(10, "Latte", 12.0, DrinkCategory.ICED_COFFEE, DrinkBase.WATER_BASED);

        service.addProduct(p);

        // Assert: Verificăm dacă produsul a ajuns în repository-ul real
        org.junit.jupiter.api.Assertions.assertEquals(p, realRepo.findOne(10));
    }

    @Test
    void testAddProduct_FullIntegration_Invalid() {
        Product invalidProduct = new Product(11, "Espresso", -5.0, DrinkCategory.ICED_COFFEE, DrinkBase.WATER_BASED);

        assertThrows(ValidationException.class, () -> {
            service.addProduct(invalidProduct);
        }, "Ar trebui să arunce o excepție pentru preț negativ");

        assertNull(realRepo.findOne(11),
                "Produsul invalid nu ar fi trebuit să fie salvat în repository");
    }
}
