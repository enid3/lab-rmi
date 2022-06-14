package by.bsu.soroka.lab.server.dao.sqlite.impl;

import by.bsu.soroka.lab.common.entity.Storage;
import by.bsu.soroka.lab.server.dao.interfaces.BasicDAO;
import by.bsu.soroka.lab.server.dao.sqlite.connection.ConnectionProvider;
import by.bsu.soroka.lab.server.dao.exception.DAOException;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SQLStorageDAO implements BasicDAO<Storage> {
    private final static String TABLE_NAME         = "storage"; // {0}

    // Column names;
    // |ID_COLUMN|NAME_COLUMN|
    private final static String ID_COLUMN          = "id";      // {1}
    private final static String NAME_COLUMN        = "name";    // {2}

    // SQL requests

    private final String ADD = MessageFormat.format(
            "INSERT INTO {0} ({2}) VALUES(?)",
            TABLE_NAME, ID_COLUMN, NAME_COLUMN);

    public static List<Storage> parseResultSetToStorages(ResultSet rs) throws SQLException {
        log.debug("Result set: {}", rs);
        List<Storage> storages = new ArrayList<>();
        while (rs.next()) {
            storages.add(new Storage(
                    rs.getInt(ID_COLUMN),
                    rs.getString(NAME_COLUMN)
                ));
        }
        log.debug("Result list: {}", storages);
        return storages;

    }
    @Override
    public List<Storage> getAll() throws DAOException {
        Connection connection = ConnectionProvider.getInstance().getConnection();
        List<Storage> list = new ArrayList<>();
        try {
            log.debug("Getting all Storages");
            list = parseResultSetToStorages(SQLCommonDAO.getAll(TABLE_NAME));

        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return list;
    }

    @Override
    public int add(Storage storage) throws DAOException {
        Connection connection = ConnectionProvider.getInstance().getConnection();
        try {
            log.debug("Adding storage {}", storage);
            log.debug("ADD: {}", ADD);

            PreparedStatement ps = connection.prepareStatement(ADD);
            ps.setString(1, storage.getName());

            log.debug("Executing statement: {}", ps.getParameterMetaData());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            log.debug("Result set: {}", rs);

            int id = 0;
            while (rs.next()) {
                id = rs.getInt(1);
            }
            storage.setId(id);
            log.debug("ID of {} was chagned to {}", storage, id);
            return id;

        } catch (SQLException e) {
            throw new DAOException(e);
        }

    }

    @Override
    public boolean removeById(int id) throws DAOException {
        return SQLCommonDAO.deleteByColumnName(TABLE_NAME, ID_COLUMN, id) > 0;
    }

}
