package vista;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import javafx.application.Platform;
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
import net.Book;
import net.Connection;

public class GeneralMenuController {
    
    private static final String BOOK = "BOOK";
    private static final String FINISHRESULTS = "FINISHRESULTS";
    private static final String REQUESTAPROVED = "REQUESTAPROVED";
    private static final String REQUESTDENIED = "REQUESTDENIED";
    private static final String DISCONNECT = "DISCONNECT";

    @FXML private MenuItem connectMenu, nameMenu, authorMenu, categoryMenu, editorialMenu;
    @FXML private RadioButton nameButton, authorButton, categoryButton, editorialButton;
    @FXML private Button searchButton;
    @FXML private Label label;
    @FXML private ImageView nameImage = new ImageView(new Image("resources/images/name.png"));
    @FXML private ImageView authorImage = new ImageView(new Image("resources/images/author.png"));
    @FXML private ImageView categoryImage = new ImageView(new Image("resources/images/category.png"));
    @FXML private ImageView editorialImage = new ImageView(new Image("resources/images/editorial.png"));
    SearchDialogue dialogue;

    private boolean nameButtonIsSelected;
    private boolean authorButtonIsSelected;
    private boolean categoryButtonIsSelected;
    private boolean editorialButtonIsSelected;
    private Stage stage;
    private boolean isConnected;
    private Connection connection;
    private LinkedList<Book> searchResults = new LinkedList<>();

    public void setNameButton() {
        nameButtonIsSelected = true;
        authorButtonIsSelected = false;
        categoryButtonIsSelected = false;
        editorialButtonIsSelected = false;
    }

    public void setAuthorButton() {
        nameButtonIsSelected = false;
        authorButtonIsSelected = true;
        categoryButtonIsSelected = false;
        editorialButtonIsSelected = false;
    }
    
    public void setCategoryButton() {
        nameButtonIsSelected = false;
        authorButtonIsSelected = false;
        categoryButtonIsSelected = true;
        editorialButtonIsSelected = false;
    }

    public void setEditorialButton() {
        nameButtonIsSelected = false;
        authorButtonIsSelected = false;
        categoryButtonIsSelected = false;
        editorialButtonIsSelected = true;
    }

    public void setNameSelected(ActionEvent event) {
        nameButton.setSelected(true);
        setNameButton();
        search(event);
    }

    public void setAuthorSelected(ActionEvent event) {
        authorButton.setSelected(true);
        setAuthorButton();
        search(event);
    }

    public void setCategorySelected(ActionEvent event) {
        categoryButton.setSelected(true);
        setCategoryButton();
        search(event);
    }

    public void setEditorialSelected(ActionEvent event) {
        editorialButton.setSelected(true);
        setEditorialButton();
        search(event);
    }

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

    public void connect(ActionEvent event) {
        String ip = "";
        try {
            Socket socket = new Socket("localhost", 1234);
            this.connection = new Connection(socket);
            connection.addListener((c, m) -> receivedMessage(c, m));
            isConnected = true;
            new Thread(() -> connection.receiveMessages()).start();
            ip = socket.getInetAddress().toString();
        } catch (IOException ioe) {
            System.out.println("\nClientConnectionException:\n");
            ioe.printStackTrace();
            errorDialogue("ERROR DE CONEXIÓN", 
                "Lo sentimos, ocurrió un error al tratar de establecer conexión con " + 
                "la dirección ip: " + ip);
        }
    }

    public void receivedMessage(Connection connection, String message) {
        if (isConnected) {
            if (message.startsWith(BOOK)) {
                collectResults(message);
            } else if (message.startsWith(FINISHRESULTS)) {
                showResults();
            } else if (message.startsWith(REQUESTAPROVED)) {
                String m = message.substring(REQUESTAPROVED.length());
                requestAproved(m);
            } else if (message.startsWith(REQUESTDENIED)) {
                String m = "Your request has been denied. You have not entered a valid user.";
                requestDenied(m);
            }
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void search(ActionEvent event) {
        searchResults.clear();
        try {
            dialogue  = new SearchDialogue(stage, lookForSelectedButton());
        } catch (IOException e) {
            String message = "There was an error when trying to display the search dialogue";
            errorDialogue("Error when trying to load the interface", message);
            return;
        }
        SearchController controller = dialogue.getController();
        controller.setConnection(connection);
        dialogue.showAndWait();
    }

    private void collectResults(String message) {
        String bookForm = message.substring(BOOK.length() + 1);
        String[] parameters = bookForm.split("\t");
        int ID = Integer.parseInt(parameters[0].substring("Id: ".length()));
        String NAME = parameters[1].substring("Name: ".length());
        String AUTHOR = parameters[2].substring("Author: ".length());
        String CATEGORY = parameters[3].substring("Category: ".length());
        String EDITORIAL = parameters[4].substring("Editorial: ".length());
        String PATH = parameters[5].substring("Path: ".length());
        Book book = new Book(ID, NAME, AUTHOR, CATEGORY, EDITORIAL, PATH);
        searchResults.add(book);
    }

    private void showResults() {
        SearchController controller = dialogue.getController();
        controller.setConnection(connection);
        controller.showResults(searchResults);
    }

    private void requestAproved(String message) { 
        Platform.runLater(() -> {
            RequestConfirmation requestConfirmation;
            try {
                requestConfirmation = new RequestConfirmation(stage, message);
            } catch (IOException e) {
                errorDialogue("Error when trying to load the interface", message);
                return;
            }
            requestConfirmation.showAndWait();
        });
    }

    private void requestDenied(String message) {
        Platform.runLater(() -> {
            Alert dialogue = new Alert(AlertType.ERROR);
            dialogue.setTitle("Request Denied");
            dialogue.setHeaderText(null);
            dialogue.setContentText(message);
            dialogue.showAndWait();
            stage.requestFocus();
        });
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

    public void disconnect(ActionEvent event) {
        try {
            connection.sendMessage(DISCONNECT);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
 
    private short lookForSelectedButton() {
        if (nameButtonIsSelected) return 1;
        else if (authorButtonIsSelected) return 2;
        else if (categoryButtonIsSelected) return 3;
        else if (editorialButtonIsSelected) return 4;
        else return -1;
    }
}
