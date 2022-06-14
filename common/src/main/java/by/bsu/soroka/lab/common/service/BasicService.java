package by.bsu.soroka.lab.common.service;

import by.bsu.soroka.lab.common.service.exception.ServiceException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Soroka Egor
 * Used to avoid cope repetition, describes basic <b>data-access functions</b>
 * @param <T> One of entities. See {@link by.bsu.soroka.lab.common.entity.Product},
 *           {@link by.bsu.soroka.lab.common.entity.Stock},
 *           {@link by.bsu.soroka.lab.common.entity.Storage}.
 */
public interface BasicService<T> extends Remote {
    /**
     * Returns all entities of {@link T} from data-storage.
     * Guaranteed, that return-type is {@link java.io.Serializable}
     * @return all entities of {@link T}
     * @throws ServiceException thrown in case, of same logical errors.
     * @throws RemoteException thrown in case of some connection errors.
     */
    List<T> getAll() throws ServiceException, RemoteException;

    /**
     * <b>Add</b> entity <code>t</code> to data-storage.
     * @param t entity, that will be added.
     * @return Identifier of newly added entity. See {@link by.bsu.soroka.lab.common.entity.Identifiable}
     * @throws ServiceException thrown in case, of same logical errors.
     * @throws RemoteException thrown in case of some connection errors.
     */
    int add(T t) throws ServiceException, RemoteException;

    /**
     * <b>Remove</b> entity  from data-storage.
     * @param id Identifier of entity, that will be removed. See {@link by.bsu.soroka.lab.common.entity.Identifiable}
     * @return <b>true</b> in case, that entity was successfully removed, <b>false</b> otherwise.
     * @throws ServiceException thrown in case, of same logical errors.
     * @throws RemoteException thrown in case of some connection errors.
     */
    boolean removeById(int id) throws ServiceException, RemoteException;
}
