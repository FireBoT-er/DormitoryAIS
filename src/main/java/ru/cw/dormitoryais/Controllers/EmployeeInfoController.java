package ru.cw.dormitoryais.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.cw.dormitoryais.*;
import ru.cw.dormitoryais.Models.Employees;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class EmployeeInfoController {
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
    public TextField positionText;
    public Label positionLength;
    public ImageView photoViewPicture;
    public Label photoFileName;
    public CheckBox isWorkingNowCheckBox;
    public ImageView isWorkingNowInfo;
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
        TextFormatter positionFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile(".{0,50}").matcher(change.getControlNewText()).matches() ? change : null);
        positionText.setTextFormatter(positionFormatter);

        surnameInfo.setImage(new Image(Program.class.getResourceAsStream("info.png")));
        nameInfo.setImage(new Image(Program.class.getResourceAsStream("info.png")));
        patronymicInfo.setImage(new Image(Program.class.getResourceAsStream("info.png")));
        isWorkingNowInfo.setImage(new Image(Program.class.getResourceAsStream("info.png")));

        Tooltip ru_tt = new Tooltip("Только русский алфавит");
        ru_tt.setShowDelay(Duration.millis(100));
        Tooltip.install(surnameInfo, ru_tt);
        Tooltip.install(nameInfo, ru_tt);
        Tooltip.install(patronymicInfo, ru_tt);

        Tooltip iwn_tt = new Tooltip("После увольнения сотрудник не может быть добавлен к уборкам");
        iwn_tt.setShowDelay(Duration.millis(100));
        Tooltip.install(isWorkingNowInfo, iwn_tt);
    }

    private int employeeID = 0;
    private boolean isWorkingNow = true;
    private byte[] image;

    /**
     * Устанавливает значения для пустого окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     */
    public void setParams(){
        isWorkingNowCheckBox.setVisible(false);
        isWorkingNowInfo.setVisible(false);

        photoViewPicture.setImage(new Image(Program.class.getResourceAsStream("no-image.png")));
        photoFileName.setText("");

        deleteB.setVisible(false);
    }

    /**
     * Устанавливает значения для заполненного данными окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     * @param employee заполняющая окно информация
     */
    public void setParams(Employees employee){
        this.employeeID = employee.getId();

        this.surnameText.setText(employee.getSurname());
        surnameText_TextChanged();
        this.nameText.setText(employee.getName());
        nameText_TextChanged();
        this.patronymicText.setText(employee.getPatronymic());
        patronymicText_TextChanged();
        this.positionText.setText(employee.getPosition());
        positionText_TextChanged();

        image = employee.getPhotoData();
        photoViewPicture.setImage(new Image(new ByteArrayInputStream(employee.getPhotoData())));
        photoFileName.setText("(загруженное изображение)");

        isWorkingNowCheckBox.setSelected(employee.isIsWorkingNow());
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
     * Изменяет информацию о количестве введённых символов.
     */
    public void positionText_TextChanged()
    {
        positionLength.setText(positionText.getText().length() + "/50");
    }

    /**
     * Загружает изображение с устройства.
     */
    public void photoOpenButton_Click() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Фотография сотрудника");
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
     * Обновляет переменную isWorkingNow.
     */
    public void isWorkingNowCheckBox_CheckedChanged()
    {
        isWorkingNow = !isWorkingNow;
    }

    public String dialogResult = "None";

    /**
     * Удаляет информацию из базы данных после второго предупреждения.
     */
    public void deleteB_Click() throws SQLException {
        String warning = DeleteMessage.yesNo("Удаление сотрудника");

        if (warning.equals("Да"))
        {
            String result = DeleteMessage.yesNo("Удаление сотрудника",
                                                  "Вы уверены, что хотите удалить информацию о выбранном сотруднике?\n\n" +
                                                       "Также будет удалена информация о его участии в уборках.");

            if (result.equals("Да"))
            {
                PreparedStatement ps = DatabaseWorker.connection.prepareStatement("DELETE FROM employees WHERE id_employee = ?");
                ps.setInt(1, employeeID);
                ps.executeUpdate();

                ps = DatabaseWorker.connection.prepareStatement("DELETE FROM performed_cleanings WHERE id_employee = ?");
                ps.setInt(1, employeeID);
                ps.executeUpdate();

                DeleteMessage.ok("Удаление сотрудника");

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
        else if (positionText.getText().isBlank())
        {
            ErrorMessage.show("Не заполнено поле \"Должность\"");
        }
        else if (photoFileName.getText().isBlank())
        {
            ErrorMessage.show("Отсутствует фотография");
        }
        else
        {
            PreparedStatement ps;

            if (employeeID == 0)
            {
                ps = DatabaseWorker.connection.prepareStatement("INSERT INTO employees (surname, name, patronymic, photo, emp_position, is_working_now) VALUES (?, ?, ?, ?, ?, ?)");
            }
            else
            {
                ps = DatabaseWorker.connection.prepareStatement("UPDATE employees SET surname = ?, name = ?, patronymic = ?, photo = ?, emp_position = ?, is_working_now = ? WHERE id_employee = ?");
                ps.setInt(7, employeeID);
            }

            ps.setString(1, surnameText.getText());
            ps.setString(2, nameText.getText());
            ps.setString(3, patronymicText.getText());
            ps.setBytes(4, image);
            ps.setString(5, positionText.getText());
            ps.setBoolean(6, isWorkingNowCheckBox.isSelected());

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
