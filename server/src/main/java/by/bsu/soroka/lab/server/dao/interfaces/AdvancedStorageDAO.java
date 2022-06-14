package by.bsu.soroka.lab.server.dao.interfaces;

import by.bsu.soroka.lab.common.entity.Stock;
import by.bsu.soroka.lab.server.dao.exception.DAOException;

/**
 * @author Soroka Egor
 * Specify some special operations, that can be implemented more efficently
 * on DAO-level.
 * @see BasicDAO
 */
public interface AdvancedStorageDAO {
    /**
     * <b>Cascade remove</b>  of {@link by.bsu.soroka.lab.common.entity.Storage} with <code>id</code> from data-storage.
     * This remove operation also removes {@link Stock} entities, if needed.
     * @param id Identifier of entity, that will be removed. See {@link by.bsu.soroka.lab.common.entity.Identifiable}
     * @return <b>true</b> in case, that entity was successfully removed, <b>false</b> otherwise.
     * @throws DAOException thrown in case of some data-access errors.
     * @see {@link by.bsu.soroka.lab.server.service.impl.StockServiceImpl#removeById(int)}
     */
    boolean cascadeRemoveByID(int id) throws DAOException;
}
