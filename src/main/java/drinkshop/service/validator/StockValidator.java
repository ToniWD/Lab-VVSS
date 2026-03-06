package drinkshop.service.validator;

import drinkshop.domain.Stock;

public class StockValidator implements Validator<Stock> {

    @Override
    public void validate(Stock stock) {

        String errors = "";

        if (stock.getId() <= 0)
            errors += "ID invalid!\n";

        if (stock.getIngredient() == null || stock.getIngredient().isBlank())
            errors += "Ingredient invalid!\n";

        if (stock.getQuantity() < 0)
            errors += "Cantitate negativa!\n";

        if (stock.getMinimumStock() < 0)
            errors += "Stoc minim negativ!\n";

        if (stock.getQuantity() < stock.getMinimumStock())
            errors += "Cantitatea este sub stocul minim!\n";

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}