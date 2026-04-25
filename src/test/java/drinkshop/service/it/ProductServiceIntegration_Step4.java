package drinkshop.service.it;

import drinkshop.domain.DrinkBase;
import drinkshop.domain.DrinkCategory;
import drinkshop.domain.Product;
import drinkshop.repository.InMemoryProductRepository;
import drinkshop.repository.Repository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
