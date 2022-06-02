package ru.cw.dormitoryais.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import java.time.LocalDate;

public class Students {
    private IntegerProperty id;
    private StringProperty surname;
    private StringProperty name;
    private StringProperty patronymic;
    private StringProperty sex;
    private LocalDate birthday;
    private Button photo;
    private byte[] photoData;
    private StringProperty recordBookNumber;
    private IntegerProperty roomNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private IntegerProperty invCount;
    private LocalDate invIssueDate;
    private LocalDate invTurnInDate;

    /**
     * Стандартный конструктор.
     * @see #Students(Integer, String, String, String, boolean, LocalDate, Button, byte[], String, Integer, Integer, LocalDate, LocalDate)
     */
    public Students(Integer id, String surname, String name, String patronymic, boolean sex, LocalDate birthday, Button photo, byte[] photoData, String recordBookNumber, Integer roomNumber, LocalDate checkInDate, LocalDate checkOutDate) {
        this.id = new SimpleIntegerProperty(id);
        this.surname = new SimpleStringProperty(surname);
        this.name = new SimpleStringProperty(name);
        this.patronymic = new SimpleStringProperty(patronymic);
        this.sex = new SimpleStringProperty(sex?"Мужской":"Женский");
        this.birthday = birthday;

        String fullName = surname + " " + name + " " + patronymic;
        photo.setOnAction(event -> {
            try {
                photoView(photoData, fullName);
            } catch (Exception ignored) { }
        });
        this.photo = photo;
        this.photoData = photoData;

        this.recordBookNumber = new SimpleStringProperty(recordBookNumber);
        this.roomNumber = new SimpleIntegerProperty(roomNumber);
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    /**
     * Конструктор для выдачи инвентаря студенту.
     * @see #Students(Integer, String, String, String, boolean, LocalDate, Button, byte[], String, Integer, LocalDate, LocalDate)
     */
    public Students(Integer id, String surname, String name, String patronymic, boolean sex, LocalDate birthday, Button photo, byte[] photoData, String recordBookNumber, Integer roomNumber, Integer invCount, LocalDate invIssueDate, LocalDate invTurnInDate) {
        this(id, surname, name, patronymic, sex, birthday, photo, photoData, recordBookNumber, roomNumber, null, null);

        this.invCount = new SimpleIntegerProperty(invCount);
        this.invIssueDate = invIssueDate;
        this.invTurnInDate = invTurnInDate;
    }

    /**
     * Открывает окно с фотографией студента.
     * @param image изображение
     * @param fullName имя студента
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

    public String getSex() {
        return sex.get();
    }

    public StringProperty sexProperty() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex.set(sex);
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
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

    public String getRecordBookNumber() {
        return recordBookNumber.get();
    }

    public StringProperty recordBookNumberProperty() {
        return recordBookNumber;
    }

    public void setRecordBookNumber(String recordBookNumber) {
        this.recordBookNumber.set(recordBookNumber);
    }

    public int getRoomNumber() {
        return roomNumber.get();
    }

    public IntegerProperty roomNumberProperty() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber.set(roomNumber);
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getInvCount() {
        return invCount.get();
    }

    public IntegerProperty invCountProperty() {
        return invCount;
    }

    public void setInvCount(int invCount) {
        this.invCount.set(invCount);
    }

    public LocalDate getInvIssueDate() {
        return invIssueDate;
    }

    public void setInvIssueDate(LocalDate invIssueDate) {
        this.invIssueDate = invIssueDate;
    }

    public LocalDate getInvTurnInDate() {
        return invTurnInDate;
    }

    public void setInvTurnInDate(LocalDate invTurnInDate) {
        this.invTurnInDate = invTurnInDate;
    }
}
