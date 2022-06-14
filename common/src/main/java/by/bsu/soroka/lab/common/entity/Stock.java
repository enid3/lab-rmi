package by.bsu.soroka.lab.common.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Soroka Egor
 * Class describes stock, <i>aka relation between {@link Product} and {@link Storage}</i>, via {@link Stock#id},{@link Stock#productID},{@link Stock#storageID}.
 * Class must be {@link Serializable}
 */
public class Stock implements Serializable, Identifiable {
    private static final long serialVersionUID = -2844985797010674471L;
    private int id;
    private int productID;
    private int storageID;
    private int count;

    /**
     * Construct's {@link Stock}.
     * @param id identifier of {@link Stock}
     * @param productID identifier of {@link Product} object
     * @param storageID identifier of {@link Storage} object
     * @param count count of {@link Product} with {@link Stock#productID} on {@link Storage} with id {@link Stock#storageID}, <b>must be positive </b>
     */
    public Stock(int id, int productID, int storageID, int count) {
        this.id = id;
        this.productID = productID;
        this.storageID = storageID;
        this.count = count;
    }

    /**
     * Construct's empty {@link Stock}.
     * Data can be specified via <b>getter & setters</b>
     */
    public Stock(){}

    /**
     * Construct's {@link Stock} with {@link Stock#id}=0
     * Equals to run {@link Stock#Stock(int, int, int)}  Stock(0, productID, storageID, count)}
     * @param productID identifier of {@link Product} object
     * @param storageID identifier of {@link Storage} object
     * @param count count of {@link Product} with {@link Stock#productID} on {@link Storage} with id {@link Stock#storageID}, <b>must be positive </b>
     */
    public Stock(int productID, int storageID, int count) {
        this(0, productID, storageID, count);
    }

    /**
     * See {@link Identifiable#getId()} for more info.
     * @return id identifier if {@link Stock}
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Return {@link Product} identifier
     * See {@link Product#getId()} for more info.
     * @return id identifier of {@link Product}, counted by this {@link Stock}
     */
    public int getProductID() {
        return productID;
    }

    /**
     * Return {@link Storage} identifier
     * See {@link Storage#getId()} for more info.
     * @return id identifier of {@link Storage}, counted by this {@link Stock}
     */
    public int getStorageID() {
        return storageID;
    }

    /**
     * Getter for {@link Stock#count}
     * @return count of {@link Product} with {@link Stock#productID} on {@link Storage} with id {@link Stock#storageID}
     */
    public int getCount() {
        return count;
    }

    /**
     * See {@link Identifiable#setId(int)} for more info.
     * @param id set new identifier.
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter for {@link Stock#productID} field.
     * Identifier, for which {@link Stock#count} specified.
     * @param productID returns identifier of {@link Product}, see {@link Product#getId()}
     */
    public void setProductID(int productID) {
        this.productID = productID;
    }

    /**
     * Setter for {@link Stock#storageID} field.
     * Identifier, for which {@link Stock#count} specified.
     * @param storageID returns identifier of {@link Storage}, see {@link Storage#getId()}
     */
    public void setStorageID(int storageID) {
        this.storageID = storageID;
    }

    /**
     * Setter for {@link Stock#count} field.
     * @param count Count of {@link Product} with {@link Stock#productID} on {@link Storage} with id {@link Stock#storageID}
     */
    public void setCount(int count) {
        this.count = count;
    }
    /**
     * Check equals for {@link Stock} entities;
     * @param o object to check
     * @return <b>true</b> if {@link Stock#id}, {@link Stock#productID}, {@link Stock#storageID}, {@link Stock#count} are equals.
     * <b>false</b> otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return id == stock.id && productID == stock.productID && storageID == stock.storageID && count == stock.count;
    }

    /**
     * @return hashCode of {@link Product}
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, productID, storageID, count);
    }

    /**
     * Generate {@link Stock} string-representation
     * @return string-representation
     */
    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", productID=" + productID +
                ", storageID=" + storageID +
                ", count=" + count +
                '}';
    }
}
