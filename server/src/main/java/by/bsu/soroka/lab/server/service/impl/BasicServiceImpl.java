package by.bsu.soroka.lab.server.service.impl;

import by.bsu.soroka.lab.common.entity.Product;
import by.bsu.soroka.lab.common.service.BasicService;
import by.bsu.soroka.lab.common.service.exception.ServiceException;
import by.bsu.soroka.lab.server.dao.exception.DAOException;
import by.bsu.soroka.lab.server.dao.interfaces.BasicDAO;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to avoid cope repetition, describes basic <b>data-access functions</b>
 * Access to storage obtained via {@link by.bsu.soroka.lab.server.dao.DAOProvider}.
 * @param <T> One of entities. See {@link by.bsu.soroka.lab.common.entity.Product},
 *           {@link by.bsu.soroka.lab.common.entity.Stock},
 *           {@link by.bsu.soroka.lab.common.entity.Storage}.
 * @see BasicService
 * @author Soroka Egor
 */
public class BasicServiceImpl<T> implements BasicService<T> {
    protected BasicDAO<T> basicDAO;

    BasicServiceImpl(BasicDAO<T> basicDAO) {
        this.basicDAO = basicDAO;
    }

    /**
     * Returns all entities of {@link T} from data-storage.
     * Access to storage obtained via {@link by.bsu.soroka.lab.server.dao.DAOProvider}.
     * Guaranteed, that return-type is {@link java.io.Serializable}
     * @return all entities of {@link T}
     * @throws ServiceException thrown in case, of same logical errors.
     * @throws RemoteException thrown in case of some connection errors.
     * @see BasicService
     */
    @Override
    public List<T> getAll() throws ServiceException, RemoteException {
        List<T> data = new ArrayList<>();
        try {
            data.addAll(basicDAO.getAll());
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }

        return data;
    }

    /**
     * <b>Add</b> entity <code>t</code> to data-storage.
     * Access to storage obtained via {@link by.bsu.soroka.lab.server.dao.DAOProvider}.
     * @param t entity, that will be added.
     * @return Identifier of newly added entity. See {@link by.bsu.soroka.lab.common.entity.Identifiable}
     * @throws ServiceException thrown in case, of same logical errors.
     * @throws RemoteException thrown in case of some connection errors.
     * @see BasicService
     */
    @Override
    public int add(T t) throws ServiceException, RemoteException {
        try {
            return basicDAO.add(t);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * <b>Remove</b> entity  from data-storage.
     * Access to storage obtained via {@link by.bsu.soroka.lab.server.dao.DAOProvider}.
     * @param id Identifier of entity, that should be added. See {@link by.bsu.soroka.lab.common.entity.Identifiable}
     * @return <b>true</b> in case, that entity was successfully removed, <b>false</b> otherwise.
     * @throws ServiceException thrown in case, of same logical errors.
     * @throws RemoteException thrown in case of some connection errors.
     * @see BasicService
     */
    @Override
    public boolean removeById(int id) throws ServiceException, RemoteException {
        try {
            return basicDAO.removeById(id);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
