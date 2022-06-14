package by.bsu.soroka.lab.server.dao;

import by.bsu.soroka.lab.common.entity.Product;
import by.bsu.soroka.lab.common.entity.Stock;
import by.bsu.soroka.lab.common.entity.Storage;
import by.bsu.soroka.lab.server.dao.interfaces.BasicDAO;
import by.bsu.soroka.lab.server.dao.json.JSONDAOProvider;
import by.bsu.soroka.lab.server.dao.sqlite.SqliteDAOProvider;
import by.bsu.soroka.lab.server.dao.xml.XMLDAOProvider;

/**
 * @author Soroka Egor
 * Warper for specifyed {@link DAOProvider}.
 */
public class GeneralDAOProvider implements DAOProvider {
    private static final DAOProvider daoProvider = XMLDAOProvider.getInstance();

    private GeneralDAOProvider() {}

    /**
     * Get specified {@link DAOProvider}
     * @return specified {@link DAOProvider}
     */
    public static DAOProvider getDAOProvider() {
        return daoProvider;
    }

    /**
     * Get implementation of {@link BasicDAO} for {@link Product}.
     * In some can implement {@link by.bsu.soroka.lab.server.dao.interfaces.AdvancedStorageDAO}.
     * @return {@link BasicDAO} for {@link Product}
     * @see DAOProvider
     */
    public BasicDAO<Product> getProductDAO() { return daoProvider.getProductDAO(); }

    /**
     * Get implementation of {@link BasicDAO} for {@link Storage}.
     * In some can implement {@link by.bsu.soroka.lab.server.dao.interfaces.AdvancedStorageDAO}.
     * @return {@link BasicDAO} for {@link Storage}
     * @see DAOProvider
     */
    public BasicDAO<Storage> getStorageDAO() {
        return daoProvider.getStorageDAO();
    }

    /**
     * Get implementation of {@link BasicDAO} for {@link Stock}.
     * In some can implement {@link by.bsu.soroka.lab.server.dao.interfaces.AdvancedStockDAO}.
     * @return {@link BasicDAO} for {@link Stock}
     * @see DAOProvider
     */
    public BasicDAO<Stock> getStockDAO() {
        return daoProvider.getStockDAO();
    }
}
