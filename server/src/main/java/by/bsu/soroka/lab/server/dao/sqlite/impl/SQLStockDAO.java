package by.bsu.soroka.lab.server.dao.sqlite.impl;

import by.bsu.soroka.lab.common.entity.Stock;
import by.bsu.soroka.lab.server.dao.interfaces.AdvancedStockDAO;
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
public class SQLStockDAO implements BasicDAO<Stock>, AdvancedStockDAO {
    private final static String TABLE_NAME          = "stock";      // {0}

    // Column names;
    // |ID_COLUMN|PRODUCT_ID_COLUMN|STORAGE_ID_COLUMN|COUNT_COLUMN|
    private final static String ID_COLUMN           = "id";         // {1}
    private final static String PRODUCT_ID_COLUMN   = "product_id"; // {2}
    private final static String STORAGE_ID_COLUMN   = "storage_id"; // {3}
    private final static String COUNT_COLUMN        = "count";      // {4}

    private final static String ADD = MessageFormat.format(
            "INSERT INTO {0} ({2}, {3}, {4}) VALUES(?,?,?)",
            TABLE_NAME, ID_COLUMN, PRODUCT_ID_COLUMN, STORAGE_ID_COLUMN, COUNT_COLUMN);

    private final static String UPDATE_STOCK = MessageFormat.format(
            "UPDATE {0} " +
                    "SET {4} = {4}+? " +
                    "WHERE {2}=? AND {3}=?",
            TABLE_NAME, ID_COLUMN, PRODUCT_ID_COLUMN, STORAGE_ID_COLUMN, COUNT_COLUMN);

    private final static String GET_COUNT = MessageFormat.format(
            "SELECT coalesce(MAX({4}), 0) as {4} FROM {0} " +
                    "WHERE {2}=? AND {3}=?",
            TABLE_NAME, ID_COLUMN, PRODUCT_ID_COLUMN, STORAGE_ID_COLUMN, COUNT_COLUMN);

    private final static String GET_TOTAL_COUNT_OF_PRODUCT_ID = MessageFormat.format(
            "SELECT {2}, SUM({4}) as {4} " +
                    "FROM {0} WHERE {2}=?",
            TABLE_NAME,ID_COLUMN, PRODUCT_ID_COLUMN, STORAGE_ID_COLUMN, COUNT_COLUMN);


    public static List<Stock> parseResultSetToStocks(ResultSet rs) throws SQLException {
        log.debug("Result set: {}", rs);
        List<Stock> stocks = new ArrayList<>();
        while (rs.next()) {
            stocks.add(new Stock(
                    rs.getInt(ID_COLUMN),
                    rs.getInt(PRODUCT_ID_COLUMN),
                    rs.getInt(STORAGE_ID_COLUMN),
                    rs.getInt(COUNT_COLUMN)
            ));
        }
        log.debug("Result list: {}", stocks);
        return stocks;
    }

    @Override
    public List<Stock> getAll() throws DAOException {
        Connection connection = ConnectionProvider.getInstance().getConnection();
        List<Stock> list = new ArrayList<>();
        try {
            log.debug("Getting all Products");
            list = parseResultSetToStocks(SQLCommonDAO.getAll(TABLE_NAME));

        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return list;
    }

    @Override
    public int add(Stock stock) throws DAOException {
        Connection connection = ConnectionProvider.getInstance().getConnection();
        try {
            log.debug("Adding product {}", stock);
            log.debug("Updating: {}", UPDATE_STOCK);

            PreparedStatement ps = connection.prepareStatement(UPDATE_STOCK);
            ps.setInt(1, stock.getCount());
            ps.setInt(2, stock.getProductID());
            ps.setInt(3, stock.getStorageID());

            log.debug("Executing statement: {}", ps.getParameterMetaData());
            int updatedCount = ps.executeUpdate();
            ResultSet rs;

            if(updatedCount == 0){
                ps = connection.prepareStatement(ADD);
                ps.setInt(1, stock.getProductID());
                ps.setInt(2, stock.getStorageID());
                ps.setInt(3, stock.getCount());

                log.debug("Executing statement: {}", ps.getParameterMetaData());
                ps.executeUpdate();
            }

            rs = ps.getGeneratedKeys();
            log.debug("Result set: {}", rs);

            int id = 0;
            while (rs.next()) {
                id = rs.getInt(1);
            }
            stock.setId(id);
            log.debug("ID of {} was chagned to {}", stock, id);
            return id;

        } catch (SQLException e) {
            throw new DAOException(e);
        }

    }

    @Override
    public boolean removeById(int id) throws DAOException {
        return SQLCommonDAO.deleteByColumnName(TABLE_NAME, ID_COLUMN, id) > 0;
    }

    @Override
    public int getTotalProductCountByID(int productID) throws DAOException {
        Connection connection = ConnectionProvider.getInstance().getConnection();
        int count = 0;

        try {
            log.debug("Getting total count for product with ID:{}", productID);
            log.debug("GET_TOTAL_COUNT_OF_PRODUCT_ID:", GET_TOTAL_COUNT_OF_PRODUCT_ID);

            PreparedStatement ps = connection.prepareStatement(GET_TOTAL_COUNT_OF_PRODUCT_ID);
            ps.setInt(1, productID);
            log.debug("Prepared statement: {}", ps);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt(COUNT_COLUMN);
                log.debug("Total count of product: {} is {}",productID, count);
            }

        }
        catch (SQLException e){
            throw new DAOException(e);
        }
        return count;
    }

    @Override
    public int getCount(int productID, int storageID) throws DAOException {
        Connection connection = ConnectionProvider.getInstance().getConnection();
        try {
            log.debug("getting count for productID={} on storageID={}", productID, storageID);
            log.debug("Updating: {}", GET_COUNT);

            PreparedStatement ps = connection.prepareStatement(GET_COUNT);
            ps.setInt(1, productID);
            ps.setInt(2, storageID);

            log.debug("Executing statement: {}", ps.getParameterMetaData());
            ResultSet rs = ps.executeQuery();

            log.debug("Result set: {}", rs);

            int count = 0;
            while (rs.next()) {
                count = rs.getInt(COUNT_COLUMN);
                log.debug("Count of productID={}, on storageID={} is {}",productID, storageID, count);
            }

            return count;


        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }
}
