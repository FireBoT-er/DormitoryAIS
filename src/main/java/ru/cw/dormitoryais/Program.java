package ru.cw.dormitoryais;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.cw.dormitoryais.Controllers.MainWindowController;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Базовый класс.
 */
public class Program extends Application {
    private static Stage primaryStage;
    /**
     * Передаёт родительскую сцену для модальных окон.
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        Parent root = FXMLLoader.load(MainWindowController.class.getResource("mainWindow.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Общежитие");
        primaryStage.setMinWidth(1106);
        primaryStage.setMinHeight(540);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("quarantine.png")));
        primaryStage.setOnCloseRequest(event -> {
            try {
                DatabaseWorker.closeConnection();
            } catch (SQLException ignored) { }
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}