package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Setting Up the Main Window of the program
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Forismatic Client");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 823, 534));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    /* The class ConnectDB is dedicated only for database connection */
    public static class ConnectDB {
        public Connection get_connection() {
            Connection connection = null;
            String host = "localhost";
            String port = "5432";
            String db_name = "postgres";
            String username = "postgres";
            String password = "1111";
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection("jdbc:postgresql://"+host+":"+port+"/"+db_name, username, password);
            }catch(Exception e) {
                System.out.println(e);
            }
            return connection;
        }
    }
}
