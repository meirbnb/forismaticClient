package sample;

/* Importing necessary modules & libraries */
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

// Controller class for main window
public class Controller{

    // defining necessary elements
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane wrapper;

    @FXML
    private Label quote;

    @FXML
    private ImageView profile;

    @FXML
    private Button nextBtn;

    @FXML
    private Button bookmarkBtn;

    @FXML
    private Button prevBtn;

    // initializing needed variables
    private int id = -1; // for keeping track of orders
    private String lang = "en"; // for the storage of the current language
    /* url for making HTTP-GET request to obtain random quote */
    private String req = "https://api.forismatic.com/api/1.0/?method=getQuote&format=text&lang=";
    /* Arraylist for storing quotes */
    private ArrayList<String> quotes     = new ArrayList<String>();
    /* Arraylist for storing bookmarked quotes */
    private ArrayList<String> bookmarked = new ArrayList<String>();

    /* necessary options for DBMS connection */
    private Connection connection = null;
    private Main.ConnectDB obj_ConnectDB = new Main.ConnectDB();

    /* Running the program */
    @FXML
    void initialize() {
        /* Adding inital text to the quote viewer */
        quotes.add("Click next to get a quote...");
        ++id; // increment the id
        /* Estabilishing connection with DBMS */
        connection = obj_ConnectDB.get_connection();
        /* working with dbms */
        try{
            Statement statement = connection.createStatement();
            String query = "SELECT quote FROM bookmarks";
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()) bookmarked.add(rs.getString(1));
        } catch (Exception error){
            System.out.println(error);
        }
        /* check whether the current quote has been marked before */
        if (bookmarked.contains(quote.getText()))
            bookmarkBtn.setText(lang == "en" ? "Unbookmark -" : "Убрать из коллекции -");
        else
            bookmarkBtn.setText(lang == "en" ? "Bookmark +" : "В коллекцию +");

        /* Adding event to previous button as follows */
        prevBtn.setOnAction(event -> {
            quote.setText(quotes.get(--id)); // go backwards
            if (bookmarked.contains(quote.getText()))
                bookmarkBtn.setText(lang == "en" ? "Unbookmark -" : "Убрать из коллекции -");
            else
                bookmarkBtn.setText(lang == "en" ? "Bookmark +" : "В коллекцию +");
            if (id == 0){ // once we hit to the very first quote
                prevBtn.setDisable(true); // disable the previous button
                bookmarkBtn.setDisable(true); // disable the bookmark button
            }
        });

        /* including an event to bookmark button */
        bookmarkBtn.setOnAction(event -> {
            if (bookmarkBtn.getText().equalsIgnoreCase(lang == "en" ? "Bookmark +" : "В коллекцию +")){
                try{
                    String sql = "INSERT INTO bookmarks(quote, added_date) VALUES(?, ?)";
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setString(1, quote.getText());
                    ps.setDate(2, Date.valueOf(java.time.LocalDate.now()));
                    ps.executeUpdate();
                    bookmarked.add(quote.getText());
                    bookmarkBtn.setText(lang == "en" ? "Unbookmark -" : "Убрать из коллекции -");
                } catch (Exception error){
                    System.out.println(error.getMessage());
                }
            } else {
                try{
                    // unbookmark the quote by removing the record from the database
                    String sql = "DELETE FROM bookmarks WHERE quote = ?";
                    /* Using prepared statements to run the statement */
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setString(1, quote.getText());
                    ps.executeUpdate(); // execute the query
                    bookmarked.remove(new String(quote.getText())); // remove it from the arrayList
                    bookmarkBtn.setText(lang == "en" ? "Bookmark +" : "В коллекцию +"); // change the caption of the button accordingly
                } catch (Exception error){
                    System.out.println(error.getMessage()); // output the error
                }
            }
        });
        /* adding a mouse click event to profile picture (preferences) as follows */
        profile.setOnMouseClicked(event -> {
            try {
                /* Open the new window */
                FXMLLoader fxmlLoader = new FXMLLoader();  // fxml object
                fxmlLoader.setLocation(getClass().getResource("preferences.fxml")); // define the fxml file
                Scene scene = new Scene(fxmlLoader.load(), 823, 534); // set parameters
                Stage stage = new Stage();
                /* Setting up default settings */
                stage.setTitle("Preferences"); // set its title
                stage.setResizable(false); // make resizable unavailable
                stage.setAlwaysOnTop(true); // make on top of windows
                stage.setScene(scene);
                stage.show(); // show the form
                /* Adding closing event to form */
                stage.setOnCloseRequest(event1 -> {
                    // get the existing instance as follows
                    Settings setting = Settings.getInstance();
                    /* checking the chosen language */
                    if (setting.getLang().equalsIgnoreCase("english")){
                        lang = "en";
                        nextBtn.setText("Next >");
                        prevBtn.setText("< Previous");
                        bookmarkBtn.setText("Bookmark +");
                    } else {
                        lang = "ru";
                        nextBtn.setText("Следующий >");
                        prevBtn.setText("< Предыдущий");
                        bookmarkBtn.setText("В коллекцию +");
                    }
                    /* check whether the removeAll button was pressed */
                    if (setting.isDropBookmarks()){
                        try{
                            Statement statement = connection.createStatement();
                            String query = "DELETE FROM bookmarks"; // Clear all the records of the table bookmarks
                            bookmarked.clear(); // clear the bookmarked arrayList
                            statement.executeUpdate(query); // execute the query
                        } catch (Exception error){
                            System.out.println(error); // output the error if there are any
                        }
                    }
                    try{
                        // obtain the language
                        lang = setting.getLang().equalsIgnoreCase("english") ? "en" : "ru";
                        URL request = new URL(req + lang); // request the page
                        Scanner scan = new Scanner(request.openStream());
                        String response = scan.useDelimiter("\\Z").next();
                        scan.close(); // close the scanner
                        quote.setText(response); // show the quote on the screen
                        quotes.add(response); // append into quotes arrayList
                        ++id; // increment the id
                        Statement statement = connection.createStatement();
                        String query = "SELECT quote FROM bookmarks";
                        ResultSet rs = statement.executeQuery(query);
                        while(rs.next()) bookmarked.add(rs.getString(1));
                    } catch (Exception error){
                        System.out.println(error); // output the error
                    }
                });
            } catch (IOException e) {
                System.out.println(e); // output the error
            }
        });
        // add event as follows
        nextBtn.setOnAction(event -> {
            // check the range
            if (id + 1 < (quotes).size()){
                quote.setText(quotes.get(++id)); // keep showing the quote forward
            } else {
                try {
                    // request the URL using get request
                    URL request = new URL(req + lang);
                    Scanner scan = new Scanner(request.openStream());
                    String response = scan.useDelimiter("\\Z").next();
                    scan.close();
                    quote.setText(response); // show the quote
                    quotes.add(response); // add it into the arraylist
                    ++id; // increment the id
                } catch (Exception e) {
                    System.out.println(e); // print the error
                }
            }
            prevBtn.setDisable(false); // make prevBtn disabled when it gets out of range
            bookmarkBtn.setDisable(false); // make bookmark button disabled as well
            if (bookmarked.contains(quote.getText())) // check if the current quote is present in bookmarked
                bookmarkBtn.setText(lang == "en" ? "Unbookmark -" : "Убрать из коллекции -");
            else
                bookmarkBtn.setText(lang == "en" ? "Bookmark +" : "В коллекцию +");
        });
    }
}
