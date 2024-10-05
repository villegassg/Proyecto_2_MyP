package vista;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for launching the JavaFX application.
 */
public class App extends Application {

    /**
     * Path to the FXML file for the main scene.
     */
    private static final String HOME = "resources/scenes/generalmenu.fxml";

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override 
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(HOME));
            Parent root = loader.load();

            // Get the controller and set up the stage and scene
            GeneralMenuController controller = loader.getController();
            controller.setImages();
            controller.setStage(primaryStage);

            // Set the scene and show the stage
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Biblioteca Cheems");
            primaryStage.show();
        } catch (IOException ioe) {
            // Handle IOException separately for debugging purposes
            System.out.println("There was an error when trying to load the FXML file.\n");
            ioe.printStackTrace();
        } catch (Exception e) {
            // Handle any other exceptions
            e.printStackTrace();
        }
    }
}
