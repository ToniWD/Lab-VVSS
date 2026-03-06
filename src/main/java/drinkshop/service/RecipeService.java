package drinkshop.service;

import drinkshop.domain.Recipe;
import drinkshop.repository.Repository;
import drinkshop.service.validator.Validator;

import java.util.List;

public class RecipeService {

    private final Repository<Integer, Recipe> recipeRepo;
    private final Validator<Recipe> recipeValidator;

    public RecipeService(Repository<Integer, Recipe> recipeRepo, Validator<Recipe> recipeValidator) {
        this.recipeRepo = recipeRepo;
        this.recipeValidator = recipeValidator;
    }

    public void addRecipe(Recipe r) {
        recipeValidator.validate(r);
        recipeRepo.save(r);
    }

    public void updateRecipe(Recipe r) {
        recipeValidator.validate(r);
        recipeRepo.update(r);
    }

    public void deleteRecipe(int id) {
        recipeRepo.delete(id);
    }

    public Recipe findById(int id) {
        return recipeRepo.findOne(id);
    }

    public List<Recipe> getAll() {
        return recipeRepo.findAll();
    }
}