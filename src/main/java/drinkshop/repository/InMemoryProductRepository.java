package drinkshop.repository;

import drinkshop.domain.Product;

public class InMemoryProductRepository extends AbstractRepository<Integer, Product> {
    @Override
    protected Integer getId(Product entity) {
        return entity.getId();
    }
}
