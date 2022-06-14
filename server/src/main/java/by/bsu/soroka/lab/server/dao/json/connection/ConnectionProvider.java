package by.bsu.soroka.lab.server.dao.json.connection;

import by.bsu.soroka.lab.server.dao.exception.DAOException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class ConnectionProvider {
    private final static String JSON_EXTENSION = ".json";
    private static String getTablePath(String tableName) {
        return  ConnectionProvider.class
                .getResource('/' + tableName + JSON_EXTENSION).getFile();
    }

    public static JSONObject getJSONObject(String tableName) throws DAOException {
        log.debug("Getting JSONObject for {}", tableName);
        String data = null;
        log.debug("Loading from {}", getTablePath(tableName));
        try {
            data = new String(Files.readAllBytes(Paths.get(getTablePath(tableName))));
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("backed error");
        }

        JSONObject jsonObject = new JSONObject(data);
        return jsonObject;
    }

    public static void saveJSONObject(JSONObject jsonObject, String tableName)
            throws DAOException {
        log.debug("Saving JSONObject for {}", tableName);
        log.debug("Saving to {}", getTablePath(tableName));
        try (FileOutputStream fs = new FileOutputStream(getTablePath(tableName));
             PrintWriter output = new PrintWriter(fs);) {
            jsonObject.write(output);
        } catch (IOException e) {
            throw new DAOException(e);
        }
    }
}
