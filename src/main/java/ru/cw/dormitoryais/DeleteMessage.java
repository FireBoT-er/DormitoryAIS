package ru.cw.dormitoryais;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Показывает базовые сообщения, выводимые при совершении операции удаления.
 */
public class DeleteMessage {
    /**
     * Выводит первое предупреждение о последствиях удаления.
     */
    public static String yesNo(String header)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(header);
        alert.setHeaderText(null);
        alert.setContentText("Внимание: удаление данных невозможно отменить. К этому следует прибегать только в крайнем случае. Продолжить?");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(new ButtonType("Да", ButtonType.YES.getButtonData()));
        alert.getButtonTypes().add(new ButtonType("Нет", ButtonType.NO.getButtonData()));
        alert.showAndWait();

        return alert.getResult().getText();
    }

    /**
     * Выводит второе предупреждение о последствиях удаления.
     */
    public static String yesNo(String header, String text)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(header);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(new ButtonType("Да", ButtonType.YES.getButtonData()));
        alert.getButtonTypes().add(new ButtonType("Нет", ButtonType.NO.getButtonData()));
        alert.showAndWait();

        return alert.getResult().getText();
    }

    /**
     * Выводит сообщение об успешном удалении.
     */
    public static void ok(String header)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(header);
        alert.setHeaderText(null);
        alert.setContentText("Данные успешно удалены");
        alert.showAndWait();
    }
}
