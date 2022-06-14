package by.bsu.soroka.lab.server.service.impl;

import by.bsu.soroka.lab.common.entity.Stock;
import by.bsu.soroka.lab.common.entity.Storage;
import by.bsu.soroka.lab.common.service.ProductService;
import by.bsu.soroka.lab.common.service.StockService;
import by.bsu.soroka.lab.common.service.StorageService;
import by.bsu.soroka.lab.common.service.exception.ServiceException;
import by.bsu.soroka.lab.server.dao.DAOProvider;
import by.bsu.soroka.lab.server.dao.GeneralDAOProvider;
import by.bsu.soroka.lab.server.dao.exception.DAOException;
import by.bsu.soroka.lab.server.dao.interfaces.AdvancedProductDAO;
import by.bsu.soroka.lab.server.dao.interfaces.AdvancedStorageDAO;
import by.bsu.soroka.lab.server.dao.interfaces.BasicDAO;
import by.bsu.soroka.lab.server.service.ServiceProvider;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Basic implimentation of {@link StorageService}
 * Access to data-storage obtained via {@link by.bsu.soroka.lab.server.dao.DAOProvider}.
 * @author Soroka Egor
 */
public class StorageServiceImpl extends BasicServiceImpl<Storage> implements StorageService {
    private static DAOProvider daoProvider = GeneralDAOProvider.getDAOProvider();

    /**
     * Create {@link StorageServiceImpl};
     */
    public StorageServiceImpl() {
        super(daoProvider.getStorageDAO());
    }

    /**
     * <b>Remove</b> {@link Storage} with <code>id</code> from data-storage.
     * This remove operation also removes {@link Stock} entities, if needed.
     * Access to data-storage obtained via {@link by.bsu.soroka.lab.server.dao.DAOProvider}.
     *
     * In case, {@link DAOProvider} implements {@link AdvancedProductDAO} this implementation
     * will use {@link AdvancedProductDAO#cascadeRemoveByID(int) cascadeRemoveByID}.
     * Otherwise, naive implimatation via {@link BasicDAO} provided.
     * @param id Identifier of entity, that will be removed. See {@link by.bsu.soroka.lab.common.entity.Identifiable}
     * @return <b>true</b> in case, that entity was successfully removed, <b>false</b> otherwise.
     * @throws ServiceException thrown in case, of same logical errors.
     * @throws RemoteException thrown in case of some connection errors.
     * @see BasicDAO
     */
    @Override
    public boolean removeById(int id) throws ServiceException, RemoteException {
        try {
            boolean res = false;
            if(basicDAO instanceof AdvancedProductDAO) {
                AdvancedStorageDAO advancedStorageDAO = (AdvancedStorageDAO)  basicDAO;
                res = advancedStorageDAO.cascadeRemoveByID(id);
            } else {
                StockService stockService = ServiceProvider.getInstance().getStockService();

                List<Stock> stocksForProduct = stockService.getAll()
                        .stream()
                        .filter(s -> s.getProductID() == id)
                        .collect(Collectors.toList());

                for (Stock stock : stocksForProduct) {
                    stockService.removeById(stock.getId());
                }

                res = basicDAO.removeById(id);
            }

            return res;
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
