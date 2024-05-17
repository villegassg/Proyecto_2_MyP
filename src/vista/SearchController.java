package vista;

import java.net.URL;
import java.util.ResourceBundle;

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
import net.BookField;

public class SearchController implements Initializable {
    
    @FXML private Button searchButton;
    @FXML private ComboBox<BookField> options;
    @FXML private TextField value;
    private Stage stage;
    private boolean accepted;

    @FXML private void cancel(ActionEvent event) {
        if (stage == null)
            stage = (Stage)value.getScene().getWindow();
        accepted = false;
        stage.close();
    }

    public boolean isAccepted() {
        return accepted;
    }

    @FXML private void search(ActionEvent event) {
        if (stage == null)
            stage = (Stage)value.getScene().getWindow();
        accepted = true;
        stage.close();
    }

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

    public String getValue() {
        return value.getText();
    }

    public BookField getField() {
        return options.getValue();
    }

    public void defineFocus() {
        value.requestFocus();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add listener to the text property of the TextField
        value.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // If the TextField is not empty, change its background color
                if (!newValue.isEmpty()) {
                    value.setStyle("-fx-background-color: white;");
                    searchButton.setDisable(false);
                } else {
                    // If the TextField is empty, reset its background color
                    value.setStyle("");
                    searchButton.setDisable(true);
                }
            }
        });
    }

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
            default: break;
        }
    }
}
