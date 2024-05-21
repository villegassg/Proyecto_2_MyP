package vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ConfirmationController {

    @FXML private Label titleLabel;
    @FXML private Button acceptButton;
    private Stage stage;

    public void setBookTitle(String title) {
        titleLabel.setText(title);
    }
    
    @FXML private void accept(ActionEvent event) {
        if (stage == null)
            stage = (Stage)titleLabel.getScene().getWindow();
        stage.close();
    }
}
