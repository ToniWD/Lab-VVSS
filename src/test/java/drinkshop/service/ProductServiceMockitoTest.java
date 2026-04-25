package drinkshop.service;

import drinkshop.domain.Product;
import drinkshop.repository.Repository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ValidationException;
import drinkshop.service.validator.Validator;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class ProductServiceMockitoTest {

    private Product product;
    private Validator<Product> validatorMock;
    private Repository<Integer, Product> repoMock;

    private ProductService service;

    @BeforeEach
    void setUp() {
        product = mock(Product.class);
        validatorMock = mock(Validator.class);
        repoMock = mock(Repository.class);

        service = new ProductService(repoMock, validatorMock);
    }

    @AfterEach
    void tearDown() {
        service = null;
        validatorMock = null;
        repoMock = null;
        product = null;
    }

    @Test
    void testAddProductInvalid() {

        doThrow(new ValidationException("Invalid product"))
                .when(validatorMock).validate(product);

        try {
            service.addProduct(product);
            fail("Exception was expected");
        } catch (Exception e) {
            assert e instanceof ValidationException;
        }

        verify(validatorMock, times(1)).validate(product);
        verify(repoMock, never()).save(any());
    }

    @Test
    void testAddProductValid() {

        doNothing().when(validatorMock).validate(product);
        when(repoMock.save(product)).thenReturn(product);

        try {
            service.addProduct(product);
        } catch (Exception e) {
            fail("Should not throw exception");
        }

        verify(validatorMock, times(1)).validate(product);
        verify(repoMock, times(1)).save(product);
    }
}