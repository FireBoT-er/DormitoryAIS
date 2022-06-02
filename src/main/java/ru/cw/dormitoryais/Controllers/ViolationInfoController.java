package ru.cw.dormitoryais.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.cw.dormitoryais.DatabaseWorker;
import ru.cw.dormitoryais.DeleteMessage;
import ru.cw.dormitoryais.ErrorMessage;
import ru.cw.dormitoryais.Models.Violations;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class ViolationInfoController {
    private Stage dialogStage;

    /**
     * Устанавливает сцену для данного окна. <br>
     * Использовать после получения экземпляра, иначе окно не откроется.
     * @param addStage сцена
     */
    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    public TextArea descriptionText;
    public Label descriptionLength;
    public TextArea punishmentText;
    public Label punishmentLength;
    public DatePicker datePicker;
    public Spinner<Integer> timeHourPicker;
    public Spinner<Integer> timeMinutePicker;
    public Button deleteB;

    @FXML
    void initialize() {
        TextFormatter descriptionFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile(".{0,500}").matcher(change.getControlNewText()).matches() ? change : null);
        descriptionText.setTextFormatter(descriptionFormatter);
        TextFormatter punishmentFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile(".{0,500}").matcher(change.getControlNewText()).matches() ? change : null);
        punishmentText.setTextFormatter(punishmentFormatter);

        SpinnerValueFactory<Integer> timeHourPickerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
        timeHourPicker.setValueFactory(timeHourPickerValueFactory);
        timeHourPicker.getValueFactory().setWrapAround(true);
        SpinnerValueFactory<Integer> timeMinutePickerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        timeMinutePicker.setValueFactory(timeMinutePickerValueFactory);
        timeMinutePicker.getValueFactory().setWrapAround(true);
    }

    private int studentID;
    private int violationID = 0;

    /**
     * Устанавливает значения для пустого окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     * @param studentID идентификатор студента-нарушителя
     */
    public void setParams(int studentID){
        this.studentID = studentID;

        timeHourPicker.getValueFactory().setValue(LocalDateTime.now().getHour());
        timeMinutePicker.getValueFactory().setValue(LocalDateTime.now().getMinute());

        deleteB.setVisible(false);
    }

    /**
     * Устанавливает значения для заполненного данными окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     * @param studentID идентификатор студента-нарушителя
     * @param violation заполняющая окно информация
     */
    public void setParams(int studentID, Violations violation) throws SQLException {
        this.studentID = studentID;
        this.violationID = violation.getId();

        descriptionText.setText(violation.getDescription());
        descriptionText_TextChanged();
        punishmentText.setText(violation.getPunishment());
        punishmentText_TextChanged();

        datePicker.setValue(violation.getDateTime().toLocalDate());

        timeHourPicker.getValueFactory().setValue(violation.getDateTime().getHour());
        timeMinutePicker.getValueFactory().setValue(violation.getDateTime().getMinute());
    }

    /**
     * Изменяет информацию о количестве введённых символов.
     */
    public void descriptionText_TextChanged()
    {
        descriptionLength.setText(descriptionText.getText().length() + "/500");
    }

    /**
     * Изменяет информацию о количестве введённых символов.
     */
    public void punishmentText_TextChanged()
    {
        punishmentLength.setText(punishmentText.getText().length() + "/500");
    }

    public String dialogResult = "None";

    /**
     * Удаляет информацию из базы данных после второго предупреждения.
     */
    public void deleteB_Click() throws SQLException {
        String warning = DeleteMessage.yesNo("Удаление нарушения");

        if (warning.equals("Да"))
        {
            String result = DeleteMessage.yesNo("Удаление нарушения", "Вы уверены, что хотите удалить информацию о данном нарушении?\n\n" +
                    "Также будет удалена информация обо всех случаях его совершения.");

            if (result.equals("Да"))
            {
                PreparedStatement ps = DatabaseWorker.connection.prepareStatement("DELETE FROM violations WHERE id_violation = ?");
                ps.setInt(1, violationID);
                ps.executeUpdate();

                ps = DatabaseWorker.connection.prepareStatement("DELETE FROM commited_violations WHERE id_violation = ?");
                ps.setInt(1, violationID);
                ps.executeUpdate();

                DeleteMessage.ok("Удаление нарушения");

                dialogResult = "Abort";
                dialogStage.close();
            }
        }
    }

    /**
     * Отправляет информацию в базу данных после проверки заполненности всех полей.
     */
    public void okB_Click() throws SQLException {
        if (descriptionText.getText().isBlank())
        {
            ErrorMessage.show("Не заполнено поле \"Описание\"");
        }
        else if (punishmentText.getText().isBlank())
        {
            ErrorMessage.show("Не заполнено поле \"Мера наказания\"");
        }
        else if (datePicker.getValue() == null)
        {
            ErrorMessage.show("Не заполнено поле \"Дата совершения\"");
        }
        else
        {
            PreparedStatement ps;

            if (violationID == 0)
            {
                ps = DatabaseWorker.connection.prepareStatement("INSERT INTO violations (description, punishment, date_time) VALUES (?, ?, ?)");

                ps.setString(1, descriptionText.getText());
                ps.setString(2, punishmentText.getText());
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.of(datePicker.getValue().getYear(), datePicker.getValue().getMonth().getValue(), datePicker.getValue().getDayOfMonth(), timeHourPicker.getValue(), timeMinutePicker.getValue())));

                ps.executeUpdate();

                ps = DatabaseWorker.connection.prepareStatement("INSERT INTO commited_violations (id_student, id_violation) VALUES (?, (SELECT MAX(id_violation) FROM violations))");
                ps.setInt(1, studentID);
                ps.executeUpdate();
            }
            else
            {
                ps = DatabaseWorker.connection.prepareStatement("UPDATE violations SET description = ?, punishment = ?, date_time = ? WHERE id_violation = ?");

                ps.setString(1, descriptionText.getText());
                ps.setString(2, punishmentText.getText());
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.of(datePicker.getValue().getYear(), datePicker.getValue().getMonth().getValue(), datePicker.getValue().getDayOfMonth(), timeHourPicker.getValue(), timeMinutePicker.getValue())));
                ps.setInt(4, violationID);

                ps.executeUpdate();
                dialogResult = "OK";
            }

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
