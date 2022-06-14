package by.bsu.soroka.lab.server.server;

import by.bsu.soroka.lab.common.service.ProductService;
import by.bsu.soroka.lab.common.service.StockService;
import by.bsu.soroka.lab.common.service.StorageService;
import by.bsu.soroka.lab.server.service.ServiceProvider;
import lombok.extern.slf4j.Slf4j;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Soroka Egor
 */
@Slf4j
public class Server {
    /**
     * Default host parameter for {@link java.rmi.registry.LocateRegistry#createRegistry(int)}
     * {@link Server#PORT }
     */
    public static final String HOST = "127.0.0.1";

    /**
     * Default port parameter for {@link java.rmi.registry.LocateRegistry#createRegistry(int)}
     * {@link Server#HOST}
     */
    public static final int PORT = 6443;
    private static final String PRODUCT_SERVICE_NAME = "product";
    private static final String STORAGE_SERVICE_NAME = "storage";
    private static final String STOCK_SERVICE_NAME = "stock";

    /**
     * Run <b>server-side</b> application.
     * @param args Arguments for function.
     */
    public static void main(String[] args) {

        try {
            ProductService productServiceStub = (ProductService) UnicastRemoteObject
                    .exportObject(ServiceProvider.getInstance().getProductService(), 0);
            StorageService storageServiceStub = (StorageService) UnicastRemoteObject
                    .exportObject(ServiceProvider.getInstance().getStorageService(), 0);
            StockService stockServiceStub = (StockService) UnicastRemoteObject
                    .exportObject(ServiceProvider.getInstance().getStockService(), 0);

            Registry registry = LocateRegistry.createRegistry(PORT);
            registry.rebind(PRODUCT_SERVICE_NAME, productServiceStub);
            log.debug("{} rebinded", PRODUCT_SERVICE_NAME);

            registry.rebind(STORAGE_SERVICE_NAME, storageServiceStub);
            log.debug("{} rebinded", STORAGE_SERVICE_NAME);

            registry.rebind(STOCK_SERVICE_NAME, stockServiceStub);
            log.debug("{} rebinded", STOCK_SERVICE_NAME);

            log.debug("server successfully stared");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
