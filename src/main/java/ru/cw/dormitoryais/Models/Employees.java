package ru.cw.dormitoryais.Models;

import javafx.beans.property.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.cw.dormitoryais.Controllers.MainWindowController;
import ru.cw.dormitoryais.Controllers.PhotoViewController;
import ru.cw.dormitoryais.Program;

import java.io.IOException;

public class Employees {
    private IntegerProperty id;
    private StringProperty surname;
    private StringProperty name;
    private StringProperty patronymic;
    private Button photo;
    private byte[] photoData;
    private StringProperty position;
    private BooleanProperty isWorkingNow;

    public Employees(Integer id, String surname, String name, String patronymic, Button photo, byte[] photoData, String position, Boolean isWorkingNow) {
        this.id = new SimpleIntegerProperty(id);
        this.surname = new SimpleStringProperty(surname);
        this.name = new SimpleStringProperty(name);
        this.patronymic = new SimpleStringProperty(patronymic);

        String fullName = surname + " " + name + " " + patronymic;
        photo.setOnAction(event -> {
            try {
                photoView(photoData, fullName);
            } catch (Exception ignored) { }
        });
        this.photo = photo;
        this.photoData = photoData;

        this.position = new SimpleStringProperty(position);
        this.isWorkingNow = new SimpleBooleanProperty(isWorkingNow);
    }

    /**
     * Открывает окно с фотографией сотрудника.
     * @param image изображение
     * @param fullName имя сотрудника
     */
    private void photoView(byte[] image, String fullName) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainWindowController.class.getResource("photoView.fxml"));

        Parent page = loader.load();
        Stage addStage = new Stage();
        addStage.setTitle(fullName);
        addStage.initOwner(Program.getPrimaryStage());

        Scene scene = new Scene(page);
        addStage.setScene(scene);
        addStage.setResizable(false);
        addStage.getIcons().add(new Image(Program.class.getResourceAsStream("quarantine.png")));

        PhotoViewController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams(image);
        addStage.show();
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getSurname() {
        return surname.get();
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPatronymic() {
        return patronymic.get();
    }

    public StringProperty patronymicProperty() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic.set(patronymic);
    }

    public Button getPhoto() {
        return photo;
    }

    public void setPhoto(Button photo) {
        this.photo = photo;
    }

    public byte[] getPhotoData() {
        return photoData;
    }

    public void setPhotoData(byte[] photoData) {
        this.photoData = photoData;
    }

    public String getPosition() {
        return position.get();
    }

    public StringProperty positionProperty() {
        return position;
    }

    public void setPosition(String position) {
        this.position.set(position);
    }

    public boolean isIsWorkingNow() {
        return isWorkingNow.get();
    }

    public BooleanProperty isWorkingNowProperty() {
        return isWorkingNow;
    }

    public void setIsWorkingNow(boolean isWorkingNow) {
        this.isWorkingNow.set(isWorkingNow);
    }
}
