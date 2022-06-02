package ru.cw.dormitoryais.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.cw.dormitoryais.DatabaseWorker;
import ru.cw.dormitoryais.DeleteMessage;
import ru.cw.dormitoryais.ErrorMessage;
import ru.cw.dormitoryais.Models.Inventory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class InventoryInfoController {
    private Stage dialogStage;

    /**
     * Устанавливает сцену для данного окна. <br>
     * Использовать после получения экземпляра, иначе окно не откроется.
     * @param addStage сцена
     */
    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    public Spinner<Integer> countPicker;
    public TextField typeText;
    public Label typeLength;
    public Label countLabel;
    public Button deleteB;

    @FXML
    void initialize() {
        TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile(".{0,30}").matcher(change.getControlNewText()).matches() ? change : null);
        typeText.setTextFormatter(formatter);
    }

    private int inventoryID = 0;

    /**
     * Устанавливает значения для пустого окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     */
    public void setParams(){
        deleteB.setVisible(false);

        SpinnerValueFactory<Integer> countPickerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999);
        countPicker.setValueFactory(countPickerValueFactory);
    }

    /**
     * Устанавливает значения для заполненного данными окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     * @param inventory заполняющая окно информация
     */
    public void setParams(Inventory inventory){
        this.inventoryID = inventory.getId();
        this.typeText.setText(inventory.getType());
        typeText_TextChanged();

        this.countLabel.setText("Всего:");
        SpinnerValueFactory<Integer> countPickerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(inventory.getCountIssued(), 9999, inventory.getCount());
        countPicker.setValueFactory(countPickerValueFactory);
    }

    /**
     * Изменяет информацию о количестве введённых символов.
     */
    public void typeText_TextChanged()
    {
        typeLength.setText(typeText.getText().length() + "/30");
    }

    public String dialogResult = "None";

    /**
     * Удаляет информацию из базы данных после второго предупреждения.
     */
    public void deleteB_Click() throws SQLException {
        String warning = DeleteMessage.yesNo("Удаление вида инвентаря");

        if (warning.equals("Да"))
        {
            String result = DeleteMessage.yesNo("Удаление вида инвентаря",
                                                  "Вы уверены, что хотите удалить информацию о выбранном виде инвентаря?\n\n" +
                                                       "Также будет удалена информация обо всех случаях его выдачи.");

            if (result.equals("Да"))
            {
                PreparedStatement ps = DatabaseWorker.connection.prepareStatement("DELETE FROM inventory WHERE id_inventory = ?");
                ps.setInt(1, inventoryID);
                ps.executeUpdate();

                ps = DatabaseWorker.connection.prepareStatement("DELETE FROM issued_inventory WHERE id_inventory = ?");
                ps.setInt(1, inventoryID);
                ps.executeUpdate();

                DeleteMessage.ok("Удаление вида инвентаря");

                dialogResult = "Abort";
                dialogStage.close();
            }
        }
    }

    /**
     * Отправляет информацию в базу данных после проверки заполненности всех полей.
     */
    public void okB_Click() throws SQLException {
        if (typeText.getText().isBlank())
        {
            ErrorMessage.show("Не заполнено поле \"Название\"");
        }
        else
        {
            PreparedStatement ps;

            if (inventoryID == 0)
            {
                ps = DatabaseWorker.connection.prepareStatement("INSERT INTO inventory (inv_type, inv_count) VALUES (?, ?)");
            }
            else
            {
                ps = DatabaseWorker.connection.prepareStatement("UPDATE inventory SET inv_type = ?, inv_count = ? WHERE id_inventory = ?");
                ps.setInt(3, inventoryID);
            }

            ps.setString(1, typeText.getText());
            ps.setInt(2, countPicker.getValue());

            ps.executeUpdate();

            dialogResult = "OK";
            dialogStage.close();
        }
    }

    /**
     * Закрывает окно.
     */
    public void cancelB_Click()
    {
        dialogStage.close();
    }
}
