package vista;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.BookField;

public class SearchDialogue extends Stage {
    
    private static final String SEARCH_BOOKS = "resources/scenes/searchbooks.fxml";

    private SearchController controller;

    public SearchDialogue(Stage stage) throws IOException {
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
            stage.setOnShown(w -> controller.defineFocus());
            setResizable(false);
        } catch (IOException ioe) {
            System.out.println("An error was occured when trying to deploy the " + 
                "search dialogue. We're sorry.\n");
            ioe.printStackTrace();
            throw new IOException();
        }
    }

    public BookField getField() {
        return controller.getField();
    }

    public String getValue() {
        return controller.getValue();
    }
}
