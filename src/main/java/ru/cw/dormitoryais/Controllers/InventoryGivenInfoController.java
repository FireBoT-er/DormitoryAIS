package ru.cw.dormitoryais.Controllers;

import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.cw.dormitoryais.DatabaseWorker;
import ru.cw.dormitoryais.DeleteMessage;
import ru.cw.dormitoryais.Models.InventoryGiven;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class InventoryGivenInfoController {
    private Stage dialogStage;

    /**
     * Устанавливает сцену для данного окна. <br>
     * Использовать после получения экземпляра, иначе окно не откроется.
     * @param addStage сцена
     */
    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    public Label countLabel;
    public Spinner<Integer> countPicker;
    public DatePicker issueDatePicker;
    public DatePicker turnInDatePicker;
    public Button deleteB;

    private int studentID;
    private int inventoryID;
    private int issuedInventoryID = 0;

    /**
     * Устанавливает значения для пустого окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     * @param inventoryID идентификатор выдаваемого вида инвентаря
     * @param studentID идентификатор получающего инвентарь студента
     * @param maxCount максимально возможное количество инвентаря для выдачи
     */
    public void setParams(int inventoryID, int studentID, int maxCount) throws SQLException {
        this.studentID = studentID;
        this.inventoryID = inventoryID;

        SpinnerValueFactory<Integer> countPickerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxCount);
        countPicker.setValueFactory(countPickerValueFactory);

        PreparedStatement ps = DatabaseWorker.connection.prepareStatement("SELECT check_out_date FROM check_ins_outs WHERE id_student = ?");
        ps.setInt(1, studentID);

        ResultSet resultSet = ps.executeQuery();
        resultSet.next();

        issueDatePicker.setValue(LocalDate.now());
        turnInDatePicker.setValue(resultSet.getDate(1).toLocalDate());

        deleteB.setVisible(false);
    }

    /**
     * Устанавливает значения для заполненного данными окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     * @param inventoryGiven заполняющая окно информация
     * @param studentID идентификатор получающего инвентарь студента
     * @param maxCount максимально возможное количество инвентаря для выдачи
     */
    public void setParams(InventoryGiven inventoryGiven, int studentID, int maxCount){
        this.studentID = studentID;
        this.inventoryID = inventoryGiven.getInventoryID();
        this.issuedInventoryID = inventoryGiven.getId();

        countLabel.setText("Выданное количество:");

        SpinnerValueFactory<Integer> countPickerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxCount+inventoryGiven.getCount(), inventoryGiven.getCount());
        countPicker.setValueFactory(countPickerValueFactory);

        issueDatePicker.setValue(inventoryGiven.getIssueDate());
        turnInDatePicker.setValue(inventoryGiven.getTurnInDate());
    }

    public String dialogResult = "None";

    /**
     * Удаляет информацию из базы данных после второго предупреждения.
     */
    public void deleteB_Click() throws SQLException {
        String warning = DeleteMessage.yesNo("Удаление информации о выдаче инвентаря");

        if (warning.equals("Да"))
        {
            String result = DeleteMessage.yesNo("Удаление информации о выдаче инвентаря", "Вы уверены, что хотите удалить информацию о данной выдаче инвентаря?");

            if (result.equals("Да"))
            {
                PreparedStatement ps = DatabaseWorker.connection.prepareStatement("DELETE FROM issued_inventory WHERE id_issued_inventory = ?");
                ps.setInt(1, issuedInventoryID);
                ps.executeUpdate();

                DeleteMessage.ok("Удаление информации о выдаче инвентаря");

                dialogResult = "Abort";
                dialogStage.close();
            }
        }
    }

    /**
     * Отправляет информацию в базу данных после проверки заполненности всех полей.
     */
    public void okB_Click() throws SQLException {
        PreparedStatement ps;

        if (issuedInventoryID == 0)
        {
            ps = DatabaseWorker.connection.prepareStatement("INSERT INTO issued_inventory (id_student, id_inventory, issue_date, turn_in_date, iss_count) VALUES (?, ?, ?, ?, ?)");
        }
        else
        {
            ps = DatabaseWorker.connection.prepareStatement("UPDATE issued_inventory SET id_student = ?, id_inventory = ?, issue_date = ?, turn_in_date = ?, iss_count = ? WHERE id_issued_inventory = ?");
            ps.setInt(6, issuedInventoryID);
        }

        ps.setInt(1, studentID);
        ps.setInt(2, inventoryID);
        ps.setDate(3, Date.valueOf(LocalDate.of(issueDatePicker.getValue().getYear(), issueDatePicker.getValue().getMonth().getValue(), issueDatePicker.getValue().getDayOfMonth())));
        ps.setDate(4, Date.valueOf(LocalDate.of(turnInDatePicker.getValue().getYear(), turnInDatePicker.getValue().getMonth().getValue(), turnInDatePicker.getValue().getDayOfMonth())));
        ps.setInt(5, countPicker.getValue());

        ps.executeUpdate();

        dialogResult = "OK";
        dialogStage.close();
    }

    /**
     * Закрывает окно.
     */
    public void cancelB_Click()
    {
        dialogStage.close();
    }
}
