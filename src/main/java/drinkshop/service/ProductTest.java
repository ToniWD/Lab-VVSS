package drinkshop.service;

import drinkshop.domain.DrinkBase;
import drinkshop.domain.DrinkCategory;
import drinkshop.domain.Product;
import drinkshop.repository.InMemoryProductRepository;
import drinkshop.repository.Repository;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.service.validator.ProductValidator;



public class ProductTest {

    private static void check(boolean condition, String testName) {
        if (condition) {
            System.out.println("[PASS] " + testName);
        } else {
            System.err.println("[FAIL] " + testName);
        }
    }
    static void testAddValidProductECP(){
        Repository<Integer, Product> productRepo = new InMemoryProductRepository();
        ProductService productService = new ProductService(productRepo, new ProductValidator());
        Product p = new Product(1, "Suc", 50.99, DrinkCategory.BUBBLE_TEA, DrinkBase.BASIC);
      try{
          productService.addProduct(p);
          check(productService.getAllProducts().size() == 1, "ECP Valid: Produs adăugat cu preț pozitiv");
    } catch (Exception e) {
        check(false, "ECP Valid: Nu trebuia să arunce excepție");
    }
    }
    static  void testAddInvalidProductECP(){
        Repository<Integer, Product> productRepo = new InMemoryProductRepository();
        ProductService productService = new ProductService(productRepo, new ProductValidator());
        Product p = new Product(2, "Suc", -50.99, DrinkCategory.BUBBLE_TEA, DrinkBase.BASIC);
        try{
            productService.addProduct(p);
            check(false, "ECP Invalid: Trebuia să arunce eroare pentru preț negativ");
        } catch (Exception e) {
            check(true, "ECP Invalid: Eroarea a fost prinsă corect pentru preț negativ");
        }
    }

    static void testAddValidProductBVA(){
        Repository<Integer, Product> productRepo = new InMemoryProductRepository();
        ProductService productService = new ProductService(productRepo, new ProductValidator());
        Product p = new Product(0, "Suc", 50.99, DrinkCategory.BUBBLE_TEA, DrinkBase.BASIC);
        try{
            productService.addProduct(p);
            check(productService.getAllProducts().size() == 1, "BVA Valid: Produs adăugat cu id 0");
        } catch (Exception e) {
            check(false, "BVA Valid: Nu trebuia să arunce excepție");
        }
    }

   static void testAddInvalidProductBVA(){
        Repository<Integer, Product> productRepo = new InMemoryProductRepository();
        ProductService productService = new ProductService(productRepo, new ProductValidator());
        Product p = new Product(67, "Suc", 0.0, DrinkCategory.BUBBLE_TEA, DrinkBase.BASIC);
        try{
            productService.addProduct(p);
            check(false, "BVA Invalid: Prețul 0.0 nu ar trebui permis");
        } catch (Exception e) {
            check(true, "BVA Invalid: Prețul 0.0 a fost respins corect");
        }
    }

    public static void runTests(){
        testAddValidProductECP();
        testAddInvalidProductECP();
        testAddValidProductBVA();
        testAddInvalidProductBVA();
    }
}
