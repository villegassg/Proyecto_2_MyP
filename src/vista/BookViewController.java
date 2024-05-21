package vista;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import net.Book;
import net.Connection;

public class BookViewController implements Initializable {

    private static final String REQUEST = "REQUEST";

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
    private Connection connection;

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
        authorLabel.setText("Author: " + book.getAuthor());
        categoryLabel.setText("Category: " + book.getCategory());
        editorialLabel.setText("Editorial: " + book.getEditorial());
        bookImageView.setImage(new Image(book.getPath()));
    }

    public void setStage(Stage stage) { this.stage = stage; }

    public void submit(ActionEvent event) {
        String name = firstNameLabel.getText();
        String lastName = lastNameLabel.getText();
        long accountNumber = Long.parseLong(accountNumberLabel.getText());
        String message = String.format("%s_%s_%s_%d_%s", REQUEST, name, 
            lastName, accountNumber, titleLabel.getText());
        try {
            connection.sendMessage(message);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void errorDialogue(String title, String message) {
        Alert dialogue = new Alert(AlertType.ERROR);
        dialogue.setTitle(title);
        dialogue.setHeaderText(null);
        dialogue.setContentText(message);
        //dialogue.setOnCloseRequest(e -> table.getItems().clear());
        dialogue.showAndWait();
        stage.requestFocus();
    }

    @FXML private void back() {
        stage.close();
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}

