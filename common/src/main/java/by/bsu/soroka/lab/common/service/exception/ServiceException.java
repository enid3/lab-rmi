package by.bsu.soroka.lab.common.service.exception;

/**
 * @author Soroka Egor
 * Base Service-layer exception, used by {@link by.bsu.soroka.lab.common.service.BasicService},
 * {@link by.bsu.soroka.lab.common.service.ProductService}, etc.
 */
public class ServiceException extends Exception {
    private static final long serialVersionUID = -5476104233135309142L;

    /**
     * Construct exception
     * @param e exception message.
     */
    public ServiceException(String e){
        super(e);
    }
}
