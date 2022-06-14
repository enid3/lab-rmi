package by.bsu.soroka.lab.common.entity;


/**
 * @author Soroka Egor
 * Used, for providing comfortable way for interaction with all {@link Product}, {@link Storage},{@link Stock}.
 */
public interface Identifiable {
    /**
     * Set new identifier.
     * @param id identifier, that should be saved.
     */
    void setId(int id);

    /**
     * Used for getting identifier, that was previously setted by {@link Identifiable#setId(int)} setId}.
     * @return return's identifier.
     */
    int getId();
}
