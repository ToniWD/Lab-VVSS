package drinkshop.service;

import drinkshop.domain.RecipeIngredient;
import drinkshop.domain.Recipe;
import drinkshop.domain.Stock;
import drinkshop.repository.Repository;
import drinkshop.service.validator.Validator;

import java.util.List;
import java.util.stream.Collectors;

public class StockService {

    private final Repository<Integer, Stock> stockRepo;
    private final Validator<Stock> stockValidator;

    public StockService(Repository<Integer, Stock> stockRepo, Validator<Stock> stockValidator) {
        this.stockRepo = stockRepo;
        this.stockValidator = stockValidator;
    }

    public List<Stock> getAll() {
        return stockRepo.findAll();
    }

    public void add(Stock s) {
        stockValidator.validate(s);
        stockRepo.save(s);
    }

    public void update(Stock s) {
        stockValidator.validate(s);
        stockRepo.update(s);
    }

    public void delete(int id) {
        stockRepo.delete(id);
    }

    public boolean areSuficient(Recipe recipe) {
        List<RecipeIngredient> ingredienteNecesare = recipe.getIngredients();

        for (RecipeIngredient e : ingredienteNecesare) {
            String ingredient = e.getName();
            double necesar = e.getQuantity();

            double disponibil = stockRepo.findAll().stream()
                    .filter(s -> s.getIngredient().equalsIgnoreCase(ingredient))
                    .mapToDouble(Stock::getQuantity)
                    .sum();

            if (disponibil < necesar) {
                return false;
            }
        }
        return true;
    }

    public void consume(Recipe recipe) {
        if (!areSuficient(recipe)) {
            throw new IllegalStateException("Stoc insuficient pentru rețeta.");
        }

        for (RecipeIngredient e : recipe.getIngredients()) {
            String ingredient = e.getName();
            double necesar = e.getQuantity();

            List<Stock> ingredienteStock = stockRepo.findAll().stream()
                    .filter(s -> s.getIngredient().equalsIgnoreCase(ingredient))
                    .collect(Collectors.toList());

            double ramas = necesar;

            for (Stock s : ingredienteStock) {
                if (ramas <= 0) break;

                double deScazut = Math.min(s.getQuantity(), ramas);
                s.setQuantity((int) (s.getQuantity() - deScazut));
                ramas -= deScazut;

                stockRepo.update(s);
            }
        }
    }
}