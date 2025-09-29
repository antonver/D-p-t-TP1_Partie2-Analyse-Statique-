package lanceur;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Classe principale pour lancer l'application JavaFX.
public class AppFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // On charge le fichier FXML qui décrit l'interface.
        Parent root = FXMLLoader.load(getClass().getResource("/interface.fxml"));
        primaryStage.setTitle("Analyseur de Code Java");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}