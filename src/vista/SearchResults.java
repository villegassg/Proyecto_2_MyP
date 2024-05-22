package vista;

import java.io.IOException;
import java.util.LinkedList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.Book;

public class SearchResults extends Stage {

    private static final String SEARCH_RESULTS = "resources/scenes/searchresults.fxml";

    private SearchResultsController controller;

    public SearchResults(Stage stage, LinkedList<Book> results) throws IOException {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getClassLoader().getResource(SEARCH_RESULTS));
            BorderPane pane = loader.load();
            setTitle("Search results");
            initOwner(stage);
            initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(pane);
            setScene(scene);
            controller = loader.getController();
            controller.setData(results);
            controller.setStage(stage);
            setResizable(false);
        } catch (IOException ioe) {
            System.out.println("An error was occurred when trying to deploy the " +
                    "search results window. We're sorry.\n");
            ioe.printStackTrace();
            throw new IOException();
        }
    }

    public SearchResultsController getController() {
        return controller;
    }
}
