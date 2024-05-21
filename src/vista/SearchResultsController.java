package vista;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.Book;
import net.Connection;

public class SearchResultsController implements Initializable {
    @FXML private TableView<Book> table;
    @FXML private TableColumn<Book, String> nameColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> editorialColumn;
    @FXML private TableColumn<Book, String> categoryColumn;
    @FXML private Button backButton;
    @FXML private Button viewButton;
    private Stage stage;
    private Connection connection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        editorialColumn.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                viewButton.setDisable(false);
            }
        });
    }
    public void setData(LinkedList<Book> list) {
        ObservableList<Book> observableList = FXCollections.observableArrayList(list);
        table.setItems(observableList);
    }

    public void setStage(Stage stage) { this.stage = stage; }

    @FXML private void back() {
        stage.close();
    }

    @FXML private void viewBook() {
        Book selectedBook = table.getSelectionModel().getSelectedItem();
        BookView bookView;
        try {
            bookView = new BookView(stage, selectedBook);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        BookViewController controller = bookView.getController();
        controller.setConnection(connection);
        bookView.show();
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}

