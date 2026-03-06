package drinkshop.repository.file;

import drinkshop.domain.Stock;

public class FileStockRepository
        extends FileAbstractRepository<Integer, Stock> {

    public FileStockRepository(String fileName) {
        super(fileName);
        loadFromFile();
    }

    @Override
    protected Integer getId(Stock entity) {
        return entity.getId();
    }

    @Override
    protected Stock extractEntity(String line) {
        String[] elems = line.split(";");

        int id = Integer.parseInt(elems[0]);
        String ingredient = elems[1];
        double quantity = Double.parseDouble(elems[2]);
        double minimumStock = Double.parseDouble(elems[3]);

        return new Stock(id, ingredient, quantity, minimumStock);
    }

    @Override
    protected String createEntityAsString(Stock entity) {
        return entity.getId() + ";" +
                entity.getIngredient() + ";" +
                entity.getQuantity() + ";" +
                entity.getMinimumStock();
    }
}