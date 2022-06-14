package by.bsu.soroka.lab.server.service.impl;

import by.bsu.soroka.lab.common.entity.Product;
import by.bsu.soroka.lab.common.entity.Stock;
import by.bsu.soroka.lab.common.entity.Storage;
import by.bsu.soroka.lab.common.service.ProductService;
import by.bsu.soroka.lab.common.service.StockService;
import by.bsu.soroka.lab.common.service.exception.ServiceException;
import by.bsu.soroka.lab.server.dao.DAOProvider;
import by.bsu.soroka.lab.server.dao.GeneralDAOProvider;
import by.bsu.soroka.lab.server.dao.exception.DAOException;
import by.bsu.soroka.lab.server.dao.interfaces.AdvancedProductDAO;
import by.bsu.soroka.lab.server.dao.interfaces.AdvancedStockDAO;
import by.bsu.soroka.lab.server.dao.interfaces.BasicDAO;
import lombok.extern.slf4j.Slf4j;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Basic implimentation of {@link StockService}
 * Access to data-storage obtained via {@link by.bsu.soroka.lab.server.dao.DAOProvider}.
 * @author Soroka Egor
 */
@Slf4j
public class StockServiceImpl extends BasicServiceImpl<Stock> implements StockService {
    private static DAOProvider daoProvider = GeneralDAOProvider.getDAOProvider();

    /**
     * Create {@link StorageServiceImpl};
     */
    public StockServiceImpl() {
        super(daoProvider.getStockDAO());
    }

    /**
     * Calculate total count of {@link Product} with <code>productID</code> inside <b>all</b> {@link Storage}'s.
     *
     * In case, {@link DAOProvider} implements {@link AdvancedProductDAO}, this implementation
     * will use {@link AdvancedStockDAO#getTotalProductCountByID(int) getTotalProductCountById}.
     * Otherwise, naive implantation via {@link BasicDAO} provided.
     * @param productID id of {@link Product}
     * @return Total count of {@link Product} with <code>productID</code>
     * @throws ServiceException in case some logcal/data-access errors.
     */
    @Override
    public int getTotalProductCountByID(int productID) throws ServiceException {
        try {
            if(basicDAO instanceof AdvancedStockDAO){
                AdvancedStockDAO advancedStockDAO = (AdvancedStockDAO) basicDAO;
                return advancedStockDAO.getTotalProductCountByID(productID);
            } else {
                log.debug("Getting total count for product with ID:{}", productID);
                List<Stock> stocks = getAll();

                int count = stocks
                        .stream()
                        .filter(stock -> {return stock.getProductID() == productID;})
                        .mapToInt(Stock::getCount)
                        .sum();

                log.debug("Total count of product: {} is {}",productID, count);
                return count;
            }
        } catch (DAOException | RemoteException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Calculate count of {@link Product} with <code>productID</code> inside {@link Storage} with <code>storageID</code>
     *
     * In case, {@link DAOProvider} implements {@link AdvancedProductDAO}, this implementation
     * will use {@link AdvancedStockDAO#getCount(int, int)}  getCount}.
     * Otherwise, naive implantation via {@link BasicDAO} provided.
     * @param productID Identifier of {@link Product}
     * @param storageID Identifier of {@link Storage}
     * @return count of {@link Product} <code>productID</code> inside {@link Storage} with <code>storageID</code>
     * @throws ServiceException in case some logcal/data-access errors.
     */
    @Override
    public int getCount(int productID, int storageID) throws ServiceException {
        try {
            if (basicDAO instanceof AdvancedStockDAO) {
                AdvancedStockDAO advancedStockDAO = (AdvancedStockDAO) basicDAO;
                return advancedStockDAO.getCount(productID, storageID);
            } else {
                log.debug("getting count for productID={} on storageID={}", productID, storageID);
                List<Stock> stocks = getAll();
                List<Stock> filteredStocks = stocks
                        .stream()
                        .filter(stock ->
                            stock.getProductID() == productID
                                    && stock.getStorageID() == storageID)
                        .collect(Collectors.toCollection(ArrayList::new));
                if(filteredStocks.isEmpty()){
                    Stock newStock = new Stock(productID, storageID, 0);
                    stocks.add(newStock);
                    add(newStock);
                }
                int count = filteredStocks.stream().mapToInt(Stock::getCount).sum();
                log.debug("Count of productID={}, on storageID={} is {}", productID, storageID, count);
                return count;

            }
        } catch (DAOException | RemoteException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
