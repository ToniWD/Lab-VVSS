package drinkshop.service;

import drinkshop.domain.*;
import drinkshop.repository.Repository;
import drinkshop.service.validator.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductService {

    private final Repository<Integer, Product> productRepo;
    private final Validator<Product> productValidator;

    public ProductService(Repository<Integer, Product> productRepo, Validator<Product> productValidator) {
        this.productRepo = productRepo;
        this.productValidator = productValidator;
    }

    public void addProduct(Product p) {
        productValidator.validate(p);
        productRepo.save(p);
    }

    public void updateProduct(int id, String name, double price, DrinkCategory category, DrinkBase tip) {
        Product updated = new Product(id, name, price, category, tip);
        productValidator.validate(updated);
        productRepo.update(updated);

    }

    public void deleteProduct(int id) {
        productRepo.delete(id);
    }

    public List<Product> getAllProducts() {
//        Iterable<Product> it=productRepo.findAll();
//        ArrayList<Product> products=new ArrayList<>();
//        it.forEach(products::add);
//        return products;

//        return StreamSupport.stream(productRepo.findAll().spliterator(), false)
//                    .collect(Collectors.toList());
        return productRepo.findAll();
    }

    public Product findById(int id) {
        return productRepo.findOne(id);
    }

//    public List<Product> filterByCategory(DrinkCategory category) {
//        if (category == DrinkCategory.ALL) return getAllProducts();
//        return getAllProducts().stream()
//                .filter(p -> p.getCategory() == category)
//                .collect(Collectors.toList());
//    }
    public List<Product> filterByCategory(DrinkCategory category) {
        List<Product> result = new ArrayList<>();
        List<Product> products = getAllProducts();

        if (category == null) {
            return result;
        }

        if (category == DrinkCategory.ALL) {
            return products;
        }

        for (Product p : products) {
            if (p != null && p.getCategory() == category) {
                result.add(p);
            }
        }

        return result;
    }

    public List<Product> filterByTip(DrinkBase tip) {
        if (tip == DrinkBase.ALL) return getAllProducts();
        return getAllProducts().stream()
                .filter(p -> p.getDrinkBase() == tip)
                .collect(Collectors.toList());
    }
}