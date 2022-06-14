package by.bsu.soroka.lab.server.dao.xml.impl;

import by.bsu.soroka.lab.common.entity.Identifiable;
import by.bsu.soroka.lab.server.dao.exception.DAOException;
import by.bsu.soroka.lab.server.dao.interfaces.BasicDAO;
import by.bsu.soroka.lab.server.dao.xml.connection.ConnectionProvider;
import by.bsu.soroka.lab.server.dao.xml.connection.DataContainer;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.util.List;

@Slf4j
public abstract class XMLCommonDAO<T extends Identifiable> implements BasicDAO<T> {
    private String tableName;

    protected DataContainer<T> dataContainer;
    protected int counter;

    protected boolean isCacheOutdated = true;
    protected boolean isCacheDirty = false;
    protected boolean isWriteBackAfterChange = true;

    protected boolean writeBackAfterChange = true;

    protected void updateCache() throws DAOException {
        log.debug("Updating cache ...");
        ConnectionProvider.getDataContainer(dataContainer, tableName);

        log.debug("counter {}", dataContainer.getCounter());
        log.debug("data {}", dataContainer.getData());

        isCacheOutdated = false;
    }

    protected void writeBackCache() throws DAOException {
        log.debug("Writing back");
        ConnectionProvider.saveDataContainer(dataContainer, tableName);

        isCacheDirty = false;
    }

    public XMLCommonDAO(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public List<T> getAll() throws DAOException {
        log.debug("Getting all Products");

        if (isCacheOutdated) {
            updateCache();
        }

        return dataContainer.getData();
    }

    @Override
    public int add(T t) throws DAOException {
        log.debug("Adding {}", t);
        log.debug("ID of {} is {}", t, counter);

        int id = dataContainer.add(t);
        t.setId(id);

        isCacheDirty = true;

        if(writeBackAfterChange) {
            writeBackCache();
        }

        return id;
    }

    @Override
    public boolean removeById(int id) throws DAOException {
        log.debug("Removing id {}", id);

        T toDelete = dataContainer
                .getData()
                .stream()
                .filter(t -> t.getId() == id)
                .limit(1)
                .findFirst()
                .orElse(null);
        if(toDelete != null) {
            dataContainer.getData().remove(toDelete);
        }

        isCacheDirty = true;
        if (writeBackAfterChange) {
            writeBackCache();
        }

        return toDelete != null;
    }

    public boolean isCacheOutdated() {
        return isCacheOutdated;
    }

    public void setCacheOutdated(boolean cacheOutdated) {
        isCacheOutdated = cacheOutdated;
    }

    public boolean isCacheDirty() {
        return isCacheDirty;
    }

    public void setCacheDirty(boolean cacheDirty) {
        isCacheDirty = cacheDirty;
    }

    public boolean isWriteBackAfterChange() {
        return isWriteBackAfterChange;
    }

    public void setWriteBackAfterChange(boolean writeBackAfterChange) {
        isWriteBackAfterChange = writeBackAfterChange;
    }

}