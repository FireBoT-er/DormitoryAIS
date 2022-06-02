package ru.cw.dormitoryais.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.cw.dormitoryais.DatabaseWorker;
import ru.cw.dormitoryais.DeleteMessage;
import ru.cw.dormitoryais.ErrorMessage;
import ru.cw.dormitoryais.Models.Rooms;

import java.sql.*;

public class RoomInfoController {
    private Stage dialogStage;

    /**
     * Устанавливает сцену для данного окна. <br>
     * Использовать после получения экземпляра, иначе окно не откроется.
     * @param addStage сцена
     */
    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    public Spinner<Integer> roomNumberPicker;
    public RadioButton beds2;
    public RadioButton beds3;
    public RadioButton beds4;
    public Button deleteB;

    @FXML
    void initialize() {
        SpinnerValueFactory<Integer> roomNumberPickerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(101, 999);
        roomNumberPicker.setValueFactory(roomNumberPickerValueFactory);
    }

    private int roomID = 0;
    private int previousRoomNumber = 0;
    private int beds = 2;

    /**
     * Устанавливает значения для пустого окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     */
    public void setParams(){
        deleteB.setVisible(false);
    }

    /**
     * Устанавливает значения для заполненного данными окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     * @param room заполняющая окно информация
     */
    public void setParams(Rooms room){
        this.roomID = room.getId();
        roomNumberPicker.getValueFactory().setValue(room.getRoomNumber());
        previousRoomNumber = room.getRoomNumber();
        beds = room.getBeds();

        switch (beds)
        {
            case 2:
                beds2.setSelected(true);
                break;
            case 3:
                beds3.setSelected(true);
                break;
            case 4:
                beds4.setSelected(true);
                break;
        }
    }

    /**
     * Обновляет переменную beds.
     */
    public void beds2_CheckedChanged()
    {
        beds = 2;
    }

    /**
     * Обновляет переменную beds.
     */
    public void beds3_CheckedChanged()
    {
        beds = 3;
    }

    /**
     * Обновляет переменную beds.
     */
    public void beds4_CheckedChanged()
    {
        beds = 4;
    }

    public String dialogResult = "None";

    /**
     * Удаляет информацию из базы данных после второго предупреждения.
     * Не удаляет, если комната заселена.
     */
    public void deleteB_Click() throws SQLException {
        String warning = DeleteMessage.yesNo("Удаление комнаты");

        if (warning.equals("Да"))
        {
            String result = DeleteMessage.yesNo("Удаление комнаты", "Вы уверены, что хотите удалить информацию о выбранной комнате?");

            if (result.equals("Да"))
            {
                PreparedStatement ps = DatabaseWorker.connection.prepareStatement("SELECT TRUE FROM RDB$DATABASE WHERE EXISTS (SELECT * FROM check_ins_outs WHERE id_room = ?)");
                ps.setInt(1, roomID);

                ResultSet resultSet = ps.executeQuery();

                boolean condition;
                if (!resultSet.next()){
                    condition = false;
                }
                else{
                    condition = resultSet.getBoolean(1);
                }

                if (condition)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Удаление комнаты");
                    alert.setHeaderText(null);
                    alert.setContentText("Удаление невозможно: в данной комнате ещё проживают студенты");
                    alert.showAndWait();

                    dialogResult = "Retry";
                }
                else
                {
                    ps = DatabaseWorker.connection.prepareStatement("DELETE FROM rooms WHERE id_room = ?");
                    ps.setInt(1, roomID);
                    ps.executeUpdate();

                    DeleteMessage.ok("Удаление комнаты");

                    dialogResult = "Abort";
                }

                dialogStage.close();
            }
        }
    }

    /**
     * Отправляет информацию в базу данных после проверки заполненности всех полей.
     * Не отправляет, если заполнено неправильными данными.
     */
    public void okB_Click() throws SQLException {
        if (roomNumberPicker.getValue()%100==0)
        {
            ErrorMessage.show("Комната с таким номером не может существовать");
        }
        else
        {
            PreparedStatement ps = DatabaseWorker.connection.prepareStatement("SELECT TRUE FROM rooms WHERE room_number = ?");
            ps.setInt(1, roomNumberPicker.getValue());

            ResultSet resultSet = ps.executeQuery();

            boolean condition;
            if (!resultSet.next()){
                condition = false;
            }
            else{
                condition = resultSet.getBoolean(1);
            }

            if (condition && previousRoomNumber != roomNumberPicker.getValue())
            {
                ErrorMessage.show("Комната с таким номером уже существует");
                return;
            }

            ps = DatabaseWorker.connection.prepareStatement("SELECT COUNT(*) FROM rooms " +
                                                 "LEFT JOIN check_ins_outs ON check_ins_outs.id_room = rooms.id_room " +
                                                 "WHERE check_ins_outs.id_room = ? " +
                                                 "AND check_ins_outs.check_out_date >= CURRENT_DATE");
            ps.setInt(1, roomID);

            resultSet = ps.executeQuery();
            resultSet.next();

            if (beds < resultSet.getInt(1))
            {
                ErrorMessage.show("Количество мест не может быть меньше количества заселённых студентов");
                return;
            }

            if (roomID == 0)
            {
                ps = DatabaseWorker.connection.prepareStatement("INSERT INTO rooms (room_number, beds) VALUES (?, ?)");
            }
            else
            {
                ps = DatabaseWorker.connection.prepareStatement("UPDATE rooms SET room_number = ?, beds = ? WHERE id_room = ?");
                ps.setInt(3, roomID);
            }

            ps.setInt(1, roomNumberPicker.getValue());
            ps.setInt(2, beds);

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
