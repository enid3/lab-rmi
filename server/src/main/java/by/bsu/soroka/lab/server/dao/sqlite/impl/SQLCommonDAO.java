package by.bsu.soroka.lab.server.dao.sqlite.impl;

import by.bsu.soroka.lab.server.dao.sqlite.connection.ConnectionProvider;
import by.bsu.soroka.lab.server.dao.exception.DAOException;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

@Slf4j
public class SQLCommonDAO {
    private final static String REMOVE_BY_COLUMN_TEMPLATE = "DELETE FROM {0} WHERE {1}=?";
    private final static String GET_ALL_TEMPLATE = "SELECT * FROM {0}";

    public static int deleteByColumnName(String tableName, String columnName, int value) throws DAOException {
        String REMOVE_BY_COLUMN = MessageFormat.format(REMOVE_BY_COLUMN_TEMPLATE, tableName, columnName);
        log.debug("REMOVE_BY_COLUMN: {}", REMOVE_BY_COLUMN);

        Connection connection = ConnectionProvider.getInstance().getConnection();

        int deletedCount = 0;
        try {
            log.debug("Removing in tableName:{}, columnName:{},  value={}",tableName, columnName, value);

            PreparedStatement ps = connection.prepareStatement(REMOVE_BY_COLUMN);
            ps.setInt(1, value);

            log.debug("Executing statement: {}", ps.getParameterMetaData());

            deletedCount = ps.executeUpdate();
            log.debug("In tableName:{} deleted {} strings", value, deletedCount);
        }
        catch (SQLException e) {
            throw new DAOException(e);
        }
        return deletedCount;

    }

    public static ResultSet getAll(String tableName) throws DAOException {
        String GET_ALL = MessageFormat.format(GET_ALL_TEMPLATE, tableName);

        Connection connection = ConnectionProvider.getInstance().getConnection();
        ResultSet rs = null;
        try {
            log.debug("Getting all for tableName:{}", tableName);
            log.debug("GET_ALL statement {}", GET_ALL);

            PreparedStatement ps = connection.prepareStatement(GET_ALL);

            rs = ps.executeQuery();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return rs;
    }
}