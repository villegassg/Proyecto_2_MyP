package vista;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import net.Book;
import net.BookField;
import net.Connection;

/**
 * Represents a controller for the search functionality.
 * This class implements the JavaFX Initializable interface.
 */
public class SearchController implements Initializable {

    /** 
     * Search message. It is sent to the server when the user wants to 
     * search a book.
     */
    private static final String SEARCH = "SEARCHBY";
    /**
     * Invalid message. It is sent to the server when something is not 
     * working properly, and it forces the server to disconnect the 
     * connection.
     */
    private static final String INVALID = "INVALID";
    
    /** Search button. */
    @FXML private Button searchButton;
    /** Book field options. */
    @FXML private ComboBox<BookField> options;
    /** Text field. */
    @FXML private TextField value;
    
    /** The stage. */
    private Stage stage;
    /** The accepted flag for the search button. */
    private boolean accepted;
    /** The connection. */
    private Connection connection;

    /**
     * Initializes the search controller.
     * Adds a listener to the text property of the TextField to enable/disable search button based on input.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        value.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // If the TextField is not empty, change its background color and enable search button
                if (!newValue.isEmpty()) {
                    value.setStyle("-fx-background-color: white;");
                    searchButton.setDisable(false);
                } else {
                    // If the TextField is empty, reset its background color and disable search button
                    value.setStyle("-fx-background-color:FFCCCC;");
                    searchButton.setDisable(true);
                }
            }
        });
    }

    /**
     * Sets the value of the ComboBox based on the provided button index.
     * 
     * @param button The index of the button indicating the type of search field.
     */
    public void setValue(short button) {
        switch (button) {
            case 1: 
                options.setValue(BookField.NAME);
                break;
            case 2: 
                options.setValue(BookField.AUTHOR);
                break;
            case 3:
                options.setValue(BookField.CATEGORY);
                break;
            case 4:
                options.setValue(BookField.EDITORIAL);
                break;
            default: 
                break;
        }
    }

    /**
     * Sets the connection used for communication.
     * 
     * @param connection The Connection object.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves the connection used for communication.
     * 
     * @return The Connection object.
     */
    public Connection getConnection() {
        return connection;
    }

        /**
     * Cancels the search operation and closes the stage.
     * 
     * @param event The ActionEvent triggering the cancellation.
     */
    @FXML private void cancel(ActionEvent event) {
        if (stage == null)
            stage = (Stage)value.getScene().getWindow();
        accepted = false;
        stage.close();
    }

    /**
     * Initiates the search operation based on the selected search field.
     * Sends a message to the server with the search query.
     * 
     * @param event The ActionEvent triggering the search.
     * @throws IOException If an I/O error occurs.
     */
    @FXML private void search(ActionEvent event) throws IOException {
        if (stage == null)
            stage = (Stage)value.getScene().getWindow();

        switch(getField()) {
            case NAME :
                connection.sendMessage(SEARCH + "_NAME_" + getValue());
                break;
            case AUTHOR :
                connection.sendMessage(SEARCH + "_AUTHOR_" + getValue());
                break;
            case CATEGORY :
                connection.sendMessage(SEARCH + "_CATEGORY_" + getValue());
                break;
            case EDITORIAL :
                connection.sendMessage(SEARCH + "_EDITORIAL_" + getValue());
                break;
            default :
                connection.sendMessage(INVALID);
                break;
        }
    }

    /**
     * Displays the search results in a separate window.
     * 
     * @param searchResults The list of search results.
     */
    public void showResults(LinkedList<Book> searchResults) {
        Platform.runLater(() -> {
            SearchResults results;
            try {
                results = new SearchResults(stage, searchResults);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            SearchResultsController controller = results.getController();
            controller.setConnection(connection);
            results.show();
        });
        accepted = true;
    }

    /**
     * Retrieves the tooltip message based on the selected search field.
     * 
     * @return The Tooltip object containing the tooltip message.
     */
    public Tooltip getTooltip() {
        String message = "";
        switch (options.getValue()) {
            case NAME:
                message = "Name of the book";
                break;
            case AUTHOR:
                message = "Author of the book";
                break;
            case CATEGORY:
                message = "Category of the book";
                break;
            case EDITORIAL:
                message = "Editorial of the book";
                break;
            default:
                break;
        }
        return new Tooltip(message);
    }

    /**
     * Retrieves the value entered for the search.
     * 
     * @return The search value.
     */
    public String getValue() {
        return value.getText();
    }

    /**
     * Retrieves the selected search field.
     * 
     * @return The selected search field.
     */
    public BookField getField() {
        return options.getValue();
    }

    /**
     * Sets the focus to the search input field.
     */
    public void defineFocus() {
        value.requestFocus();
    }

    /**
     * Gets the accepted button state.
     * @return <code>true</code> if the button search has been pressed.
     *          <code>false</code> otherwise.
     */
    public boolean getAccepted() {
        return accepted;
    }
}
