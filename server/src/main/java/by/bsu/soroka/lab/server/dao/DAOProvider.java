package by.bsu.soroka.lab.server.dao;

import by.bsu.soroka.lab.common.entity.Product;
import by.bsu.soroka.lab.common.entity.Stock;
import by.bsu.soroka.lab.common.entity.Storage;
import by.bsu.soroka.lab.server.dao.interfaces.BasicDAO;

/**
 * Provide access to data-storage
 * @author Soroka Egor
 */
public interface DAOProvider {
    /**
     * Get implementation of {@link BasicDAO} for {@link Product}.
     * In some can implement {@link by.bsu.soroka.lab.server.dao.interfaces.AdvancedStorageDAO}.
     * @return {@link BasicDAO} for {@link Product}
     */
    BasicDAO<Product> getProductDAO();

    /**
     * Get implementation of {@link BasicDAO} for {@link Storage}.
     * In some can implement {@link by.bsu.soroka.lab.server.dao.interfaces.AdvancedStorageDAO}.
     * @return {@link BasicDAO} for {@link Storage}
     */
    BasicDAO<Storage> getStorageDAO();

    /**
     * Get implementation of {@link BasicDAO} for {@link Stock}.
     * In some can implement {@link by.bsu.soroka.lab.server.dao.interfaces.AdvancedStockDAO}.
     * @return {@link BasicDAO} for {@link Stock}
     */
    BasicDAO<Stock> getStockDAO();

}
