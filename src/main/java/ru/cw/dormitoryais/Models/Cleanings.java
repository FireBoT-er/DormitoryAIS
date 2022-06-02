package ru.cw.dormitoryais.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

public class Cleanings {
    private IntegerProperty id;
    private LocalDateTime dateTime;
    private StringProperty cleaned;

    public Cleanings(Integer id, LocalDateTime dateTime, String cleaned) {
        this.id = new SimpleIntegerProperty(id);
        this.dateTime = dateTime;
        this.cleaned = new SimpleStringProperty(cleaned);
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getCleaned() {
        return cleaned.get();
    }

    public StringProperty cleanedProperty() {
        return cleaned;
    }

    public void setCleaned(String cleaned) {
        this.cleaned.set(cleaned);
    }
}
