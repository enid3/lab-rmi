package by.bsu.soroka.lab.server.dao.json.impl;

import by.bsu.soroka.lab.common.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JSONProductDAO extends JSONCommonDAO<Product> {

    private final static String TABLE_NAME = "product";

    private final static String PRODUCT_LIST_FIELD = "products";
    private final static String NAME_FILED = "name";
    private final static String PRICE_FIELD = "price";

    public JSONProductDAO() {
        super(TABLE_NAME, PRODUCT_LIST_FIELD);
    }

    @Override
    public JSONObject warp(Product product) {
        JSONObject jsonProduct = new JSONObject();

        jsonProduct.put(JSONCommonDAO.ID, product.getId());
        jsonProduct.put(NAME_FILED, product.getName());
        jsonProduct.put(PRICE_FIELD, product.getPrice());

        return jsonProduct;
    }

    @Override
    public Product unwarp(JSONObject jsonObject) {
        return new Product(
                jsonObject.getInt(JSONCommonDAO.ID),
                jsonObject.getString(NAME_FILED),
                jsonObject.getInt(PRICE_FIELD)
        );
    }
}
