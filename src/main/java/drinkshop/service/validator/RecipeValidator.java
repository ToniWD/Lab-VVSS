package drinkshop.service.validator;

import drinkshop.domain.RecipeIngredient;
import drinkshop.domain.Recipe;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class RecipeValidator implements Validator<Recipe> {

    @Override
    public void validate(Recipe recipe) {

        AtomicReference<String> errors = new AtomicReference<>("");

        if (recipe.getId() <= 0)
            errors.accumulateAndGet("Product ID invalid!\n", String::concat);

        List<RecipeIngredient> ingrediente = recipe.getIngredients();
        if (ingrediente == null || ingrediente.isEmpty())
            errors.accumulateAndGet("Ingrediente empty!\n", String::concat);

        ingrediente.stream()
                .filter(entry -> entry.getQuantity() <= 0)
                .forEach(entry -> {
                    errors.accumulateAndGet("[" + entry.getName() + "]"+ "cantitate negativa sau zero", String::concat);
                });

        if (!errors.get().isEmpty())
            throw new ValidationException(errors.get());
    }
}
