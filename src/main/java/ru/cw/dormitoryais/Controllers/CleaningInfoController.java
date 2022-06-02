package ru.cw.dormitoryais.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import ru.cw.dormitoryais.*;
import ru.cw.dormitoryais.Models.Cleaners;
import ru.cw.dormitoryais.Models.Cleanings;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class CleaningInfoController {
    private Stage dialogStage;

    /**
     * Устанавливает сцену для данного окна. <br>
     * Использовать после получения экземпляра, иначе окно не откроется.
     * @param addStage сцена
     */
    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    public TextArea cleanedText;
    public Label cleanedLength;
    public DatePicker datePicker;
    public Spinner<Integer> timeHourPicker;
    public Spinner<Integer> timeMinutePicker;
    public TableView<Cleaners> employeesList;
    public TableColumn<Cleaners, String> cleanersFullNameColumn;
    public Button deleteB;

    @FXML
    void initialize() {
        TextFormatter cleanedFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile(".{0,500}").matcher(change.getControlNewText()).matches() ? change : null);
        cleanedText.setTextFormatter(cleanedFormatter);

        SpinnerValueFactory<Integer> timeHourPickerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
        timeHourPicker.setValueFactory(timeHourPickerValueFactory);
        timeHourPicker.getValueFactory().setWrapAround(true);
        SpinnerValueFactory<Integer> timeMinutePickerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        timeMinutePicker.setValueFactory(timeMinutePickerValueFactory);
        timeMinutePicker.getValueFactory().setWrapAround(true);

        cleanersFullNameColumn.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());

        areThereAnybodyWhoAreNotWorkingNow = false;
    }

    private int cleaningID = 0;

    /**
     * Устанавливает значения для пустого окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     */
    public void setParams(){
        timeHourPicker.getValueFactory().setValue(LocalDateTime.now().getHour());
        timeMinutePicker.getValueFactory().setValue(LocalDateTime.now().getMinute());

        deleteB.setVisible(false);
    }

    /**
     * Устанавливает значения для заполненного данными окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     * @param cleaning заполняющая окно информация
     */
    public void setParams(Cleanings cleaning) throws SQLException {
        this.cleaningID = cleaning.getId();

        cleanedText.setText(cleaning.getCleaned());
        cleanedText_TextChanged();

        datePicker.setValue(cleaning.getDateTime().toLocalDate());

        timeHourPicker.getValueFactory().setValue(cleaning.getDateTime().getHour());
        timeMinutePicker.getValueFactory().setValue(cleaning.getDateTime().getMinute());

        PreparedStatement ps = DatabaseWorker.connection.prepareStatement("SELECT id_employee FROM performed_cleanings WHERE id_cleaning = ?");
        ps.setInt(1, cleaningID);

        ResultSet resultSet = ps.executeQuery();

        while (resultSet.next())
        {
            employeesListID.add(resultSet.getInt(1));
        }

        employeesListFill();
    }

    /**
     * Изменяет информацию о количестве введённых символов.
     */
    public void cleanedText_TextChanged()
    {
        cleanedLength.setText(cleanedText.getText().length() + "/500");
    }

    public static ArrayList<Integer> employeesListID = new ArrayList<>();

    public static boolean areThereAnybodyWhoAreNotWorkingNow;

    private void employeesListFill() throws SQLException {
        PreparedStatement ps = DatabaseWorker.connection.prepareStatement("SELECT surname||' '||name||' '||patronymic, is_working_now FROM employees WHERE id_employee = ?");
        ResultSet resultSet;
        for (var employeeID : employeesListID) {
            ps.clearParameters();
            ps.setInt(1, employeeID);

            resultSet = ps.executeQuery();

            while (resultSet.next()){
                employeesList.getItems().add(new Cleaners(employeeID, resultSet.getString(1)));

                if (resultSet.getBoolean(2) && !areThereAnybodyWhoAreNotWorkingNow){
                    areThereAnybodyWhoAreNotWorkingNow = true;
                }
            }
        }
    }

    public void employeesList_KeyDown(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.DELETE)
        {
            employeesDetachB_Click();
        }
    }

    /**
     * Открывает окно для добавления сотрудников к данной уборке.
     * Не открывает, если сотрудников нет в базе данных или все уже добавлены.
     */
    public void employeesAddB_Click() throws SQLException, IOException {
        ResultSet resultSet;
        if (areThereAnybodyWhoAreNotWorkingNow)
        {
            resultSet = DatabaseWorker.statement.executeQuery("SELECT COUNT(*) FROM employees");
        }
        else
        {
            resultSet = DatabaseWorker.statement.executeQuery("SELECT COUNT(*) FROM employees WHERE is_working_now = TRUE");
        }

        resultSet.next();
        int count = resultSet.getInt(1);
        if (count == 0)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("Нет информации о сотрудниках");
            alert.showAndWait();

            return;
        }
        else if ((count- employeesList.getItems().size()) == 0)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("Вы уже добавили всех сотрудников");
            alert.showAndWait();

            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("multifunctionalTableWindow.fxml"));

        Parent page = loader.load();
        Stage addStage = new Stage();
        addStage.setTitle("Выберите сотрудников");
        addStage.initOwner(Program.getPrimaryStage());
        addStage.setMinWidth(500);
        addStage.setMinHeight(150);

        Scene scene = new Scene(page);
        addStage.setScene(scene);
        addStage.getIcons().add(new Image(Program.class.getResourceAsStream("quarantine.png")));

        MultifunctionalTableWindowController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams(TableState.Select, TableType.Cleaners);
        addStage.showAndWait();

        if (controller.dialogResult.equals("OK")){
            employeesList.getItems().clear();
            employeesListFill();
        }
    }

    /**
     * Удаляет сотрудника из списка убирающихся.
     */
    public void employeesDetachB_Click()
    {
        if (employeesList.getSelectionModel().getSelectedItem() == null){
            return;
        }

        employeesListID.remove(Integer.valueOf(employeesList.getSelectionModel().getSelectedItem().getId()));
        employeesList.getItems().remove(employeesList.getSelectionModel().getSelectedItem());
    }

    public String dialogResult = "None";

    /**
     * Удаляет информацию из базы данных после второго предупреждения.
     */
    public void deleteB_Click() throws SQLException {
        String warning = DeleteMessage.yesNo("Удаление уборки");

        if (warning.equals("Да"))
        {
            String result = DeleteMessage.yesNo("Удаление уборки", "Вы уверены, что хотите удалить информацию о данной уборке?\n\n" +
                                                                               "Также будет удалена информация о причастности к ней сотрудников.");

            if (result.equals("Да"))
            {
                PreparedStatement ps = DatabaseWorker.connection.prepareStatement("DELETE FROM cleanings WHERE id_cleaning = ?");
                ps.setInt(1, cleaningID);
                ps.executeUpdate();

                ps = DatabaseWorker.connection.prepareStatement("DELETE FROM performed_cleanings WHERE id_cleaning = ?");
                ps.setInt(1, cleaningID);
                ps.executeUpdate();

                DeleteMessage.ok("Удаление уборки");

                dialogResult = "Abort";
                cancelB_Click();
            }
        }
    }

    /**
     * Отправляет информацию в базу данных после проверки заполненности всех полей.
     */
    public void okB_Click() throws SQLException {
        if (cleanedText.getText().isBlank())
        {
            ErrorMessage.show("Не заполнено поле \"Убранные помещения и территории\"");
        }
        else if (datePicker.getValue() == null)
        {
            ErrorMessage.show("Не заполнено поле \"Дата уборки\"");
        }
        else if (employeesList.getItems().size() == 0)
        {
            ErrorMessage.show("Не добавлены исполнители");
        }
        else
        {
            PreparedStatement ps;

            if (cleaningID == 0)
            {
                ps = DatabaseWorker.connection.prepareStatement("INSERT INTO cleanings (date_time, cleaned) VALUES (?, ?)");

                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.of(datePicker.getValue().getYear(), datePicker.getValue().getMonth().getValue(), datePicker.getValue().getDayOfMonth(), timeHourPicker.getValue(), timeMinutePicker.getValue())));
                ps.setString(2, cleanedText.getText());

                ps.executeUpdate();
                
                ps = DatabaseWorker.connection.prepareStatement("INSERT INTO performed_cleanings (id_employee, id_cleaning) VALUES (?, (SELECT MAX(id_cleaning) FROM cleanings))");

                for (var employeeID : employeesListID) {
                    ps.clearParameters();
                    ps.setInt(1, employeeID);
                    ps.executeUpdate();
                }
            }
            else
            {
                ps = DatabaseWorker.connection.prepareStatement("UPDATE cleanings SET date_time = ?, cleaned = ? WHERE id_cleaning = ?");

                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.of(datePicker.getValue().getYear(), datePicker.getValue().getMonth().getValue(), datePicker.getValue().getDayOfMonth(), timeHourPicker.getValue(), timeMinutePicker.getValue())));
                ps.setString(2, cleanedText.getText());
                ps.setInt(3, cleaningID);

                ps.executeUpdate();

                ps = DatabaseWorker.connection.prepareStatement("DELETE FROM performed_cleanings WHERE id_cleaning = ?");
                ps.setInt(1, cleaningID);
                ps.executeUpdate();
                
                ps = DatabaseWorker.connection.prepareStatement("INSERT INTO performed_cleanings (id_employee, id_cleaning) VALUES (?, ?)");

                for (var employeeID : employeesListID) {
                    ps.clearParameters();
                    ps.setInt(1, employeeID);
                    ps.setInt(2, cleaningID);
                    ps.executeUpdate();
                }
            }

            dialogResult = "OK";
            cancelB_Click();
        }
    }

    /**
     * Закрывает окно и обнуляет статические переменные.
     */
    public void cancelB_Click()
    {
        CleaningInfoController.employeesListID.clear();
        CleaningInfoController.areThereAnybodyWhoAreNotWorkingNow = false;
        dialogStage.close();
    }
}
