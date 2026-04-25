package drinkshop.service.it;

import drinkshop.domain.DrinkBase;
import drinkshop.domain.DrinkCategory;
import drinkshop.domain.Product;
import drinkshop.repository.Repository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class ProductServiceIntegration_Step3 {
    private ProductService service;
    private ProductValidator validator;
    private Product realProduct;

    @Mock
    private Repository<Integer, Product> repoMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new ProductValidator();
        service = new ProductService(repoMock, validator);
    }

    @Test
    void testAddProduct_Integration_S_V_E_Success() {
        realProduct = new Product(1, "Espresso", 10.0, DrinkCategory.ICED_COFFEE, DrinkBase.BASIC);

        service.addProduct(realProduct);

        verify(repoMock).save(realProduct);
    }

    @Test
    void testAddProduct_Integration_S_V_E_InvalidPrice() {
        realProduct = new Product(1, "Espresso", -5.0, DrinkCategory.ICED_COFFEE, DrinkBase.BASIC);

        // Verificăm că validatorul real aruncă excepția în contextul service-ului
        org.junit.jupiter.api.Assertions.assertThrows(ValidationException.class, () -> {
            service.addProduct(realProduct);
        });

        verify(repoMock, never()).save(any());
    }
}
