package vista;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Represents a confirmation dialogue box used to display a confirmation message with a specified title.
 * This class extends the JavaFX Stage class.
 */
public class RequestConfirmation extends Stage {
    
    // Path to the FXML file defining the layout of the confirmation dialogue
    private static final String SEARCH_BOOKS = "resources/scenes/requestconfirmation.fxml";

    // Controller associated with the confirmation dialogue
    private ConfirmationController controller;

    /**
     * Constructs a new RequestConfirmation object.
     * 
     * @param stage The parent stage.
     * @param title The title of the confirmation message.
     * @throws IOException If an error occurs during the loading of the FXML file.
     */
    public RequestConfirmation(Stage stage, String title) throws IOException {
        try {
            FXMLLoader loader = 
                new FXMLLoader(getClass().getClassLoader().getResource(SEARCH_BOOKS));
            AnchorPane pane = (AnchorPane)loader.load();
            
            setTitle("Search books");
            initOwner(stage);
            initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(pane);
            setScene(scene);
            
            controller = loader.getController();
            controller.setBookTitle(title);
            
            setResizable(false);
        } catch (IOException ioe) {
            System.out.println("An error occurred when trying to deploy the " + 
                "search dialogue. We're sorry.\n");
            ioe.printStackTrace();
            throw new IOException();
        }
    }

    /**
     * Retrieves the controller associated with this dialogue.
     * 
     * @return The ConfirmationController object.
     */
    public ConfirmationController getController() {
        return controller;
    }
}
