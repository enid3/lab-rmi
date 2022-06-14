package by.bsu.soroka.lab.server.dao.json.impl;

import by.bsu.soroka.lab.common.entity.Product;
import by.bsu.soroka.lab.common.entity.Stock;
import by.bsu.soroka.lab.server.dao.GeneralDAOProvider;
import by.bsu.soroka.lab.server.dao.exception.DAOException;
import by.bsu.soroka.lab.server.dao.interfaces.BasicDAO;
import by.bsu.soroka.lab.server.dao.json.connection.ConnectionProvider;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class JSONCommonDAO<T> implements BasicDAO<T> {
    final static String ID = "id";
    final static String COUNTER = "counter";

    private String tableName;
    private String dataField;

    protected List<T> data;
    protected JSONArray dataJSON;
    protected int counter;

    protected boolean isCacheOutdated = true;
    protected boolean isCacheDirty = false;
    protected boolean isWriteBackAfterChange = true;

    protected boolean writeBackAfterChange = true;

    public List<T> parseJsonArray(JSONArray dataJSON) {
        log.debug("JSON array: {}", dataJSON);

        return dataJSON
                .toList()
                .stream()
                .map(hashMap -> new JSONObject((HashMap) hashMap))
                .peek(o -> {
                    System.out.println(o);
                })
                .map(jo -> unwarp(jo))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public abstract JSONObject warp(T t);

    public abstract T unwarp(JSONObject jsonObject);

    protected void updateCache() throws DAOException {
        log.debug("Updating cache ...");
        JSONObject jsonObject = ConnectionProvider.getJSONObject(tableName);

        counter = jsonObject.getInt(JSONCommonDAO.COUNTER);
        log.debug("couter {}", counter);
        dataJSON = jsonObject.getJSONArray(dataField);
        log.debug("dataJSON {}", dataJSON);
        data = parseJsonArray(dataJSON);
        log.debug("data {}", data);

        isCacheOutdated = false;
    }

    protected void writeBackCache() throws DAOException {
        log.debug("Writing back");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSONCommonDAO.COUNTER, counter);
        jsonObject.put(dataField, dataJSON);

        log.debug("JSONObject {}", jsonObject);
        ConnectionProvider.saveJSONObject(jsonObject, tableName);
        isCacheDirty = false;
    }

    public JSONCommonDAO(String tableName, String dataField) {
        this.tableName = tableName;
        this.dataField = dataField;
    }

    @Override
    public List<T> getAll() throws DAOException {
        log.debug("Getting all Products");

        if (isCacheOutdated) {
            updateCache();
        }

        return data;
    }

    @Override
    public int add(T t) throws DAOException {
        log.debug("Adding {}", t);
        log.debug("ID of {} is {}", t, counter);

        data.add(t);

        JSONObject warped = warp(t);
        if(warped.has(ID)) {
            warped.remove(ID);
        }
        warped.put(ID, counter++);
        dataJSON.put(warped);

        isCacheDirty = true;

        if(writeBackAfterChange) {
            writeBackCache();
        }

        return warped.getInt(ID);
    }

    @Override
    public boolean removeById(int id) throws DAOException {
        log.debug("Removing id {}", id);
        int index = -1;
        T t = null;

        for (int i = 0; i < dataJSON.length(); ++i) {
            JSONObject jsonProduct = (JSONObject) dataJSON.get(i);
            if (jsonProduct.getInt(JSONCommonDAO.ID) == id) {
                t = unwarp(jsonProduct);
                index = i;
                break;
            }
        }

        if (index != -1) {
            data.remove(t);
            dataJSON.remove(index);

            isCacheDirty = true;
            if (writeBackAfterChange) {
                writeBackCache();
            }
        }


        return index != -1;

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