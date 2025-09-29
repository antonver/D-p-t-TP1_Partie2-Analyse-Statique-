package graphe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Classe principale pour lancer l'application JavaFX du graphe.
public class AppGrapheFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/interface_graphe.fxml"));
        primaryStage.setTitle("Analyseur de Graphe d'Appel");
        primaryStage.setScene(new Scene(root, 900, 700));
        primaryStage.setOnCloseRequest(e -> System.exit(0)); // Pour bien fermer GraphStream
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}