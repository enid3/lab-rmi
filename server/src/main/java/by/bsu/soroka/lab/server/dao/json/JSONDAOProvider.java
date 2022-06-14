package by.bsu.soroka.lab.server.dao.json;

import by.bsu.soroka.lab.common.entity.Product;
import by.bsu.soroka.lab.common.entity.Stock;
import by.bsu.soroka.lab.common.entity.Storage;
import by.bsu.soroka.lab.server.dao.DAOProvider;
import by.bsu.soroka.lab.server.dao.interfaces.BasicDAO;
import by.bsu.soroka.lab.server.dao.json.impl.JSONProductDAO;
import by.bsu.soroka.lab.server.dao.json.impl.JSONStockDAO;
import by.bsu.soroka.lab.server.dao.json.impl.JSONStorageDAO;

public class JSONDAOProvider implements DAOProvider {
    private static final JSONDAOProvider instance = new JSONDAOProvider();

    private final BasicDAO<Product> productDAO  = new JSONProductDAO();
    private final BasicDAO<Storage> storageDAO  = new JSONStorageDAO();
    private final BasicDAO<Stock> stockDAO      = new JSONStockDAO();

    private JSONDAOProvider() {}

    public static JSONDAOProvider getInstance() {
        return instance;
    }

    public BasicDAO<Product> getProductDAO() { return productDAO; }

    public BasicDAO<Storage> getStorageDAO() {
        return storageDAO;
    }

    public BasicDAO<Stock> getStockDAO() {
        return stockDAO;
    }
}
