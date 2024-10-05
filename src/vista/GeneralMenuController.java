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

/**
 * Controller class for the general menu scene.
 */
public class GeneralMenuController {
    
    /** 
     * Book message. It is sent when a user searches for a book, and the server 
     * sends the book results one by one with this message containing a toString 
     * representation of a Book. 
     */
    private static final String BOOK = "BOOK";
    /**
     * This message is sent when the server has finished sending the books to 
     * this controller, so it means it's time to show the table with the 
     * search results.
     */
    private static final String FINISHRESULTS = "FINISHRESULTS";
    /** Request aproved message. It means that the book request has been aproved. */
    private static final String REQUESTAPROVED = "REQUESTAPROVED";
    /** Request denied message. It means that the book request has been denied. */
    private static final String REQUESTDENIED = "REQUESTDENIED";
    /** 
     * Disconnect message. It means the server wants to disconnect this 
     * connection for some reason.
     */
    private static final String DISCONNECT = "DISCONNECT";

    /** Menu Items */
    @FXML private MenuItem connectMenu, nameMenu, authorMenu, categoryMenu, editorialMenu;
    /** Book fields options buttons. */
    @FXML private RadioButton nameButton, authorButton, categoryButton, editorialButton;
    /** Search button. */
    @FXML private Button searchButton;
    /** Bienvenue label. */
    @FXML private Label label;
    /** Name Button Image. */
    @FXML private ImageView nameImage = new ImageView(new Image("resources/images/name.png"));
    /** Author Button Image. */
    @FXML private ImageView authorImage = new ImageView(new Image("resources/images/author.png"));
    /** Category Button Image. */
    @FXML private ImageView categoryImage = new ImageView(new Image("resources/images/category.png"));
    /** Editorial Button Image */
    @FXML private ImageView editorialImage = new ImageView(new Image("resources/images/editorial.png"));
    /** Searching dialogue. */
    SearchDialogue dialogue;

    /** Selection flag for name button. */
    private boolean nameButtonIsSelected;
    /** Selection flag for author button. */
    private boolean authorButtonIsSelected;
    /** Selection flag for category button. */
    private boolean categoryButtonIsSelected;
    /** Selection flag for editorial button. */
    private boolean editorialButtonIsSelected;
    /** The stage. */
    private Stage stage;
    /** A flag for the connection with the server. */
    private boolean isConnected;
    /** The connection. */
    private Connection connection;
    /** Search results. */
    private LinkedList<Book> searchResults = new LinkedList<>();

    /**
     * Sets the flag for name button selection to true, and the others 
     * to false.
     */
    public void setNameButton() {
        nameButtonIsSelected = true;
        authorButtonIsSelected = false;
        categoryButtonIsSelected = false;
        editorialButtonIsSelected = false;
    }

    /**
     * Sets the flag for author button selection to true, and the others 
     * to false.
     */
    public void setAuthorButton() {
        nameButtonIsSelected = false;
        authorButtonIsSelected = true;
        categoryButtonIsSelected = false;
        editorialButtonIsSelected = false;
    }
    
    /**
     * Sets the flag for category button selection to true, and the others 
     * to false.
     */
    public void setCategoryButton() {
        nameButtonIsSelected = false;
        authorButtonIsSelected = false;
        categoryButtonIsSelected = true;
        editorialButtonIsSelected = false;
    }

    /**
     * Sets the flag for editorial button selection to true, and the others 
     * to false.
     */
    public void setEditorialButton() {
        nameButtonIsSelected = false;
        authorButtonIsSelected = false;
        categoryButtonIsSelected = false;
        editorialButtonIsSelected = true;
    }

    /**
     * Sets the field for name in the Search Dialogue.
     * 
     * @param event the action event
     */
    public void setNameSelected(ActionEvent event) {
        nameButton.setSelected(true);
        setNameButton();
        search(event);
    }

    /**
     * Sets the field for author in the Search Dialogue.
     * 
     * @param event the action event
     */
    public void setAuthorSelected(ActionEvent event) {
        authorButton.setSelected(true);
        setAuthorButton();
        search(event);
    }

    /**
     * Sets the field for category in the Search Dialogue.
     * 
     * @param event the action event
     */
    public void setCategorySelected(ActionEvent event) {
        categoryButton.setSelected(true);
        setCategoryButton();
        search(event);
    }

    /**
     * Sets the field for editorial in the Search Dialogue.
     * 
     * @param event the action event
     */
    public void setEditorialSelected(ActionEvent event) {
        editorialButton.setSelected(true);
        setEditorialButton();
        search(event);
    }

    /**
     * Sets the images to the buttons.
     */
    public void setImages() {
        // Set images and preserve aspect ratio
        nameImage.setFitHeight(40);
        nameImage.setPreserveRatio(true);
        authorImage.setFitHeight(40);
        authorImage.setPreserveRatio(true);
        categoryImage.setFitHeight(40);
        categoryImage.setPreserveRatio(true);
        editorialImage.setFitHeight(40);
        editorialImage.setPreserveRatio(true);

        // Set images to buttons
        nameButton.setGraphic(nameImage);
        authorButton.setGraphic(authorImage);
        categoryButton.setGraphic(categoryImage);
        editorialButton.setGraphic(editorialImage);
    }

    /**
     * Stablishes a connection with the server.
     * 
     * @param event the action event.
     */
    public void connect(ActionEvent event) {
        String ip = "";
        try {
            // Create socket and connection
            Socket socket = new Socket("localhost", 1234);
            this.connection = new Connection(socket);
            connection.addListener((c, m) -> receivedMessage(c, m));
            isConnected = true;
            // Start a thread to receive messages
            new Thread(() -> connection.receiveMessages()).start();
            ip = socket.getInetAddress().toString();
        } catch (IOException ioe) {
            // Handle connection error
            System.out.println("\nClientConnectionException:\n");
            ioe.printStackTrace();
            errorDialogue("ERROR DE CONEXIÓN", 
                "Lo sentimos, ocurrió un error al tratar de establecer conexión con " + 
                "la dirección ip: " + ip);
        }
    }

    /**
     * Handles the received messages from the server.
     * 
     * @param connection the connection with the server.
     * @param message the message.
     */
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

    /**
     * Sets the stage of this scene.
     * @param stage the stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Allows the user to search books. Opens the search dialogue and sends 
     * the search message to the server so it can search for the desired book.
     * 
     * @param event the action event.
     */
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

    /**
     * While there're books that match the search of the user, this method 
     * obtains them and store them in the searchResults list.
     * 
     * @param message the message containing a book.
     */
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

    /**
     * Show the results of the search. Opens the Search Results scene and 
     * shows the results to the user.
     */
    private void showResults() {
        SearchController controller = dialogue.getController();
        controller.setConnection(connection);
        controller.showResults(searchResults);
    }

    /**
     * Shows a confirmation that a book request has been aproved.
     * 
     * @param message a message containing the request aproved message.
     */
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

    /**
     * Shows an alert to the user that the book request has been denied.
     * 
     * @param message the message containing the book request deny.
     */
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

    /**
     * Shows an error alert to the user.
     * 
     * @param title the title of the alert.
     * @param message the message to show to the user.
     */
    private void errorDialogue(String title, String message) {
        if (isConnected) {
            System.out.println(message);
        } else {
            Alert dialogue = new Alert(AlertType.ERROR);
            dialogue.setTitle(title);
            dialogue.setHeaderText(null);
            dialogue.setContentText(message);
            dialogue.showAndWait();
            stage.requestFocus();
        }
    }

    /**
     * Sends the server a message that means the user wants to disconnect 
     * this connection from the server.
     * 
     * @param event the action event.
     */
    public void disconnect(ActionEvent event) {
        try {
            connection.sendMessage(DISCONNECT);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
 
    /**
     * Looks for the selected button to activate the corresponing field 
     * in the search dialogue.
     * @return a short number representing the button.
     */
    private short lookForSelectedButton() {
        if (nameButtonIsSelected) return 1;
        else if (authorButtonIsSelected) return 2;
        else if (categoryButtonIsSelected) return 3;
        else if (editorialButtonIsSelected) return 4;
        else return -1;
    }
}
