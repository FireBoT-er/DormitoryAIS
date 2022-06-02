package ru.cw.dormitoryais.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Inventory {
    private IntegerProperty id;
    private StringProperty type;
    private IntegerProperty count;
    private IntegerProperty countInStock;
    private IntegerProperty countIssued;

    public Inventory(Integer id, String type, Integer count, Integer countInStock, Integer countIssued) {
        this.id = new SimpleIntegerProperty(id);
        this.type = new SimpleStringProperty(type);
        this.count = new SimpleIntegerProperty(count);
        this.countInStock = new SimpleIntegerProperty(countInStock);
        this.countIssued = new SimpleIntegerProperty(countIssued);
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

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
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

    public int getCountInStock() {
        return countInStock.get();
    }

    public IntegerProperty countInStockProperty() {
        return countInStock;
    }

    public void setCountInStock(int countInStock) {
        this.countInStock.set(countInStock);
    }

    public int getCountIssued() {
        return countIssued.get();
    }

    public IntegerProperty countIssuedProperty() {
        return countIssued;
    }

    public void setCountIssued(int countIssued) {
        this.countIssued.set(countIssued);
    }
}
