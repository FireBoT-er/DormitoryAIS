package ru.cw.dormitoryais.Controllers;

import javafx.event.Event;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.cw.dormitoryais.DatabaseWorker;
import ru.cw.dormitoryais.ErrorMessage;

import java.io.File;
import java.sql.SQLException;
import java.util.Random;

public class SettingsWindowController {
    private Stage dialogStage;

    /**
     * Устанавливает сцену для данного окна. <br>
     * Использовать после получения экземпляра, иначе окно не откроется.
     * @param addStage сцена
     */
    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
        this.dialogStage.setOnCloseRequest(event -> cancelB_Click(event));
    }

    public TextField dbFileName;
    public Button dbFileChooseButton;
    public Hyperlink aboutButton;
    public Button cancelB;

    private boolean atStartUp;
    private boolean thingsHaveChangedForMe = false;

    /**
     * Устанавливает значения для окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     * @param atStartUp указатель на источник
     */
    public void setParams(boolean atStartUp){
        this.atStartUp = atStartUp;
        dbFileChooseButton.requestFocus();

        if (atStartUp){
            aboutButton.setVisible(false);
            cancelB.setText("Выход");
        }
        else{
            dbFileName.setText(DatabaseWorker.savedPath);
        }
    }

    /**
     * Загружает файл базы данных с устройства.
     */
    public void dbFileChooseButton_Click(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Открыть файл базы данных");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Файл базы данных", "*.fdb");
        fileChooser.getExtensionFilters().addAll(filter);
        File file = fileChooser.showOpenDialog(dialogStage);
        if (file != null) {
            dbFileName.setText(file.getAbsolutePath());
            thingsHaveChangedForMe = true;
        }
    }

    /**
     * Выводит информацию об авторе.
     */
    public void aboutButton_LinkClicked()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);

        if (new Random().nextInt(20) == 0){
            alert.setTitle("О программе");
            alert.setContentText("""
                    Кинокомпания "20-ый век Фокс" представляет:
                    Совместно с Marvel Entertainment
                    В главных ролях:
                    Цензура
                    Злодей британец
                    Так себе шутник
                    Какой-то мужик из компьютерной графики
                    А также: Халявное камео
                    Сценаристы: Недопонятные гении
                    
                    Привет, я:
                    Емельянов Владислав
                    vlademel2016@yandex.ru
                    МИВлГУ, ФИТР, ПИн-119
                    2022 г.""");
        }
        else{
            alert.setTitle("Разработчик");
            alert.setContentText("Емельянов Владислав\nvlademel2016@yandex.ru\nМИВлГУ, ФИТР, ПИн-119\n2022 г.");
        }

        alert.showAndWait();
    }

    public String dialogResult = "None";

    /**
     * Загружает информацию из файла базы данных, если загружен новый.
     */
    public void okB_Click() throws SQLException {
        if (!dbFileName.getText().isBlank()){
            if (thingsHaveChangedForMe){
                if (DatabaseWorker.load(dbFileName.getText())){
                    dialogResult = "OK";
                    dialogStage.close();
                }
            }
            else{
                dialogStage.close();
            }
        }
        else{
            ErrorMessage.show("Выберите файл");
        }
    }

    /**
     * Закрывает окно после предупреждения.
     */
    public void cancelB_Click(Event event)
    {
        if (atStartUp){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Выход");
            alert.setHeaderText(null);
            alert.setContentText("Вы действительно хотите выйти?");
            alert.getButtonTypes().clear();
            alert.getButtonTypes().add(new ButtonType("Да", ButtonType.YES.getButtonData()));
            alert.getButtonTypes().add(new ButtonType("Нет", ButtonType.NO.getButtonData()));
            alert.showAndWait();

            if (alert.getResult().getText().equals("Да")){
                System.exit(0);
            }
            else{
                event.consume();
            }
        }
        else{
            dialogStage.close();
        }
    }
}
