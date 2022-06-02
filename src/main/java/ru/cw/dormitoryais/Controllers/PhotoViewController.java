package ru.cw.dormitoryais.Controllers;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;

public class PhotoViewController {
    private Stage dialogStage;

    /**
     * Устанавливает сцену для данного окна. <br>
     * Использовать после получения экземпляра, иначе окно не откроется.
     * @param addStage сцена
     */
    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    public ImageView photoViewPicture;

    /**
     * Устанавливает значения для заполненного данными окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     * @param image заполняющая окно информация
     */
    public void setParams(byte[] image){
        photoViewPicture.setImage(new Image(new ByteArrayInputStream(image)));
    }
}
