package drinkshop.service.it;

import static org.mockito.Mockito.*;

import drinkshop.domain.Product;
import drinkshop.repository.Repository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ValidationException;
import drinkshop.service.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ProductServiceTest {
    private ProductService service;

    @Mock private Validator<Product> validatorMock;
    @Mock private Repository<Integer, Product> repoMock;
    @Mock private Product productMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ProductService(repoMock, validatorMock);
    }

    @Test
    void testAddProduct_Unit() {
        // Test 1: Succes - Verificăm fluxul apelurilor
        service.addProduct(productMock);

        verify(validatorMock, times(1)).validate(productMock);
        verify(repoMock, times(1)).save(productMock);
    }

    @Test
    void testAddProduct_ValidationFails() {
        // Test 2: Exception handling - Verificăm că repo nu e apelat dacă validarea eșuează
        doThrow(new ValidationException("Error")).when(validatorMock).validate(productMock);

        try {
            service.addProduct(productMock);
        } catch (ValidationException e) {
            // Assert
        }

        verify(repoMock, never()).save(productMock);
    }
}
