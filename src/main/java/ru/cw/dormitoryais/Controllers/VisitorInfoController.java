package ru.cw.dormitoryais.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.cw.dormitoryais.*;
import ru.cw.dormitoryais.Models.Visitors;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class VisitorInfoController {
    private Stage dialogStage;

    /**
     * Устанавливает сцену для данного окна. <br>
     * Использовать после получения экземпляра, иначе окно не откроется.
     * @param addStage сцена
     */
    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    public TextField surnameText;
    public Label surnameLength;
    public ImageView surnameInfo;
    public TextField nameText;
    public Label nameLength;
    public ImageView nameInfo;
    public TextField patronymicText;
    public Label patronymicLength;
    public ImageView patronymicInfo;
    public TextField phoneText;
    public DatePicker datePicker;
    public Spinner<Integer> timeHourPicker;
    public Spinner<Integer> timeMinutePicker;
    public ImageView visitedInfo;
    public Button deleteB;

    @FXML
    void initialize() {
        TextFormatter surnameFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("[а-яА-ЯёЁ]{0,30}").matcher(change.getControlNewText()).matches() ? change : null);
        surnameText.setTextFormatter(surnameFormatter);
        TextFormatter nameFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("[а-яА-ЯёЁ]{0,30}").matcher(change.getControlNewText()).matches() ? change : null);
        nameText.setTextFormatter(nameFormatter);
        TextFormatter patronymicFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("[а-яА-ЯёЁ]{0,30}").matcher(change.getControlNewText()).matches() ? change : null);
        patronymicText.setTextFormatter(patronymicFormatter);

        surnameInfo.setImage(new Image(Program.class.getResourceAsStream("info.png")));
        nameInfo.setImage(new Image(Program.class.getResourceAsStream("info.png")));
        patronymicInfo.setImage(new Image(Program.class.getResourceAsStream("info.png")));

        Tooltip ru_tt = new Tooltip("Только русский алфавит");
        ru_tt.setShowDelay(Duration.millis(100));
        Tooltip.install(surnameInfo, ru_tt);
        Tooltip.install(nameInfo, ru_tt);
        Tooltip.install(patronymicInfo, ru_tt);

        TextFormatter phoneFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("\\d{0,11}").matcher(change.getControlNewText()).matches() ? change : null);
        phoneText.setTextFormatter(phoneFormatter);

        SpinnerValueFactory<Integer> timeHourPickerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
        timeHourPicker.setValueFactory(timeHourPickerValueFactory);
        timeHourPicker.getValueFactory().setWrapAround(true);
        SpinnerValueFactory<Integer> timeMinutePickerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        timeMinutePicker.setValueFactory(timeMinutePickerValueFactory);
        timeMinutePicker.getValueFactory().setWrapAround(true);
    }

    private int visitorID = 0;
    private int studentID = 0;

    /**
     * Устанавливает значения для пустого окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     */
    public void setParams(){
        timeHourPicker.getValueFactory().setValue(LocalDateTime.now().getHour());
        timeMinutePicker.getValueFactory().setValue(LocalDateTime.now().getMinute());

        visitedInfo.setImage(new Image(Program.class.getResourceAsStream("no.png")));

        Tooltip sns_tt = new Tooltip("Студент не выбран");
        sns_tt.setShowDelay(Duration.millis(100));
        Tooltip.install(visitedInfo, sns_tt);

        deleteB.setVisible(false);
    }

    /**
     * Устанавливает значения для заполненного данными окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     * @param visitor заполняющая окно информация
     */
    public void setParams(Visitors visitor){
        this.visitorID = visitor.getId();

        surnameText.setText(visitor.getSurname());
        surnameText_TextChanged();
        nameText.setText(visitor.getName());
        nameText_TextChanged();
        patronymicText.setText(visitor.getPatronymic());
        patronymicText_TextChanged();

        phoneText.setText(visitor.getPhone());

        datePicker.setValue(visitor.getDateTime().toLocalDate());

        timeHourPicker.getValueFactory().setValue(visitor.getDateTime().getHour());
        timeMinutePicker.getValueFactory().setValue(visitor.getDateTime().getMinute());

        this.studentID = visitor.getStudentID();

        studentSelected(visitor.getVisited());
    }

    /**
     * Показывает на экране, что посетитель выбран.
     */
    private void studentSelected(String studentFullName)
    {
        visitedInfo.setImage(new Image(Program.class.getResourceAsStream("yes.png")));

        Tooltip ss_tt = new Tooltip("Выбранный студент:\n"+studentFullName);
        ss_tt.setShowDelay(Duration.millis(100));
        Tooltip.install(visitedInfo, ss_tt);
    }

    /**
     * Изменяет информацию о количестве введённых символов.
     */
    public void surnameText_TextChanged()
    {
        surnameLength.setText(surnameText.getText().length() + "/30");
    }

    /**
     * Изменяет информацию о количестве введённых символов.
     */
    public void nameText_TextChanged()
    {
        nameLength.setText(nameText.getText().length() + "/30");
    }

    /**
     * Изменяет информацию о количестве введённых символов.
     */
    public void patronymicText_TextChanged()
    {
        patronymicLength.setText(patronymicText.getText().length() + "/30");
    }

    /**
     * Открывает окно для выбора посещаемого студента.
     * Не открывает, если студентов нет в базе данных.
     */
    public void studentSelect_Click() throws SQLException, IOException {
        ResultSet resultSet = DatabaseWorker.statement.executeQuery("SELECT COUNT(*) FROM students");
        resultSet.next();

        if (resultSet.getInt(1) == 0)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("Нет информации о студентах");
            alert.showAndWait();

            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("multifunctionalTableWindow.fxml"));

        Parent page = loader.load();
        Stage addStage = new Stage();
        addStage.setTitle("Выберите студента");
        addStage.initOwner(Program.getPrimaryStage());
        addStage.setMinWidth(500);
        addStage.setMinHeight(150);

        Scene scene = new Scene(page);
        addStage.setScene(scene);
        addStage.getIcons().add(new Image(Program.class.getResourceAsStream("quarantine.png")));

        MultifunctionalTableWindowController controller = loader.getController();
        controller.setAddStage(addStage);
        if (studentID != 0){
            controller.setParams(TableState.Select, TableType.Visited, studentID);
        }
        else{
            controller.setParams(TableState.Select, TableType.Visited);
        }
        addStage.showAndWait();

        if (controller.dialogResult.equals("OK")){
            studentID = controller.returnID;

            PreparedStatement ps = DatabaseWorker.connection.prepareStatement("SELECT surname, name, patronymic FROM students WHERE id_student = ?");
            ps.setInt(1, studentID);

            resultSet = ps.executeQuery();
            resultSet.next();

            studentSelected(resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3));
        }
    }

    public String dialogResult = "None";

    /**
     * Удаляет информацию из базы данных после второго предупреждения.
     */
    public void deleteB_Click() throws SQLException {
        String warning = DeleteMessage.yesNo("Удаление посетителя");

        if (warning.equals("Да"))
        {
            String result = DeleteMessage.yesNo("Удаление посетителя", "Вы уверены, что хотите удалить информацию о выбранном посетителе?");

            if (result.equals("Да"))
            {
                PreparedStatement ps = DatabaseWorker.connection.prepareStatement("DELETE FROM visitors WHERE id_visitor = ?");
                ps.setInt(1, visitorID);
                ps.executeUpdate();

                DeleteMessage.ok("Удаление посетителя");

                dialogResult = "Abort";
                dialogStage.close();
            }
        }
    }

    /**
     * Отправляет информацию в базу данных после проверки заполненности всех полей.
     */
    public void okB_Click() throws SQLException {
        if (surnameText.getText().isBlank())
        {
            ErrorMessage.show("Не заполнено поле \"Фамилия\"");
        }
        else if (nameText.getText().isBlank())
        {
            ErrorMessage.show("Не заполнено поле \"Имя\"");
        }
        else if (patronymicText.getText().isBlank())
        {
            ErrorMessage.show("Не заполнено поле \"Отчество\"");
        }
        else if (phoneText.getText().length() < 11)
        {
            ErrorMessage.show("Не заполнено поле \"Номер телефона\"");
        }
        else if (datePicker.getValue() == null)
        {
            ErrorMessage.show("Не заполнено поле \"Дата посещения\"");
        }
        else if (studentID == 0)
        {
            ErrorMessage.show("Не выбран посещаемый студент");
        }
        else
        {
            PreparedStatement ps;

            if (visitorID == 0)
            {
                ps = DatabaseWorker.connection.prepareStatement("INSERT INTO visitors (surname, name, patronymic, phone, date_time, id_student) VALUES (?, ?, ?, ?, ?, ?)");
            }
            else
            {
                ps = DatabaseWorker.connection.prepareStatement("UPDATE visitors SET surname = ?, name = ?, patronymic = ?, phone = ?, date_time = ?, id_student = ? WHERE id_visitor = ?");
                ps.setInt(7, visitorID);
            }

            ps.setString(1, surnameText.getText());
            ps.setString(2, nameText.getText());
            ps.setString(3, patronymicText.getText());
            ps.setString(4, phoneText.getText());
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.of(datePicker.getValue().getYear(), datePicker.getValue().getMonth().getValue(), datePicker.getValue().getDayOfMonth(), timeHourPicker.getValue(), timeMinutePicker.getValue())));
            ps.setInt(6, studentID);

            ps.executeUpdate();

            dialogResult = "OK";
            dialogStage.close();
        }
    }

    /**
     * Закрывает окно и обнуляет статические переменные.
     */
    public void cancelB_Click()
    {
        dialogStage.close();
    }
}
