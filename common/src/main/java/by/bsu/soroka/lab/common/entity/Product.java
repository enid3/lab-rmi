package by.bsu.soroka.lab.common.entity;

import java.io.Serializable;
import java.util.Objects;


/**
 * @author Soroka Egor
 * Class describes product, via {@link Product#id},{@link Product#name},{@link Product#price}.
 * Class must be {@link Serializable}
 */
public class Product implements Serializable, Identifiable {
    private static final long serialVersionUID = -8157269109462864430L;
    private int id ;
    private String name;
    private int price;

    /**
     * Construct's {@link Product}.
     * @param id identifier of {@link Product}
     * @param name name of the {@link Product}
     * @param price price of the {@link Product}, <b>must be positive </b>
     */
    public Product(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    /**
     * Construct's empty {@link Product}.
     * Data can be specified via <b>getter & setters</b>
     */
    public Product(){}

    /**
     * Construct's {@link Product} with {@link Product#id}=0.
     * Equals to run {@link Product#Product(int, String, int) Product(0, name, price)}
     * @param name name of the {@link Product}
     * @param price price of the {@link Product}, <b>must be positive </b>
     */
    public Product(String name, int price) {
        this(0, name, price);
    }

    /**
     * See {@link Identifiable#getId()} for more info.
     * @return id identifier of {@link Product}
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Getter for field {@link Product#name} of {@link Product}
     * @return value of {@link Product#name} of {@link Product}
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for field {@link Product#price} of {@link Product}
     * @return value of {@link Product#price} of {@link Product}
     */
    public int getPrice() {
        return price;
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
     * Setter for field {@link Product#name} of {@link Product}
     * @param name new value of {@link Product#name} for {@link Product}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for field {@link Product#price} of {@link Product}
     * @param price new value of {@link Product#price} for {@link Product}
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Check equals for {@link Product} entities;
     * @param o Object to check.
     * @return <b>true</b> if {@link Product#id}, {@link Product#name}, {@link Product#price} are equals.
     * <b>false</b> otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && price == product.price && Objects.equals(name, product.name);
    }

    /**
     * @return hashCode of {@link Product}
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }

    /**
     * Generate {@link Product} string-representation
     * @return string-representation
     */
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
