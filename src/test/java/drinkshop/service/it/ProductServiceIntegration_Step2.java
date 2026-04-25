package drinkshop.service.it;

import drinkshop.domain.Product;
import drinkshop.repository.Repository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductServiceIntegration_Step2 {
    private ProductService service;
    private ProductValidator validator; // Real

    @Mock
    private Repository<Integer, Product> repoMock;
    @Mock private Product productMock; // E este încă mock

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new ProductValidator();
        service = new ProductService(repoMock, validator);
    }

    @Test
    void testAddProduct_Integration_S_V() {
        // Simulăm un produs valid prin mock
        when(productMock.getId()).thenReturn(1);
        when(productMock.getName()).thenReturn("Cafea");
        when(productMock.getPrice()).thenReturn(15.0);

        service.addProduct(productMock);

        verify(repoMock).save(productMock);
    }
}
