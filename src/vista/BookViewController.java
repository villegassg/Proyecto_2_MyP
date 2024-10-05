package vista;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import net.Book;
import net.Connection;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller class for the book view scene.
 */
public class BookViewController implements Initializable {

    /**
     * Request identifier for sending book request.
     */
    private static final String REQUEST = "REQUEST";

    /** Title label. */
    @FXML private Label titleLabel;
    /** Author label. */
    @FXML private Label authorLabel;
    /** Category label. */
    @FXML private Label categoryLabel;
    /** Editorial label. */
    @FXML private Label editorialLabel;
    /** Book portrait image view. */
    @FXML private ImageView bookImageView;
    /** First name request label. */
    @FXML private TextField firstNameLabel;
    /** Last name request label. */
    @FXML private TextField lastNameLabel;
    /** Account number request label. */
    @FXML private TextField accountNumberLabel;
    /** Sumbit request button. */
    @FXML private Button submitButton;

    /** The stage. */
    private Stage stage;
    /** The connection to the server. */
    private Connection connection;

    /**
     * Initializes the controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Listener to check if the first name TextField is empty
        firstNameLabel.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // If the TextField is not empty, enable the submit button
                if (!newValue.isEmpty()) {
                    firstNameLabel.setStyle("-fx-background-color: white;");
                    submitButton.setDisable(false);
                } else {
                    // If the TextField is empty, disable the submit button
                    firstNameLabel.setStyle("");
                    submitButton.setDisable(true);
                }
            }
        });
    }

    /**
     * Sets data for the book view.
     *
     * @param book the book data to display
     */
    public void setData(Book book) {
        titleLabel.setText(book.getName());
        authorLabel.setText("Author: " + book.getAuthor());
        categoryLabel.setText("Category: " + book.getCategory());
        editorialLabel.setText("Editorial: " + book.getEditorial());
        bookImageView.setImage(new Image(book.getPath()));
    }

    /**
     * Sets the stage for the controller.
     *
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Handles the submit button action.
     *
     * @param event the action event
     */
    public void submit(ActionEvent event) {
        String name = firstNameLabel.getText();
        String lastName = lastNameLabel.getText();
        long accountNumber = Long.parseLong(accountNumberLabel.getText());
        // Construct message for book request
        String message = String.format("%s_%s_%s_%d_%s", REQUEST, name, 
            lastName, accountNumber, titleLabel.getText());
        try {
            // Send message through the connection
            connection.sendMessage(message);
        } catch (IOException ioe) {
            // Handle IO exception
            ioe.printStackTrace();
        }
    }

    /**
     * Handles the back button action.
     */
    @FXML private void back() {
        // Close the stage
        stage.close();
    }

    /**
     * Sets the connection for the controller.
     *
     * @param connection the connection to set
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
