package drinkshop.service;

import drinkshop.domain.*;
import drinkshop.repository.InMemoryProductRepository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Teste pentru ProductService - ECP și BVA")
class ProductServiceTest {

    private ProductService service;

    @BeforeEach
    void setUp() {
        // Setup comun pentru fiecare test
        var repo = new InMemoryProductRepository();
        var validator = new ProductValidator();
        service = new ProductService(repo, validator);
    }

    // --- TESTE ECP (Din prima imagine) ---

    @Test
    @DisplayName("ECP 1: Adăugare produs valid (Matcha)")
    @Tag("Valid")
    void testAddProduct_ECP_Valid_Matcha() {
        // Arrange
        Product p = new Product(69, "Matcha", 29.01, DrinkCategory.BUBBLE_TEA, DrinkBase.PLANT_BASED);

        // Act & Assert
        assertDoesNotThrow(() -> service.addProduct(p));
        assertEquals(1, service.getAllProducts().size());
    }

    @Test
    @DisplayName("ECP 2: Adăugare produs valid (Red Bull)")
    @Tag("Valid")
    void testAddProduct_ECP_Valid_RedBull() {
        // Arrange
        Product p = new Product(99, "Red bull", 5.99, DrinkCategory.TEA, DrinkBase.BASIC);

        // Act & Assert
        assertDoesNotThrow(() -> service.addProduct(p));
    }

    @Test
    @DisplayName("ECP 3: ID Invalid (Negativ)")
    @Tag("Invalid")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void testAddProduct_ECP_Invalid_NegativeId() {
        // Arrange
        Product p = new Product(-6, "Suc", 65.32, DrinkCategory.TEA, DrinkBase.PLANT_BASED);

        // Act & Assert
        assertThrows(ValidationException.class, () -> service.addProduct(p), "Ar trebui să refuze ID negativ");
    }

    @Test
    @DisplayName("ECP 4: Preț Invalid (Negativ)")
    @Tag("Invalid")
    void testAddProduct_ECP_Invalid_NegativePrice() {
        // Arrange
        Product p = new Product(9, "Caramel Frapucino", -23.02, DrinkCategory.SPECIAL_COFFEE, DrinkBase.BASIC);

        // Act & Assert
        assertThrows(ValidationException.class, () -> service.addProduct(p));
    }

    // --- TESTE BVA (Din a doua imagine) ---

    @Test
    @DisplayName("BVA 1: ID la limită (0 - Valid)")
    @Tag("Boundary")
    void testAddProduct_BVA_Valid_IdZero() {
        // Arrange
        Product p = new Product(0, "Adio tata", 69.99, DrinkCategory.BUBBLE_TEA, DrinkBase.PLANT_BASED);

        // Act & Assert
        assertDoesNotThrow(() -> service.addProduct(p));
    }

    @Test
    @DisplayName("BVA 2: ID la limită (-1 - Invalid)")
    @Tag("Boundary")
    void testAddProduct_BVA_Invalid_IdMinusOne() {
        // Arrange
        Product p = new Product(-1, "Suc", 23.2, DrinkCategory.BUBBLE_TEA, DrinkBase.PLANT_BASED);

        // Act & Assert
        Exception exception = assertThrows(ValidationException.class, () -> service.addProduct(p));
        assertTrue(exception.getMessage().contains("ID invalid!"));
    }

    @Test
    @DisplayName("BVA 3: Preț la limită (0 - Invalid)")
    @Tag("Boundary")
    void testAddProduct_BVA_Invalid_PriceZero() {
        // Arrange
        Product p = new Product(23, "Suc3", 0, DrinkCategory.BUBBLE_TEA, DrinkBase.PLANT_BASED);

        // Act & Assert
        Exception exception = assertThrows(ValidationException.class, () -> service.addProduct(p));
        assertTrue(exception.getMessage().contains("Pret invalid!"));
    }

    @Test
    @DisplayName("BVA 4: Preț la limită (0.01 - Valid)")
    @Tag("Boundary")
    void testAddProduct_BVA_Valid_PriceSmallest() {
        // Arrange
        Product p = new Product(24, "Suc4", 0.01, DrinkCategory.BUBBLE_TEA, DrinkBase.PLANT_BASED);

        // Act & Assert
        assertDoesNotThrow(() -> service.addProduct(p));
    }

    @Test
    @DisplayName("Filter: category null → listă goală")
    void testFilterByCategory_Null() {
        Product p1 = new Product(1, "A", 10, DrinkCategory.ICED_COFFEE, DrinkBase.BASIC);
        service.addProduct(p1);

        var result = service.filterByCategory(null);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Filtrare cu null ar trebui să returneze listă goală");
    }

    @Test
    @DisplayName("Filter: category ALL → returnează toate produsele")
    void testFilterByCategory_All() {
        Product p1 = new Product(1, "A", 10, DrinkCategory.ICED_COFFEE, DrinkBase.BASIC);
        Product p2 = new Product(2, "B", 15, DrinkCategory.TEA, DrinkBase.BASIC);
        service.addProduct(p1);
        service.addProduct(p2);

        var result = service.filterByCategory(DrinkCategory.ALL);

        assertEquals(2, result.size(), "Filtrare ALL ar trebui să returneze toate produsele");
        assertTrue(result.contains(p1));
        assertTrue(result.contains(p2));
    }

    @Test
    @DisplayName("Filter: category ICED_COFFEE → returnează doar produsele ICED_COFFEE")
    void testFilterByCategory_Match() {
        Product p1 = new Product(1, "A", 10, DrinkCategory.ICED_COFFEE, DrinkBase.BASIC);
        Product p2 = new Product(2, "B", 15, DrinkCategory.ICED_COFFEE, DrinkBase.BASIC);
        Product p3 = new Product(3, "C", 20, DrinkCategory.TEA, DrinkBase.BASIC);
        service.addProduct(p1);
        service.addProduct(p2);
        service.addProduct(p3);

        var result = service.filterByCategory(DrinkCategory.ICED_COFFEE);

        assertEquals(2, result.size(), "Filtrare ICED_COFFEE ar trebui să returneze 2 produse");
        assertTrue(result.contains(p1));
        assertTrue(result.contains(p2));
    }

    @Test
    @DisplayName("Filter: category TEA fără match → listă goală")
    void testFilterByCategory_NoMatch() {
        Product p1 = new Product(1, "A", 10, DrinkCategory.ICED_COFFEE, DrinkBase.BASIC);
        service.addProduct(p1);

        var result = service.filterByCategory(DrinkCategory.TEA);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Filtrare fără match ar trebui să returneze listă goală");
    }

    @Test
    @DisplayName("Filter: listă goală → orice categorie → listă goală")
    void testFilterByCategory_EmptyList() {
        var result = service.filterByCategory(DrinkCategory.ICED_COFFEE);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Filtrare pe listă goală ar trebui să returneze listă goală");
    }

    @Test
    @DisplayName("Filter: listă cu element null → elementul null este ignorat")
    void testFilterByCategory_NullElement_ManualList() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "A", 10, DrinkCategory.ICED_COFFEE, DrinkBase.BASIC));
        products.add(null);
        ProductService serviceWithCustomList = new ProductService(new InMemoryProductRepository(), new ProductValidator()) {
            @Override
            public List<Product> getAllProducts() {
                return products;
            }
        };

        var result = serviceWithCustomList.filterByCategory(DrinkCategory.ICED_COFFEE);

        assertNotNull(result);
        assertEquals(1, result.size(), "Elementul null este ignorat");
    }

}