package by.bsu.soroka.lab.server.service;

import by.bsu.soroka.lab.common.service.ProductService;
import by.bsu.soroka.lab.common.service.StockService;
import by.bsu.soroka.lab.common.service.StorageService;
import by.bsu.soroka.lab.server.service.impl.ProductServiceImpl;
import by.bsu.soroka.lab.server.service.impl.StockServiceImpl;
import by.bsu.soroka.lab.server.service.impl.StorageServiceImpl;

/**
 * @author Soroka Egor
 * {@link ServiceProvider} used for <b>>managing & proviceing</b> servicies implimentations.
 * @see by.bsu.soroka.lab.common.service
 */
public class ServiceProvider {
    private static final ServiceProvider instance = new ServiceProvider();

    private final ProductService productService = new ProductServiceImpl();
    private final StorageService storageService = new StorageServiceImpl();
    private final StockService stockService   = new StockServiceImpl();

    private ServiceProvider() {}

    /**
     * Provide instance of {@link ServiceProvider}.
     * @return instrance of {@link ServiceProvider}
     */
    public static ServiceProvider getInstance() {
        return instance;
    }

    /**
     * Return specified {@link ProductService} implementation.
     * @return {@link ProductService} implementation.
     */
    public ProductService getProductService() {
        return productService;
    }

    /**
     * Return specified {@link StorageService} implementation.
     * @return {@link StorageService} implementation.
     */
    public StorageService getStorageService() {
        return storageService;
    }

    /**
     * Return specified {@link StockService} implementation.
     * @return {@link StockService} implementation.
     */
    public StockService getStockService() {
        return stockService;
    }
}

