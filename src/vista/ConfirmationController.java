package vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller class for the confirmation dialog.
 */
public class ConfirmationController {

    /** Title label. */
    @FXML private Label titleLabel;
    /** Accept button. */
    @FXML private Button acceptButton;

    private Stage stage;

    /**
     * Sets the book title to display in the confirmation dialog.
     *
     * @param title the title of the book
     */
    public void setBookTitle(String title) {
        titleLabel.setText(title);
    }
    
    /**
     * Handles the action event for accepting the confirmation.
     *
     * @param event the action event
     */
    @FXML private void accept(ActionEvent event) {
        // If stage is not set, retrieve it from the label's scene and close it
        if (stage == null)
            stage = (Stage)titleLabel.getScene().getWindow();
        stage.close();
    }
}