package by.bsu.soroka.lab.client.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * @author Soroka Egor
 */
public class AppFX extends Application {


    /**
     * necessary for {@link javafx.application.Application}
     * @param stage Prepare and show {@link javafx.stage.Stage}.
     * @throws Exception thrown if something goes wrong
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        Scene scene = new Scene(root,400,400);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Run <b>client-side</b> application.
     * @param args Arguments for function.
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
