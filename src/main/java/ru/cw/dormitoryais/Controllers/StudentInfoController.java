package ru.cw.dormitoryais.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.cw.dormitoryais.*;
import ru.cw.dormitoryais.Models.Students;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.time.LocalDate;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class StudentInfoController {
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
    public RadioButton sexMale;
    public RadioButton sexFemale;
    public DatePicker birthdayPicker;
    public ImageView photoViewPicture;
    public Label photoFileName;
    public TextField recordBookNumberText;
    public ImageView recordBookNumberInfo;
    public ImageView roomInfo;
    public DatePicker checkInDatePicker;
    public Label checkOutDateLabel;
    public DatePicker checkOutDatePicker;
    public ImageView checkOutDateInfo;
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

        TextFormatter recordBookNumberFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("[а-яА-ЯёЁ\s]{0,3}-\\d{0,3}-\\d{0,2}").matcher(change.getControlNewText()).matches() ? change : null);
        recordBookNumberText.setTextFormatter(recordBookNumberFormatter);

        recordBookNumberInfo.setImage(new Image(Program.class.getResourceAsStream("info.png")));

        Tooltip rbn_tt = new Tooltip("3 буквы - 3 цифры - 2 цифры\nЕсли меньше 3-х букв – заместить недостающие пробелами\nТолько русский алфавит");
        rbn_tt.setShowDelay(Duration.millis(100));
        Tooltip.install(recordBookNumberInfo, rbn_tt);

        checkOutDateInfo.setImage(new Image(Program.class.getResourceAsStream("info.png")));

        Tooltip cod_tt = new Tooltip("Нажмите два раза для получения справки о процессе выселения");
        cod_tt.setShowDelay(Duration.millis(100));
        Tooltip.install(checkOutDateInfo, cod_tt);
    }

    private int studentID = 0;
    private int roomID = 0;
    private boolean isMale = true;
    private byte[] image;

    /**
     * Устанавливает значения для пустого окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     */
    public void setParams(){
        photoViewPicture.setImage(new Image(Program.class.getResourceAsStream("no-image.png")));
        photoFileName.setText("");

        recordBookNumberText.setText("ААА-000-00");

        roomInfo.setImage(new Image(Program.class.getResourceAsStream("no.png")));

        Tooltip rns_tt = new Tooltip("Комната не выбрана");
        rns_tt.setShowDelay(Duration.millis(100));
        Tooltip.install(roomInfo, rns_tt);

        checkOutDateLabel.setVisible(false);
        checkOutDatePicker.setVisible(false);
        checkOutDateInfo.setVisible(false);

        deleteB.setVisible(false);
    }

    /**
     * Устанавливает значения для заполненного данными окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     * @param student заполняющая окно информация
     */
    public void setParams(Students student) throws SQLException {
        this.studentID = student.getId();

        surnameText.setText(student.getSurname());
        surnameText_TextChanged();
        nameText.setText(student.getName());
        nameText_TextChanged();
        patronymicText.setText(student.getPatronymic());
        patronymicText_TextChanged();

        if (!student.getSex().equals("Мужской")){
            sexFemale.setSelected(true);
        }

        birthdayPicker.setValue(student.getBirthday());

        image = student.getPhotoData();
        photoViewPicture.setImage(new Image(new ByteArrayInputStream(student.getPhotoData())));
        photoFileName.setText("(загруженное изображение)");

        recordBookNumberText.setText(student.getRecordBookNumber());

        PreparedStatement ps = DatabaseWorker.connection.prepareStatement("SELECT id_room FROM rooms where room_number = ?");
        ps.setInt(1, student.getRoomNumber());

        ResultSet resultSet = ps.executeQuery();
        resultSet.next();
        roomID = resultSet.getInt(1);

        roomSelected(student.getRoomNumber());

        checkInDatePicker.setValue(student.getCheckInDate());
        checkOutDatePicker.setValue(student.getCheckOutDate());
    }

    /**
     * Получает информацию о наличии свободных комнат. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     * @param freeMaleRooms информация о комнатах с лицами мужского пола
     * @param freeFemaleRooms информация о комнатах с лицами женского пола
     */
    public void setFreeRoomsData(boolean freeMaleRooms, boolean freeFemaleRooms)
    {
        if (studentID > 0)
        {
            if (!freeMaleRooms && !isMale)
            {
                sexMale.setDisable(true);
                sexFemale.setSelected(true);
            }

            if (!freeFemaleRooms && isMale)
            {
                sexFemale.setDisable(true);
                sexMale.setSelected(true);
            }
        }
        else
        {
            if (!freeMaleRooms)
            {
                sexMale.setDisable(true);
                sexFemale.setSelected(true);
            }
            else if (!freeFemaleRooms)
            {
                sexFemale.setDisable(true);
                sexMale.setSelected(true);
            }
        }
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
     * Обновляет переменную isMale и обнуляет выбор комнаты.
     */
    public void sexMale_CheckedChanged()
    {
        isMale = true;
        roomDeselected();
    }

    /**
     * Обновляет переменную isMale и обнуляет выбор комнаты.
     */
    public void sexFemale_CheckedChanged()
    {
        isMale = false;
        roomDeselected();
    }

    /**
     * Загружает изображение с устройства.
     */
    public void photoOpenButton_Click() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Фотография студента");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Изображения", "*.jpg", "*.jpeg", "*.png");
        fileChooser.getExtensionFilters().addAll(filter);
        File file = fileChooser.showOpenDialog(dialogStage);
        if (file != null) {
            image = Files.readAllBytes(file.toPath());
            if (!FormatCheck.isImage(image))
            {
                ErrorMessage.show("Файл не является изображением");
                return;
            }

            photoViewPicture.setImage(new Image(file.toURI().toString()));
            photoFileName.setText(file.getName());
        }
    }

    /**
     * Показывает на экране, что комната выбрана.
     */
    private void roomSelected(int roomNumber)
    {
        roomInfo.setImage(new Image(Program.class.getResourceAsStream("yes.png")));

        Tooltip rs_tt = new Tooltip("Выбрана комната №"+roomNumber);
        rs_tt.setShowDelay(Duration.millis(100));
        Tooltip.install(roomInfo, rs_tt);
    }

    /**
     * Обнуляет выбор комнаты.
     */
    private void roomDeselected()
    {
        roomID = 0;
        roomInfo.setImage(new Image(Program.class.getResourceAsStream("no.png")));

        Tooltip rns_tt = new Tooltip("Комната не выбрана");
        rns_tt.setShowDelay(Duration.millis(100));
        Tooltip.install(roomInfo, rns_tt);
    }

    /**
     * Открывает окно для выбора комнаты.
     * Не открывает, если комнат нет в базе данных.
     */
    public void roomNumberPanelSelect_Click() throws SQLException, IOException {
        ResultSet resultSet = DatabaseWorker.statement.executeQuery("SELECT COUNT(*) FROM rooms");
        resultSet.next();

        if (resultSet.getInt(1) == 0)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("Нет информации о комнатах");
            alert.showAndWait();

            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("multifunctionalTableWindow.fxml"));

        Parent page = loader.load();
        Stage addStage = new Stage();
        addStage.setTitle("Доступные комнаты");
        addStage.initOwner(Program.getPrimaryStage());
        addStage.setMinWidth(500);
        addStage.setMinHeight(150);

        Scene scene = new Scene(page);
        addStage.setScene(scene);
        addStage.getIcons().add(new Image(Program.class.getResourceAsStream("quarantine.png")));

        MultifunctionalTableWindowController controller = loader.getController();
        controller.setAddStage(addStage);
        if (roomID != 0){
            controller.setParams(TableState.Select, TableType.Rooms, roomID, isMale);
        }
        else{
            controller.setParams(TableState.Select, TableType.Rooms, isMale);
        }
        addStage.showAndWait();

        if (controller.dialogResult.equals("OK")){
            roomID = controller.returnID;

            PreparedStatement ps = DatabaseWorker.connection.prepareStatement("SELECT room_number FROM rooms WHERE id_room = ?");
            ps.setInt(1, roomID);

            resultSet = ps.executeQuery();
            resultSet.next();

            roomSelected(resultSet.getInt(1));
        }
    }

    private boolean checkOutTime = false;

    /**
     * Обновляет переменную checkOutTime.
     */
    public void checkOutDatePicker_ValueChanged()
    {
        if (checkOutDatePicker.getValue().compareTo(LocalDate.now())<=0)
        {
            checkOutTime = true;
        }
        else
        {
            checkOutTime = false;
        }
    }

    /**
     * Показывает справку о процессе выселения студента.
     */
    public void checkOutDateInfo_DoubleClick(MouseEvent mouseEvent){
        if (mouseEvent.getClickCount() == 2){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Справка: выселение студента");
            alert.setHeaderText(null);
            alert.setContentText("Для выселения студента поставьте дату в диапазон от даты заселения до сегодняшней даты.\n\n" +
                    "После выселения вы не сможете добавлять и изменять сведения о выданном студенту инвентаре и совершённых им нарушениях, " +
                    "а сам студент будет недоступен для выбора и отображения во всех иных связанных таблицах (кроме таблицы посетителей), равно как и влиять на какие-либо их значения.\n\n" +
                    "Окончательное выселение происходит на следующий день после указанной даты. До этого момента перечисленные выше ограничения не действуют.");
            alert.showAndWait();
        }
    }

    public String dialogResult = "None";

    /**
     * Удаляет информацию из базы данных после второго предупреждения.
     */
    public void deleteB_Click() throws SQLException {
        String warning = DeleteMessage.yesNo("Удаление студента");

        if (warning.equals("Да"))
        {
            String result = DeleteMessage.yesNo("Удаление студента", "Вы уверены, что хотите удалить информацию о выбранном студенте?\n\n" +
                    "Также будет удалена информация о его нарушениях, посетителях, выданном инвентаре и проживании в комнате.");

            if (result.equals("Да"))
            {
                PreparedStatement ps = DatabaseWorker.connection.prepareStatement("DELETE FROM students WHERE id_student = ?");
                ps.setInt(1, studentID);
                ps.executeUpdate();

                ps = DatabaseWorker.connection.prepareStatement("DELETE FROM violations " +
                        "WHERE (SELECT COUNT(id_student) FROM commited_violations " +
                        "WHERE commited_violations.id_violation = violations.id_violation)=1 " +
                        "AND EXISTS (SELECT id_student FROM commited_violations " +
                        "WHERE commited_violations.id_violation = violations.id_violation " +
                        "AND id_student = ?)");
                ps.setInt(1, studentID);
                ps.executeUpdate();

                ps = DatabaseWorker.connection.prepareStatement("DELETE FROM commited_violations WHERE id_student = ?");
                ps.setInt(1, studentID);
                ps.executeUpdate();

                ps = DatabaseWorker.connection.prepareStatement("DELETE FROM issued_inventory WHERE id_student = ?");
                ps.setInt(1, studentID);
                ps.executeUpdate();

                ps = DatabaseWorker.connection.prepareStatement("DELETE FROM check_ins_outs WHERE id_student = ?");
                ps.setInt(1, studentID);
                ps.executeUpdate();

                ps = DatabaseWorker.connection.prepareStatement("DELETE FROM visitors WHERE id_student = ?");
                ps.setInt(1, studentID);
                ps.executeUpdate();

                DeleteMessage.ok("Удаление студента");

                dialogResult = "Abort";
                dialogStage.close();
            }
        }
    }

    /**
     * Отправляет информацию в базу данных после проверки заполненности всех полей.
     * Не отправляет, если заполнено неправильными данными.
     */
    public void okB_Click() throws SQLException, IOException {
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
        else if (birthdayPicker.getValue() == null)
        {
            ErrorMessage.show("Не заполнено поле \"Дата рождения\"");
        }
        else if (photoFileName.getText().isBlank())
        {
            ErrorMessage.show("Отсутствует фотография");
        }
        else if (recordBookNumberText.getText().length() < 10)
        {
            ErrorMessage.show("Не заполнено поле \"Номер зачётной книжки\"");
        }
        else if (roomID == 0)
        {
            ErrorMessage.show("Не выбрана комната");
        }
        else if (checkInDatePicker.getValue() == null)
        {
            ErrorMessage.show("Не заполнено поле \"Дата заселения\"");
        }
        else if (checkOutDatePicker.getValue() == null && studentID != 0)
        {
            ErrorMessage.show("Не заполнено поле \"Дата выселения\"");
        }
        else
        {
            PreparedStatement ps;

            if (studentID == 0)
            {
                ps = DatabaseWorker.connection.prepareStatement("INSERT INTO students (surname, name, patronymic, photo, sex, birthday, record_book_number) VALUES (?, ?, ?, ?, ?, ?, ?)");

                ps.setString(1, surnameText.getText());
                ps.setString(2, nameText.getText());
                ps.setString(3, patronymicText.getText());
                ps.setBytes(4, image);
                ps.setBoolean(5, isMale);
                ps.setDate(6, Date.valueOf(LocalDate.of(birthdayPicker.getValue().getYear(), birthdayPicker.getValue().getMonth().getValue(), birthdayPicker.getValue().getDayOfMonth())));
                ps.setString(7, recordBookNumberText.getText());

                ps.executeUpdate();

                ps = DatabaseWorker.connection.prepareStatement("INSERT INTO check_ins_outs (id_student, id_room, check_in_date, check_out_date) VALUES ((SELECT MAX(id_student) FROM students), ?, ?, ?)");

                ps.setInt(1, roomID);
                ps.setDate(2, Date.valueOf(LocalDate.of(checkInDatePicker.getValue().getYear(), checkInDatePicker.getValue().getMonth().getValue(), checkInDatePicker.getValue().getDayOfMonth())));
                ps.setDate(3, Date.valueOf(LocalDate.of(checkInDatePicker.getValue().getYear() + 4, checkInDatePicker.getValue().getMonth().getValue(), checkInDatePicker.getValue().getDayOfMonth())));

                ps.executeUpdate();
            }
            else
            {
                if (checkOutTime){
                    ps = DatabaseWorker.connection.prepareStatement("SELECT id_inventory FROM issued_inventory " +
                            "WHERE (SELECT COUNT(id_inventory) FROM issued_inventory " +
                            "WHERE turn_in_date >= CURRENT_DATE AND id_student = ?)>0");
                    ps.setInt(1, studentID);

                    ResultSet resultSet = ps.executeQuery();
                    resultSet.next();

                    if (resultSet.getInt(1) > 0){
                        ErrorMessage.show("Студент не может быть выселен пока у него имеется несданный инвентарь");

                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("multifunctionalTableWindow.fxml"));

                        Parent page = loader.load();
                        Stage addStage = new Stage();
                        addStage.setTitle("Инвентарь, выданный студенту " + surnameText.getText() + " " + nameText.getText() + " " + patronymicText.getText());
                        addStage.initOwner(Program.getPrimaryStage());
                        addStage.setMinWidth(500);
                        addStage.setMinHeight(150);

                        Scene scene = new Scene(page);
                        addStage.setScene(scene);
                        addStage.getIcons().add(new Image(Program.class.getResourceAsStream("quarantine.png")));

                        MultifunctionalTableWindowController controller = loader.getController();
                        controller.setAddStage(addStage);
                        controller.setParams(TableState.Update, TableType.Inventory, studentID);
                        addStage.showAndWait();

                        return;
                    }
                }

                ps = DatabaseWorker.connection.prepareStatement("UPDATE students SET surname = ?, name = ?, patronymic = ?, photo = ?, sex = ?, birthday = ?, record_book_number = ? WHERE id_student = ?");

                ps.setString(1, surnameText.getText());
                ps.setString(2, nameText.getText());
                ps.setString(3, patronymicText.getText());
                ps.setBytes(4, image);
                ps.setBoolean(5, isMale);
                ps.setDate(6, Date.valueOf(LocalDate.of(birthdayPicker.getValue().getYear(), birthdayPicker.getValue().getMonth().getValue(), birthdayPicker.getValue().getDayOfMonth())));
                ps.setString(7, recordBookNumberText.getText());
                ps.setInt(8, studentID);

                ps.executeUpdate();

                ps = DatabaseWorker.connection.prepareStatement("UPDATE check_ins_outs SET id_room = ?, check_in_date = ?, check_out_date = ? WHERE id_student = ?");

                ps.setInt(1, roomID);
                ps.setDate(2, Date.valueOf(LocalDate.of(checkInDatePicker.getValue().getYear(), checkInDatePicker.getValue().getMonth().getValue(), checkInDatePicker.getValue().getDayOfMonth())));
                ps.setDate(3, Date.valueOf(LocalDate.of(checkOutDatePicker.getValue().getYear(), checkOutDatePicker.getValue().getMonth().getValue(), checkOutDatePicker.getValue().getDayOfMonth())));
                ps.setInt(4, studentID);

                ps.executeUpdate();
            }

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
