package vista;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.Book;

/**
 * Custom stage for displaying detailed information about a book.
 */
public class BookView extends Stage {

    /**
     * Path to the FXML file for the book view scene.
     */
    private static final String BOOK_VIEW = "resources/scenes/bookview.fxml";

    /**
     * The controller associated with the book view.
     */
    private BookViewController controller;

    /**
     * Constructs a new BookView stage.
     *
     * @param stage        the owner stage
     * @param selectedBook the book to display details for
     * @throws IOException if an error occurs while loading the FXML file
     */
    public BookView(Stage stage, Book selectedBook) throws IOException {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(BOOK_VIEW));
            BorderPane pane = loader.load();

            // Set stage properties
            setTitle("Book Details");
            initOwner(stage);
            initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(pane);
            setScene(scene);

            // Get the controller and set data
            controller = loader.getController();
            controller.setData(selectedBook);
            controller.setStage(stage);

            // Disable window resizing
            setResizable(false);
        } catch (IOException ioe) {
            // Handle IOException separately for debugging purposes
            System.out.println("An error occurred when trying to deploy the book detail window. We're sorry.\n");
            ioe.printStackTrace();
            throw new IOException();
        }
    }

    /**
     * Retrieves the controller associated with the BookView.
     *
     * @return the controller
     */
    public BookViewController getController() {
        return controller;
    }
}
