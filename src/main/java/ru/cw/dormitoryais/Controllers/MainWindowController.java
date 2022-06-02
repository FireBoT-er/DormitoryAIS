package ru.cw.dormitoryais.Controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.cw.dormitoryais.*;
import ru.cw.dormitoryais.Models.*;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainWindowController {
    public TabPane tabs;

    public TableView<Students> studentsTable;
    public TableColumn<Students, Integer> studentsIdColumn;
    public TableColumn<Students, String> studentsSurnameColumn;
    public TableColumn<Students, String> studentsNameColumn;
    public TableColumn<Students, String> studentsPatronymicColumn;
    public TableColumn<Students, String> studentsSexColumn;
    public TableColumn<Students, LocalDate> studentsBirthdayColumn;
    public TableColumn<Students, Button> studentsPhotoColumn;
    public TableColumn<Students, String> studentsRecordBookNumberColumn;
    public TableColumn<Students, Integer> studentsRoomNumberColumn;
    public TableColumn<Students, LocalDate> studentsCheckInDateColumn;
    public TableColumn<Students, LocalDate> studentsCheckOutDateColumn;

    public CheckBox studentsEditOnlyResidents;

    public TableView<Rooms> roomsTable;
    public TableColumn<Rooms, Integer> roomsIdColumn;
    public TableColumn<Rooms, Integer> roomsRoomNumberColumn;
    public TableColumn<Rooms, Integer> roomsBedsColumn;
    public Label roomTotalInfoData;

    public TableView<Inventory> inventoryTable;
    public TableColumn<Inventory, Integer> inventoryIdColumn;
    public TableColumn<Inventory, String> inventoryTypeColumn;
    public TableColumn<Inventory, Integer> inventoryCountColumn;
    public TableColumn<Inventory, Integer> inventoryCountInStockColumn;
    public TableColumn<Inventory, Integer> inventoryCountIssuedColumn;

    public TableView<Visitors> visitorsTable;
    public TableColumn<Visitors, Integer> visitorsIdColumn;
    public TableColumn<Visitors, String> visitorsSurnameColumn;
    public TableColumn<Visitors, String> visitorsNameColumn;
    public TableColumn<Visitors, String> visitorsPatronymicColumn;
    public TableColumn<Visitors, String> visitorsPhoneColumn;
    public TableColumn<Visitors, LocalDateTime> visitorsDateTimeColumn;
    public TableColumn<Visitors, Integer> visitorsStudentIdColumn;
    public TableColumn<Visitors, String> visitorsVisitedColumn;

    public TableView<Employees> employeesTable;
    public TableColumn<Employees, Integer> employeesIdColumn;
    public TableColumn<Employees, String> employeesSurnameColumn;
    public TableColumn<Employees, String> employeesNameColumn;
    public TableColumn<Employees, String> employeesPatronymicColumn;
    public TableColumn<Employees, Button> employeesPhotoColumn;
    public TableColumn<Employees, String> employeesPositionColumn;
    public TableColumn<Employees, String> employeesIsWorkingNowColumn;

    public TableView<Cleanings> cleaningsTable;
    public TableColumn<Cleanings, Integer> cleaningsIdColumn;
    public TableColumn<Cleanings, LocalDateTime> cleaningsDateTimeColumn;
    public TableColumn<Cleanings, String> cleaningsCleanedColumn;

    public CheckBox employeesButtonsOnlyArranged;

    @FXML
    void initialize() throws SQLException, IOException {
        settingsWindow(true);

        tabs.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == tabs.getTabs().get(6)){
                tabs.getSelectionModel().select(oldValue);
                try {
                    settingsWindow(false);
                } catch (Exception ignored) { }
            }
        }));

        studentsIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        studentsSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        studentsNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        studentsPatronymicColumn.setCellValueFactory(cellData -> cellData.getValue().patronymicProperty());
        studentsSexColumn.setCellValueFactory(cellData -> cellData.getValue().sexProperty());
        studentsBirthdayColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper(cellData.getValue().getBirthday()));
        studentsBirthdayColumn.setCellFactory(cellData -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(DateTimeFormatter.ofPattern("dd.MM.yyyy").format(date));
                }
            }
        });
        studentsPhotoColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper(cellData.getValue().getPhoto()));
        studentsRecordBookNumberColumn.setCellValueFactory(cellData -> cellData.getValue().recordBookNumberProperty());
        studentsRoomNumberColumn.setCellValueFactory(cellData -> cellData.getValue().roomNumberProperty().asObject());
        studentsCheckInDateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper(cellData.getValue().getCheckInDate()));
        studentsCheckInDateColumn.setCellFactory(cellData -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(DateTimeFormatter.ofPattern("dd.MM.yyyy").format(date));
                }
            }
        });
        studentsCheckOutDateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper(cellData.getValue().getCheckOutDate()));
        studentsCheckOutDateColumn.setCellFactory(cellData -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(DateTimeFormatter.ofPattern("dd.MM.yyyy").format(date));
                }
            }
        });

        roomsIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        roomsRoomNumberColumn.setCellValueFactory(cellData -> cellData.getValue().roomNumberProperty().asObject());
        roomsBedsColumn.setCellValueFactory(cellData -> cellData.getValue().bedsProperty().asObject());

        inventoryIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        inventoryTypeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        inventoryCountColumn.setCellValueFactory(cellData -> cellData.getValue().countProperty().asObject());
        inventoryCountInStockColumn.setCellValueFactory(cellData -> cellData.getValue().countInStockProperty().asObject());
        inventoryCountIssuedColumn.setCellValueFactory(cellData -> cellData.getValue().countIssuedProperty().asObject());

        visitorsIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        visitorsSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        visitorsNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        visitorsPatronymicColumn.setCellValueFactory(cellData -> cellData.getValue().patronymicProperty());
        visitorsPhoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        visitorsDateTimeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper(cellData.getValue().getDateTime()));
        visitorsDateTimeColumn.setCellFactory(cellData -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime dateTime, boolean empty) {
                super.updateItem(dateTime, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(dateTime));
                }
            }
        });
        visitorsDateTimeColumn.setSortType(TableColumn.SortType.DESCENDING);
        visitorsStudentIdColumn.setCellValueFactory(cellData -> cellData.getValue().studentIDProperty().asObject());
        visitorsVisitedColumn.setCellValueFactory(cellData -> cellData.getValue().visitedProperty());

        employeesIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        employeesSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        employeesNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        employeesPatronymicColumn.setCellValueFactory(cellData -> cellData.getValue().patronymicProperty());
        employeesPositionColumn.setCellValueFactory(cellData -> cellData.getValue().positionProperty());
        employeesPhotoColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper(cellData.getValue().getPhoto()));
        employeesIsWorkingNowColumn.setCellValueFactory(cellData -> cellData.getValue().isIsWorkingNow()?new SimpleStringProperty("Да") :new SimpleStringProperty("Нет"));

        cleaningsIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        cleaningsDateTimeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper(cellData.getValue().getDateTime()));
        cleaningsDateTimeColumn.setCellFactory(cellData -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime dateTime, boolean empty) {
                super.updateItem(dateTime, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(dateTime));
                }
            }
        });
        cleaningsDateTimeColumn.setSortType(TableColumn.SortType.DESCENDING);
        cleaningsCleanedColumn.setCellValueFactory(cellData -> cellData.getValue().cleanedProperty());

        loadAllTables();
    }

    private Object[] setModalWindow(String name, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(name));

        Parent page = loader.load();
        Stage addStage = new Stage();
        addStage.setTitle(title);
        addStage.initModality(Modality.WINDOW_MODAL);
        addStage.initOwner(Program.getPrimaryStage());

        Scene scene = new Scene(page);
        addStage.setScene(scene);
        addStage.setResizable(false);
        addStage.getIcons().add(new Image(Program.class.getResourceAsStream("quarantine.png")));

        return new Object[]{ loader, addStage };
    }

    private Object[] setMultifunctionalModalWindow(String title) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("multifunctionalTableWindow.fxml"));

        Parent page = loader.load();
        Stage addStage = new Stage();
        addStage.setTitle(title);
        addStage.initOwner(Program.getPrimaryStage());
        addStage.setMinWidth(500);
        addStage.setMinHeight(150);

        Scene scene = new Scene(page);
        addStage.setScene(scene);
        addStage.getIcons().add(new Image(Program.class.getResourceAsStream("quarantine.png")));

        return new Object[]{ loader, addStage };
    }

    private void loadAllTables() throws SQLException {
        selectStudents();
        selectRooms();
        selectInventory();
        selectEmployees();
        selectVisitors();
        selectCleanings();
    }

    private String selectStudentsQueryLastPart = " WHERE check_out_date >= CURRENT_DATE";

    private void selectStudents() throws SQLException {
        studentsTable.getItems().clear();

        ResultSet resultSet = DatabaseWorker.statement.executeQuery("SELECT students.*, check_ins_outs.*, rooms.room_number " +
                                                                        "FROM students " +
                                                                        "LEFT JOIN check_ins_outs ON students.id_student = check_ins_outs.id_student " +
                                                                        "LEFT JOIN rooms ON check_ins_outs.id_room = rooms.id_room" +
                                                                        selectStudentsQueryLastPart);

        while (resultSet.next()){
            studentsTable.getItems().add(new Students(resultSet.getInt(1),
                                                      resultSet.getString(2),
                                                      resultSet.getString(3),
                                                      resultSet.getString(4),
                                                      resultSet.getBoolean(6),
                                                      resultSet.getDate(7).toLocalDate(),
                                                      new Button("Открыть"),
                                                      resultSet.getBytes(5),
                                                      resultSet.getString(8),
                                                      resultSet.getInt(13),
                                                      resultSet.getDate(11).toLocalDate(),
                                                      resultSet.getDate(12).toLocalDate()));
        }

        if (studentsTable.getItems().size() > 0){
            studentsTable.getSortOrder().clear();
            studentsTable.getSortOrder().add(studentsSurnameColumn);
            studentsTable.sort();
        }
    }

    private void selectRooms() throws SQLException {
        roomsTable.getItems().clear();

        ResultSet resultSet = DatabaseWorker.statement.executeQuery("SELECT * FROM rooms");

        while (resultSet.next()){
            roomsTable.getItems().add(new Rooms(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3)));
        }

        if (roomsTable.getItems().size() > 0){
            roomsTable.getSortOrder().clear();
            roomsTable.getSortOrder().add(roomsRoomNumberColumn);
            roomsTable.sort();
        }

        roomTotalInfoDataFill();
    }

    private void selectInventory() throws SQLException {
        inventoryTable.getItems().clear();

        ResultSet resultSet = DatabaseWorker.statement.executeQuery("SELECT id_inventory, inv_type, inv_count, " +
                                                                        "inv_count-(SELECT COALESCE(SUM(iss_count), 0) FROM issued_inventory " +
                                                                        "WHERE inventory.id_inventory = issued_inventory.id_inventory " +
                                                                        "AND turn_in_date >= CURRENT_DATE), " +
                                                                        "(SELECT COALESCE(SUM(iss_count), 0) FROM issued_inventory " +
                                                                        "WHERE issued_inventory.id_inventory = inventory.id_inventory " +
                                                                        "AND turn_in_date >= CURRENT_DATE) " +
                                                                        "FROM inventory");

        while (resultSet.next()){
            inventoryTable.getItems().add(new Inventory(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getInt(5)));
        }

        if (inventoryTable.getItems().size() > 0){
            inventoryTable.getSortOrder().clear();
            inventoryTable.getSortOrder().add(inventoryTypeColumn);
            inventoryTable.sort();
        }
    }

    private void selectVisitors() throws SQLException {
        visitorsTable.getItems().clear();

        ResultSet resultSet = DatabaseWorker.statement.executeQuery("SELECT visitors.*, students.surname, students.name, students.patronymic FROM visitors LEFT JOIN students ON visitors.id_student = students.id_student");

        while (resultSet.next()){
            visitorsTable.getItems().add(new Visitors(resultSet.getInt(1),
                                                      resultSet.getString(2),
                                                      resultSet.getString(3),
                                                      resultSet.getString(4),
                                                      resultSet.getString(5),
                                                      resultSet.getTimestamp(6).toLocalDateTime(),
                                                      resultSet.getInt(7),
                                                resultSet.getString(8) + " " + resultSet.getString(9) + " " + resultSet.getString(10)));
        }

        if (visitorsTable.getItems().size() > 0){
            visitorsTable.getSortOrder().clear();
            visitorsTable.getSortOrder().add(visitorsDateTimeColumn);
            visitorsTable.sort();
        }
    }

    private String selectEmployeesQuery = "SELECT * FROM employees WHERE is_working_now = TRUE";

    private void selectEmployees() throws SQLException {
        employeesTable.getItems().clear();

        ResultSet resultSet = DatabaseWorker.statement.executeQuery(selectEmployeesQuery);

        while (resultSet.next()){
            employeesTable.getItems().add(new Employees(resultSet.getInt(1),
                                                        resultSet.getString(2),
                                                        resultSet.getString(3),
                                                        resultSet.getString(4),
                                                        new Button("Открыть"),
                                                        resultSet.getBytes(5),
                                                        resultSet.getString(6),
                                                        resultSet.getBoolean(7)));
        }

        if (employeesTable.getItems().size() > 0){
            employeesTable.getSortOrder().clear();
            employeesTable.getSortOrder().add(employeesSurnameColumn);
            employeesTable.sort();
        }
    }

    private void selectCleanings() throws SQLException {
        cleaningsTable.getItems().clear();

        ResultSet resultSet = DatabaseWorker.statement.executeQuery("SELECT * FROM cleanings");

        while (resultSet.next()){
            cleaningsTable.getItems().add(new Cleanings(resultSet.getInt(1), resultSet.getTimestamp(2).toLocalDateTime(), resultSet.getString(3)));
        }

        if (cleaningsTable.getItems().size() > 0){
            cleaningsTable.getSortOrder().clear();
            cleaningsTable.getSortOrder().add(cleaningsDateTimeColumn);
            cleaningsTable.sort();
        }
    }

    private int getFreeBedsBySex(boolean isMale) throws SQLException {
        ResultSet resultSet = DatabaseWorker.statement.executeQuery("SELECT (COALESCE(SUM(beds), 0) - (SELECT COUNT(*) FROM students " +
                                                                        "LEFT JOIN check_ins_outs ON students.id_student = check_ins_outs.id_student " +
                                                                        "WHERE students.sex IS NOT "  + !isMale + " " +
                                                                        "AND check_out_date >= CURRENT_DATE)) " +
                                                                        "FROM rooms " +
                                                                        "WHERE (EXISTS (SELECT students.sex FROM check_ins_outs " +
                                                                        "LEFT JOIN students ON check_ins_outs.id_student = students.id_student " +
                                                                        "WHERE check_ins_outs.id_room = rooms.id_room " +
                                                                        "AND check_ins_outs.check_out_date >= CURRENT_DATE " +
                                                                        "AND students.sex IS NOT " + !isMale + ") " +
                                                                        "OR NOT EXISTS (SELECT id_room FROM check_ins_outs " +
                                                                        "WHERE check_ins_outs.id_room = rooms.id_room " +
                                                                        "AND check_ins_outs.check_out_date >= CURRENT_DATE))");
        resultSet.next();
        return resultSet.getInt(1);
    }

    private int getOccupiedBedsBySex(boolean isMale) throws SQLException {
        ResultSet resultSet = DatabaseWorker.statement.executeQuery("SELECT COUNT(check_ins_outs.id_student) FROM check_ins_outs " +
                                                                        "LEFT JOIN students ON check_ins_outs.id_student = students.id_student " +
                                                                        "WHERE students.sex IS NOT " + !isMale + " " +
                                                                        "AND check_out_date >= CURRENT_DATE");
        resultSet.next();
        return resultSet.getInt(1);
    }


    private void roomTotalInfoDataFill() throws SQLException {
        ResultSet resultSet = DatabaseWorker.statement.executeQuery("SELECT COALESCE(SUM(beds), 0) FROM rooms");
        resultSet.next();
        int total = resultSet.getInt(1);
        String text = total + "\n";

        text += getFreeBedsBySex(true) + " | " + getFreeBedsBySex(false) + "\n";

        text += getOccupiedBedsBySex(true) + " | " + getOccupiedBedsBySex(false);

        roomTotalInfoData.setText(text);
    }

    private void tableIsEmptyMessage()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Таблица пуста");
        alert.setHeaderText(null);
        alert.setContentText("Добавьте данные в таблицу на выбранной вкладке, чтобы воспользоваться этой функцией");
        alert.showAndWait();
    }

    private void studentCheckedOutMessage()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Студент выселен");
        alert.setHeaderText(null);
        alert.setContentText("Выбранный студент выселен, действие недоступно");
        alert.showAndWait();
    }

    public void studentsTable_CellDoubleClick(MouseEvent mouseEvent) throws SQLException, IOException {
        if (mouseEvent.getClickCount() == 2 && studentsTable.getSelectionModel().getSelectedItem() != null){
            studentsEditUpdate_Click();
        }
    }

    private boolean areThereFreeRooms(boolean isMale) throws SQLException {
        ResultSet resultSet = DatabaseWorker.statement.executeQuery("SELECT TRUE FROM RDB$DATABASE WHERE(" +
                "SELECT COUNT(*) FROM rooms " +
                "WHERE beds > (SELECT COUNT(*) FROM check_ins_outs " +
                "WHERE check_ins_outs.id_room = rooms.id_room " +
                "AND check_ins_outs.check_out_date >= CURRENT_DATE) " +
                "AND (EXISTS (SELECT students.sex FROM check_ins_outs " +
                "LEFT JOIN students ON check_ins_outs.id_student = students.id_student " +
                "WHERE check_ins_outs.id_room = rooms.id_room " +
                "AND check_ins_outs.check_out_date >= CURRENT_DATE " +
                "AND students.sex IS NOT " + !isMale + ") " +
                "OR NOT EXISTS (SELECT id_room FROM check_ins_outs " +
                "WHERE check_ins_outs.id_room = rooms.id_room " +
                "AND check_ins_outs.check_out_date >= CURRENT_DATE)))>0");

        boolean result;
        if (!resultSet.next()){
            result = false;
        }
        else{
            result = resultSet.getBoolean(1);
        }

        return result;
    }

    public void studentsEditAddNew_Click() throws IOException, SQLException {
        boolean freeMaleRooms = areThereFreeRooms(true);
        boolean freeFemaleRooms = areThereFreeRooms(false);

        if (!freeMaleRooms && !freeFemaleRooms){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Нет свободных комнат");
            alert.setHeaderText(null);
            alert.setContentText("В общежитии нет свободных комнат");
            alert.showAndWait();

            return;
        }
        else if (!freeMaleRooms){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Нет свободных комнат для мужчин");
            alert.setHeaderText(null);
            alert.setContentText("В общежитии нет свободных комнат для мужчин");
            alert.showAndWait();
        }
        else if (!freeFemaleRooms){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Нет свободных комнат для женщин");
            alert.setHeaderText(null);
            alert.setContentText("В общежитии нет свободных комнат для женщин");
            alert.showAndWait();
        }

        Object[] windowProps = setModalWindow("studentInfo.fxml", "Заселить нового студента");
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        StudentInfoController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams();
        controller.setFreeRoomsData(freeMaleRooms, freeFemaleRooms);
        addStage.showAndWait();

        if (controller.dialogResult.equals("OK")){
            selectStudents();
            roomTotalInfoDataFill();
        }
    }

    public void studentsEditUpdate_Click() throws IOException, SQLException {
        if (studentsTable.getItems().size() == 0)
        {
            tableIsEmptyMessage();
            return;
        }

        if (studentsTable.getSelectionModel().getSelectedItem() == null){
            return;
        }

        boolean freeMaleRooms = areThereFreeRooms(true);
        boolean freeFemaleRooms = areThereFreeRooms(false);

        Object[] windowProps = setModalWindow("studentInfo.fxml", "Изменить данные студента");
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        StudentInfoController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams(studentsTable.getSelectionModel().getSelectedItem());
        controller.setFreeRoomsData(freeMaleRooms, freeFemaleRooms);
        addStage.showAndWait();

        if (controller.dialogResult.equals("OK"))
        {
            selectStudents();
            roomTotalInfoDataFill();
        }
        else if (controller.dialogResult.equals("Abort"))
        {
            selectStudents();
            roomTotalInfoDataFill();
            selectInventory();
            selectVisitors();
        }
    }

    /**
     * Включает/выключает отображение выселенных студентов.
     */
    public void studentsEditOnlyResidents_CheckedChanged() throws SQLException {
        if (studentsEditOnlyResidents.isSelected())
        {
            selectStudentsQueryLastPart = " WHERE check_out_date >= CURRENT_DATE";
        }
        else
        {
            selectStudentsQueryLastPart = "";
        }

        selectStudents();
    }

    public void studentsInventoryGive_Click() throws SQLException, IOException {
        if (studentsTable.getItems().size() == 0)
        {
            tableIsEmptyMessage();
            return;
        }

        if (studentsTable.getSelectionModel().getSelectedItem() == null){
            return;
        }

        if (studentsTable.getSelectionModel().getSelectedItem().getCheckOutDate().compareTo(LocalDate.now())<=0){
            studentCheckedOutMessage();
            return;
        }

        int id = studentsTable.getSelectionModel().getSelectedItem().getId();

        Object[] windowProps = setMultifunctionalModalWindow("Доступный для выдачи инвентарь");
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        MultifunctionalTableWindowController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams(TableState.Add, TableType.Inventory, id);
        addStage.showAndWait();

        if (controller.dialogResult.equals("OK")){
            selectInventory();
        }
    }

    private boolean doesStudentHaveInventory(int id) throws SQLException {
        PreparedStatement ps = DatabaseWorker.connection.prepareStatement("SELECT COUNT(*) FROM issued_inventory WHERE id_student = ?");
        ps.setInt(1, id);

        ResultSet resultSet = ps.executeQuery();
        resultSet.next();

        if (resultSet.getInt(1) == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("Данный студент не получал инвентарь");
            alert.showAndWait();

            return false;
        }

        return true;
    }

    public void studentsInventoryUpdate_Click() throws SQLException, IOException {
        if (studentsTable.getItems().size() == 0)
        {
            tableIsEmptyMessage();
            return;
        }

        if (studentsTable.getSelectionModel().getSelectedItem() == null){
            return;
        }

        if (studentsTable.getSelectionModel().getSelectedItem().getCheckOutDate().compareTo(LocalDate.now())<=0){
            studentCheckedOutMessage();
            return;
        }

        int id = studentsTable.getSelectionModel().getSelectedItem().getId();

        if (!doesStudentHaveInventory(id))
        {
            return;
        }

        Object[] windowProps = setMultifunctionalModalWindow("Инвентарь, выданный студенту " +
                studentsTable.getSelectionModel().getSelectedItem().getSurname() + " " +
                studentsTable.getSelectionModel().getSelectedItem().getName() + " " +
                studentsTable.getSelectionModel().getSelectedItem().getPatronymic());
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        MultifunctionalTableWindowController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams(TableState.Update, TableType.Inventory, id);
        addStage.showAndWait();

        if (controller.dialogResult.equals("OK")){
            selectInventory();
        }
    }

    public void studentsInventorySelect_Click() throws SQLException, IOException {
        if (studentsTable.getItems().size() == 0)
        {
            tableIsEmptyMessage();
            return;
        }

        if (studentsTable.getSelectionModel().getSelectedItem() == null){
            return;
        }

        int id = studentsTable.getSelectionModel().getSelectedItem().getId();

        if (!doesStudentHaveInventory(id))
        {
            return;
        }

        Object[] windowProps = setMultifunctionalModalWindow("Инвентарь, выданный студенту " +
                studentsTable.getSelectionModel().getSelectedItem().getSurname() + " " +
                studentsTable.getSelectionModel().getSelectedItem().getName() + " " +
                studentsTable.getSelectionModel().getSelectedItem().getPatronymic());
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        MultifunctionalTableWindowController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams(TableState.ViewData, TableType.Inventory, id);
        addStage.showAndWait();
    }

    public void studentsViolationsAdd_Click() throws SQLException, IOException {
        if (studentsTable.getItems().size() == 0)
        {
            tableIsEmptyMessage();
            return;
        }

        if (studentsTable.getSelectionModel().getSelectedItem() == null){
            return;
        }

        if (studentsTable.getSelectionModel().getSelectedItem().getCheckOutDate().compareTo(LocalDate.now())<=0){
            studentCheckedOutMessage();
            return;
        }

        int id = studentsTable.getSelectionModel().getSelectedItem().getId();

        ResultSet resultSet = DatabaseWorker.statement.executeQuery("SELECT COUNT(*) FROM violations");
        resultSet.next();

        Object[] windowProps = setModalWindow("violationInfo.fxml", "Добавить новое нарушение");
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        ViolationInfoController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams(id);

        if (resultSet.getInt(1) > 0){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Добавить нарушение студенту");
            alert.setHeaderText(null);
            alert.setContentText("Выбрать из существующих несовершённых нарушений?");
            alert.getButtonTypes().clear();
            alert.getButtonTypes().add(new ButtonType("Да", ButtonType.YES.getButtonData()));
            alert.getButtonTypes().add(new ButtonType("Нет", ButtonType.NO.getButtonData()));
            alert.getButtonTypes().add(new ButtonType("Отмена", ButtonType.CANCEL.getButtonData()));
            alert.showAndWait();

            String first_try = alert.getResult().getText();

            if (first_try.equals("Да")){
                PreparedStatement ps = DatabaseWorker.connection.prepareStatement("SELECT TRUE FROM RDB$DATABASE WHERE (SELECT COUNT(*) FROM violations) > (SELECT COUNT(*) FROM commited_violations WHERE id_student = ?)");
                ps.setInt(1, id);

                resultSet = ps.executeQuery();

                boolean condition;
                if (!resultSet.next()){
                    condition = false;
                }
                else{
                    condition = resultSet.getBoolean(1);
                }

                if (condition){
                    Object[] mtw_windowProps = setMultifunctionalModalWindow("Существующие нарушения");
                    FXMLLoader mtw_loader = (FXMLLoader) mtw_windowProps[0];
                    Stage mtw_addStage = (Stage) mtw_windowProps[1];

                    MultifunctionalTableWindowController mtw_controller = mtw_loader.getController();
                    mtw_controller.setAddStage(mtw_addStage);
                    mtw_controller.setParams(TableState.Select, TableType.Violations, id);
                    mtw_addStage.showAndWait();
                }
                else{
                    Alert inf_alert = new Alert(Alert.AlertType.INFORMATION);
                    inf_alert.setTitle("Информация");
                    inf_alert.setHeaderText(null);
                    inf_alert.setContentText("Не существует нарушений, которых не совершал бы данный студент!");
                    inf_alert.showAndWait();

                    alert.setContentText("Добавить новое нарушение?");
                    alert.getButtonTypes().clear();
                    alert.getButtonTypes().add(new ButtonType("Да", ButtonType.YES.getButtonData()));
                    alert.getButtonTypes().add(new ButtonType("Нет", ButtonType.NO.getButtonData()));
                    alert.showAndWait();

                    if (alert.getResult().getText().equals("Да")){
                        addStage.showAndWait();
                    }
                }
            }
            else if (first_try.equals("Нет")){
                addStage.showAndWait();
            }
        }
        else{
            addStage.showAndWait();
        }
    }

    private boolean doesStudentHaveViolations(int id) throws SQLException {
        PreparedStatement ps = DatabaseWorker.connection.prepareStatement("SELECT COUNT(*) FROM commited_violations WHERE id_student = ?");
        ps.setInt(1, id);

        ResultSet resultSet = ps.executeQuery();
        resultSet.next();

        if (resultSet.getInt(1) == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("Данный студент не совершал нарушений");
            alert.showAndWait();

            return false;
        }

        return true;
    }

    public void studentsViolationsUpdate_Click() throws SQLException, IOException {
        if (studentsTable.getItems().size() == 0)
        {
            tableIsEmptyMessage();
            return;
        }

        if (studentsTable.getSelectionModel().getSelectedItem() == null){
            return;
        }

        if (studentsTable.getSelectionModel().getSelectedItem().getCheckOutDate().compareTo(LocalDate.now())<=0){
            studentCheckedOutMessage();
            return;
        }

        int id = studentsTable.getSelectionModel().getSelectedItem().getId();

        if (!doesStudentHaveViolations(id))
        {
            return;
        }

        Object[] windowProps = setMultifunctionalModalWindow("Нарушения, совершённые студентом " +
                studentsTable.getSelectionModel().getSelectedItem().getSurname() + " " +
                studentsTable.getSelectionModel().getSelectedItem().getName() + " " +
                studentsTable.getSelectionModel().getSelectedItem().getPatronymic());
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        MultifunctionalTableWindowController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams(TableState.Update, TableType.Violations, id);
        addStage.showAndWait();
    }

    public void studentsViolationsSelect_Click() throws SQLException, IOException {
        if (studentsTable.getItems().size() == 0)
        {
            tableIsEmptyMessage();
            return;
        }

        if (studentsTable.getSelectionModel().getSelectedItem() == null){
            return;
        }

        int id = studentsTable.getSelectionModel().getSelectedItem().getId();

        if (!doesStudentHaveViolations(id))
        {
            return;
        }

        Object[] windowProps = setMultifunctionalModalWindow("Нарушения, совершённые студентом " +
                studentsTable.getSelectionModel().getSelectedItem().getSurname() + " " +
                studentsTable.getSelectionModel().getSelectedItem().getName() + " " +
                studentsTable.getSelectionModel().getSelectedItem().getPatronymic());
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        MultifunctionalTableWindowController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams(TableState.ViewData, TableType.Violations, id);
        addStage.showAndWait();
    }

    public void studentsVisitorsSelect_Click() throws SQLException, IOException {
        if (studentsTable.getItems().size() == 0)
        {
            tableIsEmptyMessage();
            return;
        }

        if (studentsTable.getSelectionModel().getSelectedItem() == null){
            return;
        }

        int id = studentsTable.getSelectionModel().getSelectedItem().getId();

        PreparedStatement ps = DatabaseWorker.connection.prepareStatement("SELECT COUNT(*) FROM visitors WHERE id_student = ?");
        ps.setInt(1, id);

        ResultSet resultSet = ps.executeQuery();
        resultSet.next();

        if (resultSet.getInt(1) == 0)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("К данному студенту не приходили посетители");
            alert.showAndWait();

            return;
        }

        Object[] windowProps = setMultifunctionalModalWindow("Посетители студента " +
                studentsTable.getSelectionModel().getSelectedItem().getSurname() + " " +
                studentsTable.getSelectionModel().getSelectedItem().getName() + " " +
                studentsTable.getSelectionModel().getSelectedItem().getPatronymic());
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        MultifunctionalTableWindowController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams(TableState.ViewData, TableType.Visitors, id);
        addStage.showAndWait();
    }

    public void roomsTable_CellDoubleClick(MouseEvent mouseEvent) throws SQLException, IOException {
        if (mouseEvent.getClickCount() == 2 && roomsTable.getSelectionModel().getSelectedItem() != null){
            roomsButtonsUpdate_Click();
        }
    }

    public void roomsButtonsAdd_Click() throws IOException, SQLException {
        Object[] windowProps = setModalWindow("roomInfo.fxml", "Добавить новую комнату");
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        RoomInfoController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams();
        addStage.showAndWait();

        if (controller.dialogResult.equals("OK")){
            selectRooms();
        }
    }

    public void roomsButtonsUpdate_Click() throws IOException, SQLException {
        if (roomsTable.getItems().size() == 0)
        {
            tableIsEmptyMessage();
            return;
        }

        if (roomsTable.getSelectionModel().getSelectedItem() == null){
            return;
        }

        Object[] windowProps = setModalWindow("roomInfo.fxml", "Изменить сведения о комнате");
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        RoomInfoController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams(roomsTable.getSelectionModel().getSelectedItem());
        addStage.showAndWait();

        if (controller.dialogResult.equals("OK") || controller.dialogResult.equals("Abort"))
        {
            selectRooms();
            selectStudents();
        }
        else if (controller.dialogResult.equals("Retry"))
        {
            windowProps = setMultifunctionalModalWindow("Проживающие в комнате №"+roomsTable.getSelectionModel().getSelectedItem().getRoomNumber());
            loader = (FXMLLoader) windowProps[0];
            addStage = (Stage) windowProps[1];

            MultifunctionalTableWindowController controller2 = loader.getController();
            controller2.setAddStage(addStage);
            controller2.setParams(TableState.ViewData, TableType.Residents, roomsTable.getSelectionModel().getSelectedItem().getId());
            addStage.showAndWait();
        }
    }

    public void roomsButtonsSelectResidents_Click() throws SQLException, IOException {
        if (roomsTable.getItems().size() == 0)
        {
            tableIsEmptyMessage();
            return;
        }

        if (roomsTable.getSelectionModel().getSelectedItem() == null){
            return;
        }

        int id = roomsTable.getSelectionModel().getSelectedItem().getId();

        PreparedStatement ps = DatabaseWorker.connection.prepareStatement("SELECT COUNT(*) FROM check_ins_outs WHERE id_room = ? AND check_out_date >= CURRENT_DATE");
        ps.setInt(1, id);

        ResultSet resultSet = ps.executeQuery();
        resultSet.next();

        if (resultSet.getInt(1) == 0)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("В данной комнате никто не проживает");
            alert.showAndWait();

            return;
        }

        Object[] windowProps = setMultifunctionalModalWindow("Проживающие в комнате №"+roomsTable.getSelectionModel().getSelectedItem().getRoomNumber());
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        MultifunctionalTableWindowController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams(TableState.ViewData, TableType.Residents, id);
        addStage.showAndWait();
    }

    public void inventoryTable_CellDoubleClick(MouseEvent mouseEvent) throws SQLException, IOException {
        if (mouseEvent.getClickCount() == 2 && inventoryTable.getSelectionModel().getSelectedItem() != null){
            inventoryButtonsUpdate_Click();
        }
    }

    public void inventoryButtonsAdd_Click() throws IOException, SQLException {
        Object[] windowProps = setModalWindow("inventoryInfo.fxml", "Добавить новый вид инвентаря");
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        InventoryInfoController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams();
        addStage.showAndWait();

        if (controller.dialogResult.equals("OK")){
            selectInventory();
        }
    }

    public void inventoryButtonsUpdate_Click() throws IOException, SQLException {
        if (inventoryTable.getItems().size() == 0)
        {
            tableIsEmptyMessage();
            return;
        }

        if (inventoryTable.getSelectionModel().getSelectedItem() == null){
            return;
        }

        Object[] windowProps = setModalWindow("inventoryInfo.fxml", "Изменить сведения о виде инвентаря");
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        InventoryInfoController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams(inventoryTable.getSelectionModel().getSelectedItem());
        addStage.showAndWait();

        if (controller.dialogResult.equals("OK") || controller.dialogResult.equals("Abort"))
        {
            selectInventory();
        }
    }

    public void inventoryButtonsSelectHolders_Click() throws SQLException, IOException {
        if (inventoryTable.getItems().size() == 0)
        {
            tableIsEmptyMessage();
            return;
        }

        if (inventoryTable.getSelectionModel().getSelectedItem() == null){
            return;
        }

        if (inventoryTable.getSelectionModel().getSelectedItem().getCountIssued() == 0)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("Весь имеющийся инвентарь данного вида находится на складе");
            alert.showAndWait();

            return;
        }

        int id = inventoryTable.getSelectionModel().getSelectedItem().getId();

        Object[] windowProps = setMultifunctionalModalWindow(inventoryTable.getSelectionModel().getSelectedItem().getType() + ": обладатели");
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        MultifunctionalTableWindowController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams(TableState.ViewData, TableType.Holders, id);
        addStage.showAndWait();
    }

    public void visitorsTable_CellDoubleClick(MouseEvent mouseEvent) throws SQLException, IOException {
        if (mouseEvent.getClickCount() == 2 && visitorsTable.getSelectionModel().getSelectedItem() != null){
            visitorsButtonsUpdate_Click();
        }
    }

    public void visitorsButtonsAdd_Click() throws IOException, SQLException {
        Object[] windowProps = setModalWindow("visitorInfo.fxml", "Добавить нового посетителя");
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        VisitorInfoController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams();
        addStage.showAndWait();

        if (controller.dialogResult.equals("OK")){
            selectVisitors();
        }
    }

    public void visitorsButtonsUpdate_Click() throws IOException, SQLException {
        if (visitorsTable.getItems().size() == 0)
        {
            tableIsEmptyMessage();
            return;
        }

        if (visitorsTable.getSelectionModel().getSelectedItem() == null){
            return;
        }

        Object[] windowProps = setModalWindow("visitorInfo.fxml", "Изменить данные посетителя");
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        VisitorInfoController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams(visitorsTable.getSelectionModel().getSelectedItem());
        addStage.showAndWait();

        if (controller.dialogResult.equals("OK") || controller.dialogResult.equals("Abort"))
        {
            selectVisitors();
        }
    }

    /**
     * Находит в таблице студентов посещаемого выбранным посетителем студента.
     * Если он выселен, возвращает сообщение об этом.
     */
    public void visitorsButtonsFind_Click(){
        if (visitorsTable.getItems().size() == 0)
        {
            tableIsEmptyMessage();
            return;
        }

        if (visitorsTable.getSelectionModel().getSelectedItem() == null){
            return;
        }

        Students student = null;
        for (var item : studentsTable.getItems()) {
            if (item.getId() == visitorsTable.getSelectionModel().getSelectedItem().getStudentID()){
                student = item;
                break;
            }
        }

        if (student == null){
            ErrorMessage.show("Студент " + visitorsTable.getSelectionModel().getSelectedItem().getVisited() + " выехал из общежития. " +
                                   "Для просмотра сведений о нём отключите опцию \"Только проживающие в данный момент\" на вкладке \"Студенты\".");
        }
        else{
            tabs.getSelectionModel().select(0);
            studentsTable.getSelectionModel().select(student);
            studentsTable.scrollTo(student);
        }
    }

    public void employeesTable_CellDoubleClick(MouseEvent mouseEvent) throws SQLException, IOException {
        if (mouseEvent.getClickCount() == 2 && employeesTable.getSelectionModel().getSelectedItem() != null){
            employeesButtonsUpdate_Click();
        }
    }

    public void employeesButtonsAdd_Click() throws IOException, SQLException {
        Object[] windowProps = setModalWindow("employeeInfo.fxml", "Добавить нового сотрудника");
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        EmployeeInfoController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams();
        addStage.showAndWait();

        if (controller.dialogResult.equals("OK")){
            employeesButtonsOnlyArranged_CheckedChanged();
        }
    }

    public void employeesButtonsUpdate_Click() throws IOException, SQLException {
        if (employeesTable.getItems().size() == 0)
        {
            tableIsEmptyMessage();
            return;
        }

        if (employeesTable.getSelectionModel().getSelectedItem() == null){
            return;
        }

        Object[] windowProps = setModalWindow("employeeInfo.fxml", "Изменить данные сотрудника");
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        EmployeeInfoController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams(employeesTable.getSelectionModel().getSelectedItem());
        addStage.showAndWait();

        if (controller.dialogResult.equals("OK") || controller.dialogResult.equals("Abort"))
        {
            employeesButtonsOnlyArranged_CheckedChanged();
        }
    }

    public void employeesButtonsSelectCleanings_Click() throws SQLException, IOException {
        if (employeesTable.getItems().size() == 0)
        {
            tableIsEmptyMessage();
            return;
        }

        if (employeesTable.getSelectionModel().getSelectedItem() == null){
            return;
        }

        int id = employeesTable.getSelectionModel().getSelectedItem().getId();

        PreparedStatement ps = DatabaseWorker.connection.prepareStatement("SELECT COUNT(*) FROM performed_cleanings WHERE id_employee = ?");
        ps.setInt(1, id);

        ResultSet resultSet = ps.executeQuery();
        resultSet.next();

        if (resultSet.getInt(1) == 0)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("Данный сотрудник не совершал уборок");
            alert.showAndWait();

            return;
        }

        Object[] windowProps = setMultifunctionalModalWindow("Уборки, совершённые сотрудником " +
                                                             employeesTable.getSelectionModel().getSelectedItem().getSurname() + " " +
                                                             employeesTable.getSelectionModel().getSelectedItem().getName() + " " +
                                                             employeesTable.getSelectionModel().getSelectedItem().getPatronymic());
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        MultifunctionalTableWindowController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams(TableState.ViewData, TableType.Cleanings, id);
        addStage.showAndWait();
    }

    /**
     * Включает/выключает отображение уволенных сотрудников.
     */
    public void employeesButtonsOnlyArranged_CheckedChanged() throws SQLException {
        if (employeesButtonsOnlyArranged.isSelected())
        {
            selectEmployeesQuery = "SELECT * FROM employees WHERE is_working_now = TRUE";
        }
        else
        {
            selectEmployeesQuery = "SELECT * FROM employees";
        }

        selectEmployees();
    }

    public void cleaningsTable_CellDoubleClick(MouseEvent mouseEvent) throws SQLException, IOException {
        if (mouseEvent.getClickCount() == 2 && cleaningsTable.getSelectionModel().getSelectedItem() != null){
            cleaningsButtonsUpdate_Click();
        }
    }

    public void cleaningsButtonsAdd_Click() throws IOException, SQLException {
        Object[] windowProps = setModalWindow("cleaningInfo.fxml", "Добавить новую уборку");
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        addStage.setOnCloseRequest(event -> {
            CleaningInfoController.employeesListID.clear();
            CleaningInfoController.areThereAnybodyWhoAreNotWorkingNow = false;
        });

        CleaningInfoController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams();
        addStage.showAndWait();

        if (controller.dialogResult.equals("OK")){
            selectCleanings();
        }
    }

    public void cleaningsButtonsUpdate_Click() throws IOException, SQLException {
        if (cleaningsTable.getItems().size() == 0)
        {
            tableIsEmptyMessage();
            return;
        }

        if (cleaningsTable.getSelectionModel().getSelectedItem() == null){
            return;
        }

        Object[] windowProps = setModalWindow("cleaningInfo.fxml", "Изменить сведения об уборке");
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        addStage.setOnCloseRequest(event -> {
            CleaningInfoController.employeesListID.clear();
            CleaningInfoController.areThereAnybodyWhoAreNotWorkingNow = false;
        });

        CleaningInfoController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams(cleaningsTable.getSelectionModel().getSelectedItem());
        addStage.showAndWait();

        if (controller.dialogResult.equals("OK") || controller.dialogResult.equals("Abort"))
        {
            selectCleanings();
        }
    }

    public void cleaningsButtonsSelectEmployees_Click() throws SQLException, IOException {
        if (cleaningsTable.getItems().size() == 0)
        {
            tableIsEmptyMessage();
            return;
        }

        if (cleaningsTable.getSelectionModel().getSelectedItem() == null){
            return;
        }

        int id = cleaningsTable.getSelectionModel().getSelectedItem().getId();

        String date = cleaningsTable.getSelectionModel().getSelectedItem().getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String time = cleaningsTable.getSelectionModel().getSelectedItem().getDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        Object[] windowProps = setMultifunctionalModalWindow("Сотрудники, убиравшиеся " + date + " в " + time);
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        MultifunctionalTableWindowController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams(TableState.ViewData, TableType.Cleaners, id);
        addStage.showAndWait();
    }

    /**
     * Открывает окно настроек.
     * @param atStartUp указание на источник
     */
    private void settingsWindow(boolean atStartUp) throws IOException, SQLException {
        String title;
        if (atStartUp){
            title = "Выберите файл базы данных";
        }
        else{
            title = "Настройки";
        }

        Object[] windowProps = setModalWindow("settingsWindow.fxml", title);
        FXMLLoader loader = (FXMLLoader) windowProps[0];
        Stage addStage = (Stage) windowProps[1];

        SettingsWindowController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setParams(atStartUp);
        addStage.showAndWait();

        if (controller.dialogResult.equals("OK") && !atStartUp){
            loadAllTables();
        }
    }
}