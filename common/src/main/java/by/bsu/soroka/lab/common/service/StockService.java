package by.bsu.soroka.lab.common.service;

import by.bsu.soroka.lab.common.entity.Product;
import by.bsu.soroka.lab.common.entity.Stock;
import by.bsu.soroka.lab.common.entity.Storage;
import by.bsu.soroka.lab.common.service.exception.ServiceException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Soroka Egor
 * Service, that describes operations with {@link Stock} data.
 * See {@link BasicService}
 */
public interface StockService extends Remote, BasicService<Stock> {
    /**
     * Calculate total count of {@link Product} with <code>productID</code> inside <b>all</b> {@link Storage}'s.
     * @param productID id of {@link Product}
     * @return Total count of {@link Product} with <code>productID</code>
     */
    int getTotalProductCountByID(int productID) throws ServiceException, RemoteException;

    /**
     * Calculate count of {@link Product} with <code>productID</code> inside {@link Storage} with <code>storageID</code>
     * @param productID Identifier of {@link Product}
     * @param storageID Identifier of {@link Storage}
     * @return count of {@link Product} <code>productID</code> inside {@link Storage} with <code>storageID</code>
     */
    int getCount(int productID, int storageID) throws ServiceException, RemoteException;
}
