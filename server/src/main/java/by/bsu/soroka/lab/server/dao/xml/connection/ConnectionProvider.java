package by.bsu.soroka.lab.server.dao.xml.connection;

import by.bsu.soroka.lab.common.entity.Identifiable;
import by.bsu.soroka.lab.server.dao.exception.DAOException;
import jakarta.xml.bind.JAXBException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectionProvider {
    private final static String XML_EXTENSION = ".xml";
    private final static String DATA_FOLDER = "/xml";
    private static String getTablePath(String tableName) {
        return  ConnectionProvider.class
                .getResource(DATA_FOLDER + '/' + tableName + XML_EXTENSION).getFile();
    }

    public static <T extends Identifiable> DataContainer<T> getDataContainer(DataContainer<T> dataContainer, String tableName) throws DAOException {
        log.debug("Getting DataContainer for {}", tableName);
        log.debug("Loading from {}", getTablePath(tableName));
        try {
            dataContainer.reload(getTablePath(tableName));
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new DAOException("backed error");
        }

        return dataContainer;
    }

    public static <T extends Identifiable> void saveDataContainer(DataContainer<T> dataContainer, String tableName)
            throws DAOException {
        log.debug("Saving DataContainer for {}", tableName);
        log.debug("Saving to {}", getTablePath(tableName));
        try {
            dataContainer.write(getTablePath(tableName));
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new DAOException("backed error");
        }
    }
}
