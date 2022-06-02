package ru.cw.dormitoryais;

import javafx.scene.control.Alert;

/**
 * Показывает сообщения об ошибках.
 */
public class ErrorMessage {
    public static void show(String text)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
