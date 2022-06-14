package by.bsu.soroka.lab.server.dao.xml;

import by.bsu.soroka.lab.common.entity.Product;
import by.bsu.soroka.lab.common.entity.Stock;
import by.bsu.soroka.lab.common.entity.Storage;
import by.bsu.soroka.lab.server.dao.DAOProvider;
import by.bsu.soroka.lab.server.dao.exception.DAOException;
import by.bsu.soroka.lab.server.dao.interfaces.BasicDAO;
import by.bsu.soroka.lab.server.dao.json.impl.JSONProductDAO;
import by.bsu.soroka.lab.server.dao.json.impl.JSONStockDAO;
import by.bsu.soroka.lab.server.dao.json.impl.JSONStorageDAO;
import by.bsu.soroka.lab.server.dao.xml.impl.XMLProductDAO;
import by.bsu.soroka.lab.server.dao.xml.impl.XMLStockDAO;
import by.bsu.soroka.lab.server.dao.xml.impl.XMLStorageDAO;

public class XMLDAOProvider implements DAOProvider {
    private static XMLDAOProvider instance = new XMLDAOProvider();

    private final BasicDAO<Product> productDAO  = new XMLProductDAO();
    private final BasicDAO<Storage> storageDAO  = new XMLStorageDAO();
    private final BasicDAO<Stock>   stockDAO    = new XMLStockDAO();

    private XMLDAOProvider() {}

    public static XMLDAOProvider getInstance() { return instance; }

    public BasicDAO<Product> getProductDAO() { return productDAO; }

    public BasicDAO<Storage> getStorageDAO() {
        return storageDAO;
    }

    public BasicDAO<Stock> getStockDAO() {
        return stockDAO;
    }
}
