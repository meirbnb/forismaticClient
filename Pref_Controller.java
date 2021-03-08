package sample;

/* Importing necessary modules & libraries */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.ResourceBundle;

/* Importing necessary modules & libraries */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

/* Controller class for preferences form */
public class Pref_Controller {
    // defining needed elements
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> langsBox;

    @FXML
    private TextArea quotesViewer;

    @FXML
    private Button apply;

    @FXML
    private Button saveAsTxt;

    @FXML
    private Button removeAll;

    // initializing process goes as follows
    @FXML
    void initialize() {
        /* Establishing the connection with databse */
        Main.ConnectDB obj_ConnectDB = new Main.ConnectDB();
        Connection connection = obj_ConnectDB.get_connection();
        /* starting the work with DBMS */
        try{
            Statement statement = connection.createStatement();
            String query = "SELECT quote FROM bookmarks"; // fetching all data from DB
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){ // iterating over result set
                String newQuote = rs.getString(1);
                String curQuote = quotesViewer.getText(); // obtaining current quote
                if (curQuote.isEmpty()) // check for validity
                    quotesViewer.setText(newQuote); // add the new quote
                else
                    quotesViewer.setText(curQuote + "\n\n" + newQuote); // append the new quote
            }
        } catch (Exception error){
            System.out.println(error); // print out the error if it was detected
        }

        /* Create observable list for the storage of languages */
        ObservableList<String> options = FXCollections.observableArrayList("English", "Russian");
        /* providing our list to comboBox */
        langsBox.setItems(options);

        /* adding event to the remove all button */
        removeAll.setOnAction(event -> {
            quotesViewer.setText(""); // clear out everything
        });

        /* save quotes as text file */
        saveAsTxt.setOnAction(event -> {
            /* Get paragraphs as showing below */
            ObservableList<CharSequence> paragraph = quotesViewer.getParagraphs();
            /* Define an iterator to looping through this paragraph */
            Iterator<CharSequence> iter = paragraph.iterator();
            try{
                /* Declaring bufferedWriter to access file-write operations */
                BufferedWriter bf = new BufferedWriter(new FileWriter(new File("/home/"+System.getProperty("user.name")+"/Documents/myQuotes.txt")));
                // set default path for the storage of txt file as Documents folder
                while(iter.hasNext()){ // iterate
                    CharSequence seq = iter.next(); // over all the paragraphs
                    bf.append(seq); // append to the buffer
                    bf.newLine(); // after each iteration add new line
                }
                bf.flush(); // flush out the buffer
                bf.close(); // then close it for security purposes
            } catch (Exception e){ // cath errors
                System.out.println(e); // if there are some, show them
            }
        });

        /* adding event to apply button */
        apply.setOnAction(event -> {
            /* Using Singleton class for storing data that needs to be transfered */
            Settings parameters = Settings.getInstance(); // create a new instance
            // set necessary information to this instance
            parameters.setLang(langsBox.getValue() == null ? "English" : langsBox.getValue());
            parameters.setDropBookmarks(quotesViewer.getText().isEmpty());
        });
    }
}
