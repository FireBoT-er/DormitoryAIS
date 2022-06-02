package ru.cw.dormitoryais.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

public class Visitors {
    private IntegerProperty id;
    private StringProperty surname;
    private StringProperty name;
    private StringProperty patronymic;
    private StringProperty phone;
    private LocalDateTime dateTime;
    private IntegerProperty studentID;
    private StringProperty visited;

    public Visitors(Integer id, String surname, String name, String patronymic, String phone, LocalDateTime dateTime, Integer studentID, String visited) {
        this.id = new SimpleIntegerProperty(id);
        this.surname = new SimpleStringProperty(surname);
        this.name = new SimpleStringProperty(name);
        this.patronymic = new SimpleStringProperty(patronymic);
        this.phone = new SimpleStringProperty(phone);
        this.dateTime = dateTime;
        this.studentID = new SimpleIntegerProperty(studentID);
        this.visited = new SimpleStringProperty(visited);
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

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getStudentID() {
        return studentID.get();
    }

    public IntegerProperty studentIDProperty() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID.set(studentID);
    }

    public String getVisited() {
        return visited.get();
    }

    public StringProperty visitedProperty() {
        return visited;
    }

    public void setVisited(String visited) {
        this.visited.set(visited);
    }
}
