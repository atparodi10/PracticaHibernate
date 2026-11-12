package ni.edu.uam.practicahibernate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LibreriaApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LibreriaApplication.class.getResource("libros-views.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Libros y ORM");
        stage.setScene(scene);
        stage.show();
    }
}
