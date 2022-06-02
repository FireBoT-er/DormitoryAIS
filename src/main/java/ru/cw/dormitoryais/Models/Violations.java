package ru.cw.dormitoryais.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

public class Violations {
    private IntegerProperty id;
    private StringProperty description;
    private StringProperty punishment;
    private LocalDateTime dateTime;

    public Violations(Integer id, String description, String punishment, LocalDateTime dateTime) {
        this.id = new SimpleIntegerProperty(id);
        this.description = new SimpleStringProperty(description);
        this.punishment = new SimpleStringProperty(punishment);
        this.dateTime = dateTime;
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

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getPunishment() {
        return punishment.get();
    }

    public StringProperty punishmentProperty() {
        return punishment;
    }

    public void setPunishment(String punishment) {
        this.punishment.set(punishment);
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
