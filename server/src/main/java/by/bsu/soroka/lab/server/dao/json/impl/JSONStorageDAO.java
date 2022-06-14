package by.bsu.soroka.lab.server.dao.json.impl;

import by.bsu.soroka.lab.common.entity.Storage;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

@Slf4j
public class JSONStorageDAO extends JSONCommonDAO<Storage> {
    private final static String TABLE_NAME   = "storage";

    private final static String STORAGE_LIST_FIELD   = "storages";

    // Column names;
    private final static String NAME_FIELD = "name";

    public JSONStorageDAO() {
        super(TABLE_NAME, STORAGE_LIST_FIELD);
    }

    @Override
    public JSONObject warp(Storage storage) {
        JSONObject jsonStorage = new JSONObject();

        jsonStorage.put(JSONCommonDAO.ID, storage.getId());
        jsonStorage.put(NAME_FIELD, storage.getName());

        return jsonStorage;
    }

    @Override
    public Storage unwarp(JSONObject jsonObject) {
        return new Storage(
                jsonObject.getInt(JSONCommonDAO.ID),
                jsonObject.getString(NAME_FIELD)
        );
    }
}
