package vista;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class GeneralMenuController {

    @FXML private MenuItem nameMenu, authorMenu, categoryMenu, editorialMenu;
    @FXML private RadioButton nameButton, authorButton, categoryButton, editorialButton;
    @FXML private Button searchButton;
    @FXML private Label label;
    @FXML private ImageView nameImage = new ImageView(new Image("resources/images/name.png"));
    @FXML private ImageView authorImage = new ImageView(new Image("resources/images/author.png"));
    @FXML private ImageView categoryImage = new ImageView(new Image("resources/images/category.png"));
    @FXML private ImageView editorialImage = new ImageView(new Image("resources/images/editorial.png"));

    private Stage stage;
    private boolean isConnected;


    public void setImages() {
        nameImage.setFitHeight(40);
        nameImage.setPreserveRatio(true);
        authorImage.setFitHeight(40);
        authorImage.setPreserveRatio(true);
        categoryImage.setFitHeight(40);
        categoryImage.setPreserveRatio(true);
        editorialImage.setFitHeight(40);
        editorialImage.setPreserveRatio(true);

        nameButton.setGraphic(nameImage);
        authorButton.setGraphic(authorImage);
        categoryButton.setGraphic(categoryImage);
        editorialButton.setGraphic(editorialImage);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void search(ActionEvent event) {
        SearchDialogue dialogue;
        try {
            dialogue  = new SearchDialogue(stage);
        } catch (IOException e) {
            String message = "There was an error when trying to display the search dialogue";
            errorDialogue("Error when trying to load the interface", message);
            return;
        }
        dialogue.showAndWait();
        // table.requestFocus();
        //ArrayList<BookProxy> results = 
        // viewSelectionModel.clearSelection();
    }

    private void errorDialogue(String title, String message) {
        if (isConnected) {
            System.out.println(message);
            // disconnect();
        } else {
            Alert dialogue = new Alert(AlertType.ERROR);
            dialogue.setTitle(title);
            dialogue.setHeaderText(null);
            dialogue.setContentText(message);
            //dialogue.setOnCloseRequest(e -> table.getItems().clear());
            dialogue.showAndWait();
            stage.requestFocus();
        }
    }
}
