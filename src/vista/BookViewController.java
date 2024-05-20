package vista;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import net.Book;

public class BookViewController implements Initializable {
    @FXML private Label titleLabel;
    @FXML private Label authorLabel;
    @FXML private Label categoryLabel;
    @FXML private Label editorialLabel;
    @FXML private ImageView bookImageView;
    @FXML private TextField firstNameLabel;
    @FXML private TextField lastNameLabel;
    @FXML private TextField accountNumberLabel;
    @FXML private Button submitButton;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Temporal para verificar que el formulario esté lleno.
        firstNameLabel.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // If the TextField is not empty, change its background color
                if (!newValue.isEmpty()) {
                    firstNameLabel.setStyle("-fx-background-color: white;");
                    submitButton.setDisable(false);
                } else {
                    // If the TextField is empty, reset its background color
                    firstNameLabel.setStyle("");
                    submitButton.setDisable(true);
                }
            }
        });
    }
    public void setData(Book book) {
        titleLabel.setText(book.getName());
        authorLabel.setText(book.getAuthor());
        categoryLabel.setText(book.getCategory());
        editorialLabel.setText(book.getEditorial());
    }

    public void setStage(Stage stage) { this.stage = stage; }

    @FXML private void submit() {
        stage.close();
    }
}

