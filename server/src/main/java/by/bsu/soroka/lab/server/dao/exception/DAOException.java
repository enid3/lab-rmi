package by.bsu.soroka.lab.server.dao.exception;

/**
 * @author Soroka Egor
 * Base DAO-layer exception, used by {@link by.bsu.soroka.lab.server.dao.interfaces.BasicDAO},
 * {@link by.bsu.soroka.lab.server.dao.interfaces.AdvancedStockDAO}, etc.
 */
public class DAOException extends Exception {
    /**
     * Construct exception
     * @param e exception.
     */
    public DAOException(Exception e){super(e);}

    /**
     * Construct exception
     * @param message exception message.
     */
    public DAOException(String message){super(message);}
}
