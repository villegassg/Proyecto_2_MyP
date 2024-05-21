package vista;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import net.Book;

public class BookView extends Stage {

    private static final String BOOK_VIEW = "resources/scenes/bookview.fxml";

    private BookViewController controller;

    public BookView(Stage stage, Book selectedBook) throws IOException {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getClassLoader().getResource(BOOK_VIEW));
            BorderPane pane = loader.load();
            setTitle("Book Details");
            initOwner(stage);
            initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(pane);
            setScene(scene);
            controller = loader.getController();
            controller.setData(selectedBook);
            controller.setStage(stage);
            setResizable(false);
        } catch (IOException ioe) {
            System.out.println("An error was occurred when trying to deploy the " +
                    "book detail window. We're sorry.\n");
            ioe.printStackTrace();
            throw new IOException();
        }
    }

    public BookViewController getController() {
        return controller;
    }
}
