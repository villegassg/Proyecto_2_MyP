package vista;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static final String HOME = "resources/scenes/generalmenu.fxml";

    @Override 
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(HOME));
            Parent root = loader.load();
            GeneralMenuController controller = loader.getController();
            controller.setImages();
            controller.setStage(primaryStage);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Bibioteca Cheems");
            primaryStage.show();
        } catch (IOException ioe) {
            System.out.println("There was an error when trying to create a socket.\n");
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
