package ru.cw.dormitoryais.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Rooms {
    private IntegerProperty id;
    private IntegerProperty roomNumber;
    private IntegerProperty beds;

    public Rooms(Integer id, Integer roomNumber, Integer beds) {
        this.id = new SimpleIntegerProperty(id);
        this.roomNumber = new SimpleIntegerProperty(roomNumber);
        this.beds = new SimpleIntegerProperty(beds);
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

    public int getRoomNumber() {
        return roomNumber.get();
    }

    public IntegerProperty roomNumberProperty() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber.set(roomNumber);
    }

    public int getBeds() {
        return beds.get();
    }

    public IntegerProperty bedsProperty() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds.set(beds);
    }
}
