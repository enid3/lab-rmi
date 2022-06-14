package by.bsu.soroka.lab.common.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Soroka Egor
 * Class describes storage, via {@link Storage#id}, {@link Storage#name}.
 * Class must be {@link Serializable}
 */
public class Storage implements Serializable, Identifiable {
    private static final long serialVersionUID = -3295499478815283724L;
    private int id;
    private String name;
    //private String location;


    /**
     * Construct's {@link Storage}.
     * @param id identifier of {@link Storage}
     * @param name name of the {@link Storage}
     */
    public Storage(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Construct's empty {@link Storage}.
     * Data can be specified via <b>getter & setters</b>
     */
    public Storage(){}

    /**
     * Construct's {@link Storage} with {@link Storage#id}=0.
     * Equals to run {@link Storage#Storage(int, String)}  Storage(0, name, price)}
     * @param name name of the {@link Storage}
     */
    public Storage(String name) {
        this(0, name);
    }

    /**
     * See {@link Identifiable#getId()} for more info.
     * @return id identifier of {@link Storage}
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Getter for field {@link Storage#name} of {@link Storage}
     * @return value of {@link Storage#name} of {@link Storage}
     */
    public String getName() {
        return name;
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
     * Setter for field {@link Storage#name} of {@link Storage}
     * @param name new value of {@link Storage#name} for {@link Storage}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Check equals for {@link Storage} entities;
     * @param o Object to check.
     * @return <b>true</b> if {@link Storage#id}, {@link Storage#name} are equals.
     * <b>false</b> otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Storage storage = (Storage) o;
        return id == storage.id && Objects.equals(name, storage.name);
    }

    /**
     * @return hashCode of {@link Product}
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    /**
     * Generate {@link Product} string-representation
     * @return string-representation
     */
    @Override
    public String toString() {
        return "Storage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
