package ru.cw.dormitoryais.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class InventoryGiven {
    private IntegerProperty id;
    private IntegerProperty count;
    private LocalDate issueDate;
    private LocalDate turnInDate;
    private IntegerProperty inventoryID;
    private StringProperty type;

    public InventoryGiven(Integer id, Integer count, LocalDate issueDate, LocalDate turnInDate) {
        this.id = new SimpleIntegerProperty(id);
        this.count = new SimpleIntegerProperty(count);
        this.issueDate = issueDate;
        this.turnInDate = turnInDate;
    }

    public InventoryGiven(Integer inventoryID, String type, Integer id, Integer count, LocalDate issueDate, LocalDate turnInDate) {
        this.inventoryID = new SimpleIntegerProperty(inventoryID);
        this.type = new SimpleStringProperty(type);
        this.id = new SimpleIntegerProperty(id);
        this.count = new SimpleIntegerProperty(count);
        this.issueDate = issueDate;
        this.turnInDate = turnInDate;
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

    public int getCount() {
        return count.get();
    }

    public IntegerProperty countProperty() {
        return count;
    }

    public void setCount(int count) {
        this.count.set(count);
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getTurnInDate() {
        return turnInDate;
    }

    public void setTurnInDate(LocalDate turnInDate) {
        this.turnInDate = turnInDate;
    }

    public int getInventoryID() {
        return inventoryID.get();
    }

    public IntegerProperty inventoryIDProperty() {
        return inventoryID;
    }

    public void setInventoryID(int inventoryID) {
        this.inventoryID.set(inventoryID);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }
}
