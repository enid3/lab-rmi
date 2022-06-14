package by.bsu.soroka.lab.server.dao.xml.impl;

import by.bsu.soroka.lab.common.entity.Stock;
import by.bsu.soroka.lab.server.dao.exception.DAOException;
import by.bsu.soroka.lab.server.dao.json.impl.JSONCommonDAO;
import by.bsu.soroka.lab.server.dao.xml.connection.ConnectionProvider;
import by.bsu.soroka.lab.server.dao.xml.connection.DataContainer;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public class XMLStockDAO extends XMLCommonDAO<Stock> {
    private final static String TABLE_NAME       = "stock";


    public XMLStockDAO() {
        super(TABLE_NAME);
        try {
            this.dataContainer = new DataContainer<>();
            ConnectionProvider.getDataContainer(this.dataContainer, TABLE_NAME);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int add(Stock stock) throws DAOException {
        log.debug("Adding stock {}", stock);

        int id = 0;
        List<Stock> sameStocks = dataContainer
                .getData()
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
