package by.bsu.soroka.lab.server.dao.json.impl;

import by.bsu.soroka.lab.common.entity.Stock;
import by.bsu.soroka.lab.server.dao.exception.DAOException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public class JSONStockDAO extends JSONCommonDAO<Stock> {
    private final static String TABLE_NAME       = "stock";

    private final static String STOCK_LIST_FIELD = "stocks";

    // Column names;
    private final static String PRODUCT_ID_FIELD = "product_id";
    private final static String STORAGE_ID_FIELD = "storage_id";
    private final static String COUNT_FIELD      = "count";

    public JSONStockDAO() {
        super(TABLE_NAME, STOCK_LIST_FIELD);
    }

    @Override
    public JSONObject warp(Stock stock) {
        JSONObject jsonStock = new JSONObject();

        jsonStock.put(JSONCommonDAO.ID, stock.getId());
        jsonStock.put(PRODUCT_ID_FIELD, stock.getProductID());
        jsonStock.put(STORAGE_ID_FIELD, stock.getStorageID());
        jsonStock.put(COUNT_FIELD, stock.getCount());

        return jsonStock;
    }

    @Override
    public Stock unwarp(JSONObject jsonObject) {
        return new Stock(
                jsonObject.getInt(JSONCommonDAO.ID),
                jsonObject.getInt(PRODUCT_ID_FIELD),
                jsonObject.getInt(STORAGE_ID_FIELD),
                jsonObject.getInt(COUNT_FIELD)
        );
    }

    @Override
    public int add(Stock stock) throws DAOException {
        log.debug("Adding stock {}", stock);

        int id = 0;
        List<Stock> sameStocks = data
                .stream()
                .filter(s -> s.getProductID() == stock.getProductID()
                        && s.getStorageID() == stock.getStorageID())
                .collect(Collectors.toList());

        if(sameStocks.isEmpty()) {
            log.debug("Creating new stock {}", stock);

            id = super.add(stock);
            log.debug("ID of {} was changed to {}", stock, id);
        } else {
            log.debug("Total same: {}", sameStocks.size());
            Stock sameStock = sameStocks.get(0);
            log.debug("Adding {} to existed {}", stock, sameStock);

            stock.setId(sameStock.getId());
            stock.setCount(sameStock.getCount() + stock.getCount());

            boolean wbac = super.isWriteBackAfterChange();
            setWriteBackAfterChange(false);
            super.removeById(stock.getId());
            super.add(stock);
            setCacheDirty(wbac);

            if(isWriteBackAfterChange()) {
                writeBackCache();
            }

        }
        return stock.getId();
    }
}
