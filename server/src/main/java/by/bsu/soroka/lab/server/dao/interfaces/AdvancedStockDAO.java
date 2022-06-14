package by.bsu.soroka.lab.server.dao.interfaces;

import by.bsu.soroka.lab.common.entity.Product;
import by.bsu.soroka.lab.common.entity.Storage;
import by.bsu.soroka.lab.server.dao.exception.DAOException;

/**
 * @author Soroka Egor
 * Specify some special operations, that can be implemented more efficently
 * on DAO-level.
 * @see BasicDAO
 */
public interface AdvancedStockDAO {
    /**
     * Calculate total count of {@link Product} with <code>productID</code> inside <b>all</b> {@link Storage}'s.
     * Return a total count of {@link Product} inside all {@link Storage}'s.
     * @param productID id of {@link Product}
     * @return Total count of {@link Product} with <code>productID</code>
     * @throws DAOException thrown in case of some data-access errors.
     * @see by.bsu.soroka.lab.server.service.impl.StockServiceImpl#getTotalProductCountByID(int) 
     */
    int getTotalProductCountByID(int productID) throws DAOException;

    /**
     * Calculate count of {@link Product} with <code>productID</code> inside {@link Storage} with <code>storageID</code>
     *
     * @param productID Identifier of {@link Product}
     * @param storageID Identifier of {@link Storage}
     * @return count of {@link Product} <code>productID</code> inside {@link Storage} with <code>storageID</code>
     * @throws DAOException thrown in case of some data-access errors.
     * @see by.bsu.soroka.lab.server.service.impl.StockServiceImpl#getCount(int, int) 
     */
    int getCount(int productID, int storageID) throws DAOException;
}
