package by.bsu.soroka.lab.server.dao.xml.impl;

import by.bsu.soroka.lab.common.entity.Storage;
import by.bsu.soroka.lab.server.dao.exception.DAOException;
import by.bsu.soroka.lab.server.dao.json.impl.JSONCommonDAO;
import by.bsu.soroka.lab.server.dao.xml.connection.ConnectionProvider;
import by.bsu.soroka.lab.server.dao.xml.connection.DataContainer;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

@Slf4j
public class XMLStorageDAO extends XMLCommonDAO<Storage> {
    private final static String TABLE_NAME   = "storage";

    public XMLStorageDAO() {
        super(TABLE_NAME);
        try {
            this.dataContainer = new DataContainer<>();
            ConnectionProvider.getDataContainer(this.dataContainer, TABLE_NAME);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}
