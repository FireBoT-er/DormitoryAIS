package ru.cw.dormitoryais.Controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.cw.dormitoryais.DatabaseWorker;
import ru.cw.dormitoryais.Models.*;
import ru.cw.dormitoryais.Program;
import ru.cw.dormitoryais.TableState;
import ru.cw.dormitoryais.TableType;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Окно, в котором находится таблица ({@link #multifunctionalTable}) и 3 кнопки для работы с ней.
 * Используется для отображения различной информации. <br>
 * Кнопки: <br>
 * {@link #detachB} - для функции открепления (см. {@link TableType#Violations}); <br>
 * {@link #multifunctionalB} - многофункиональная кнопка; <br>
 * {@link #cancelB} - кнопка отмены или выхода. <br>
 * Кнопки могут быть скрыты при необходимости.
 * @see TableState
 * @see TableType
 */
public class MultifunctionalTableWindowController {
    private Stage dialogStage;

    /**
     * Устанавливает сцену для данного окна. <br>
     * Использовать после получения экземпляра, иначе окно не откроется.
     * @param addStage сцена
     */
    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    /**
     * Многофункциональная таблица.
     * @see #setParams(TableState, TableType, int, boolean)
     */
    public TableView multifunctionalTable;
    public HBox buttonsBox;
    /**
     * @see #detachB_Click()
     */
    public Button detachB;
    /**
     * @see #multifunctionalB_Click()
     */
    public Button multifunctionalB;
    /**
     * @see #cancelB_Click()
     */
    public Button cancelB;

    private int id = 0;
    private TableState tableState;
    private TableType tableType;

    /**
     * Устанавливает все возможные значения для окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     * @param id идентификатор вызывающего объекта
     * @param boolData какое-либо логическое значение
     */
    public void setParams(TableState tableState, TableType tableType, int id, boolean boolData) throws SQLException {
        if (tableState == TableState.ViewData)
        {
            buttonsBox.setVisible(false);
            buttonsBox.setManaged(false);
        }

        detachB.setVisible(false);

        this.id = id;
        this.tableState = tableState;
        this.tableType = tableType;

        PreparedStatement ps;
        ResultSet resultSet;

        if (tableType == TableType.Inventory)
        {
            cancelB.setText("Закрыть");

            if (tableState == TableState.Add){
                TableView<Inventory> inventoryTable = multifunctionalTable;

                TableColumn<Inventory, Integer> inventoryIdColumn = new TableColumn<>();
                inventoryIdColumn.setVisible(false);
                inventoryIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

                TableColumn<Inventory, String> inventoryTypeColumn = new TableColumn<>();
                inventoryTypeColumn.setPrefWidth(75.0);
                inventoryTypeColumn.setReorderable(false);
                inventoryTypeColumn.setText("Вид");
                inventoryTypeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());

                TableColumn<Inventory, Integer> inventoryCountInStockColumn = new TableColumn<>();
                inventoryCountInStockColumn.setReorderable(false);
                inventoryCountInStockColumn.setText("На складе");
                inventoryCountInStockColumn.setCellValueFactory(cellData -> cellData.getValue().countInStockProperty().asObject());

                inventoryTable.getColumns().addAll(inventoryIdColumn, inventoryTypeColumn, inventoryCountInStockColumn);

                multifunctionalB.setText("Выдать");

                inventoryAddSelect();
            }
            else{
                TableView<InventoryGiven> inventoryGivenTable = multifunctionalTable;

                TableColumn<InventoryGiven, Integer> inventoryGivenInventoryIdColumn = new TableColumn<>();
                inventoryGivenInventoryIdColumn.setVisible(false);
                inventoryGivenInventoryIdColumn.setCellValueFactory(cellData -> cellData.getValue().inventoryIDProperty().asObject());

                TableColumn<InventoryGiven, String> inventoryGivenTypeColumn = new TableColumn<>();
                inventoryGivenTypeColumn.setPrefWidth(75.0);
                inventoryGivenTypeColumn.setReorderable(false);
                inventoryGivenTypeColumn.setText("Вид");
                inventoryGivenTypeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());

                TableColumn<InventoryGiven, Integer> inventoryGivenIdColumn = new TableColumn<>();
                inventoryGivenIdColumn.setVisible(false);
                inventoryGivenIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

                TableColumn<InventoryGiven, Integer> inventoryGivenCountColumn = new TableColumn<>();
                inventoryGivenCountColumn.setPrefWidth(75.0);
                inventoryGivenCountColumn.setReorderable(false);
                inventoryGivenCountColumn.setText("Количество");
                inventoryGivenCountColumn.setCellValueFactory(cellData -> cellData.getValue().countProperty().asObject());

                TableColumn<InventoryGiven, LocalDate> inventoryGivenIssueDateColumn = new TableColumn<>();
                inventoryGivenIssueDateColumn.setPrefWidth(75.0);
                inventoryGivenIssueDateColumn.setReorderable(false);
                inventoryGivenIssueDateColumn.setText("Дата выдачи");
                inventoryGivenIssueDateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper(cellData.getValue().getIssueDate()));
                inventoryGivenIssueDateColumn.setCellFactory(cellData -> new TableCell<>() {
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
                inventoryGivenIssueDateColumn.setSortType(TableColumn.SortType.DESCENDING);

                TableColumn<InventoryGiven, LocalDate> inventoryGivenTurnInDateColumn = new TableColumn<>();
                inventoryGivenTurnInDateColumn.setReorderable(false);
                inventoryGivenTurnInDateColumn.setText("Дата сдачи");
                inventoryGivenTurnInDateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper(cellData.getValue().getTurnInDate()));
                inventoryGivenTurnInDateColumn.setCellFactory(cellData -> new TableCell<>() {
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

                inventoryGivenTable.getColumns().addAll(inventoryGivenInventoryIdColumn, inventoryGivenTypeColumn, inventoryGivenIdColumn, inventoryGivenCountColumn, inventoryGivenIssueDateColumn, inventoryGivenTurnInDateColumn);

                inventoryUpdateSelect();
            }
        }
        else if (tableType == TableType.Violations)
        {
            TableView<Violations> violationsTable = multifunctionalTable;

            TableColumn<Violations, Integer> violationsIdColumn = new TableColumn<>();
            violationsIdColumn.setVisible(false);
            violationsIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

            TableColumn<Violations, String> violationsDescriptionColumn = new TableColumn<>();
            violationsDescriptionColumn.setPrefWidth(75.0);
            violationsDescriptionColumn.setReorderable(false);
            violationsDescriptionColumn.setText("Описание");
            violationsDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

            TableColumn<Violations, String> violationsPunishmentColumn = new TableColumn<>();
            violationsPunishmentColumn.setPrefWidth(75.0);
            violationsPunishmentColumn.setReorderable(false);
            violationsPunishmentColumn.setText("Мера наказания");
            violationsPunishmentColumn.setCellValueFactory(cellData -> cellData.getValue().punishmentProperty());

            TableColumn<Violations, LocalDateTime> violationsDateTimeColumn = new TableColumn<>();
            violationsDateTimeColumn.setReorderable(false);
            violationsDateTimeColumn.setText("Дата и время совершения");
            violationsDateTimeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper(cellData.getValue().getDateTime()));
            violationsDateTimeColumn.setCellFactory(cellData -> new TableCell<>() {
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
            violationsDateTimeColumn.setSortType(TableColumn.SortType.DESCENDING);

            violationsTable.getColumns().addAll(violationsIdColumn, violationsDescriptionColumn, violationsPunishmentColumn, violationsDateTimeColumn);

            if (tableState == TableState.Select){
                ps = DatabaseWorker.connection.prepareStatement("SELECT DISTINCT violations.* FROM violations " +
                        "LEFT JOIN commited_violations ON violations.id_violation = commited_violations.id_violation " +
                        "WHERE commited_violations.id_student != ? " +
                        "AND NOT EXISTS (SELECT * FROM commited_violations " +
                        "WHERE id_student = ? " +
                        "AND violations.id_violation = commited_violations.id_violation)");
                ps.setInt(1, id);
                ps.setInt(2, id);

                multifunctionalB.setText("Добавить");
            }
            else{
                if (tableState == TableState.Update){
                    detachB.setVisible(true);
                    cancelB.setText("Закрыть");
                }

                ps = DatabaseWorker.connection.prepareStatement("SELECT violations.* FROM violations " +
                        "LEFT JOIN commited_violations ON violations.id_violation = commited_violations.id_violation " +
                        "WHERE commited_violations.id_student = ?");
                ps.setInt(1, id);
            }

            resultSet = ps.executeQuery();

            while (resultSet.next()){
                violationsTable.getItems().add(new Violations(resultSet.getInt(1),
                                                              resultSet.getString(2),
                                                              resultSet.getString(3),
                                                              resultSet.getTimestamp(4).toLocalDateTime()));
            }

            violationsTable.getSortOrder().clear();
            violationsTable.getSortOrder().add(violationsDateTimeColumn);
            violationsTable.sort();
        }
        else if (tableType == TableType.Visitors)
        {
            TableView<Visitors> visitorsTable = multifunctionalTable;

            TableColumn<Visitors, Integer> visitorsIdColumn = new TableColumn<>();
            visitorsIdColumn.setVisible(false);
            visitorsIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

            TableColumn<Visitors, String> visitorsSurnameColumn = new TableColumn<>();
            visitorsSurnameColumn.setPrefWidth(75.0);
            visitorsSurnameColumn.setReorderable(false);
            visitorsSurnameColumn.setText("Фамилия");
            visitorsSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());

            TableColumn<Visitors, String> visitorsNameColumn = new TableColumn<>();
            visitorsNameColumn.setPrefWidth(75.0);
            visitorsNameColumn.setReorderable(false);
            visitorsNameColumn.setText("Имя");
            visitorsNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

            TableColumn<Visitors, String> visitorsPatronymicColumn = new TableColumn<>();
            visitorsPatronymicColumn.setPrefWidth(75.0);
            visitorsPatronymicColumn.setReorderable(false);
            visitorsPatronymicColumn.setText("Отчество");
            visitorsPatronymicColumn.setCellValueFactory(cellData -> cellData.getValue().patronymicProperty());

            TableColumn<Visitors, String> visitorsPhoneColumn = new TableColumn<>();
            visitorsPhoneColumn.setPrefWidth(75.0);
            visitorsPhoneColumn.setReorderable(false);
            visitorsPhoneColumn.setText("Номер телефона");
            visitorsPhoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());

            TableColumn<Visitors, LocalDateTime> visitorsDateTimeColumn = new TableColumn<>();
            visitorsDateTimeColumn.setReorderable(false);
            visitorsDateTimeColumn.setText("Дата и время посещения");
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

            visitorsTable.getColumns().addAll(visitorsIdColumn, visitorsSurnameColumn, visitorsNameColumn, visitorsPatronymicColumn, visitorsPhoneColumn, visitorsDateTimeColumn);

            ps = DatabaseWorker.connection.prepareStatement("SELECT * FROM visitors WHERE id_student = ?");
            ps.setInt(1, id);

            resultSet = ps.executeQuery();

            while (resultSet.next()){
                visitorsTable.getItems().add(new Visitors(resultSet.getInt(1),
                                                          resultSet.getString(2),
                                                          resultSet.getString(3),
                                                          resultSet.getString(4),
                                                          resultSet.getString(5),
                                                          resultSet.getTimestamp(6).toLocalDateTime(),
                                                          0, ""));
            }

            visitorsTable.getSortOrder().clear();
            visitorsTable.getSortOrder().add(visitorsDateTimeColumn);
            visitorsTable.sort();
        }
        else if (tableType == TableType.Residents)
        {
            TableView<Students> studentsTable = multifunctionalTable;

            TableColumn<Students, Integer> studentsIdColumn = new TableColumn<>();
            studentsIdColumn.setVisible(false);
            studentsIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
            studentsTable.getColumns().add(studentsIdColumn);

            addBaseStudentHeader(studentsTable);

            TableColumn<Students, LocalDate> studentsCheckInDateColumn = new TableColumn<>();
            studentsCheckInDateColumn.setPrefWidth(75.0);
            studentsCheckInDateColumn.setReorderable(false);
            studentsCheckInDateColumn.setText("Дата заселения");
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

            TableColumn<Students, LocalDate> studentsCheckOutDateColumn = new TableColumn<>();
            studentsCheckOutDateColumn.setReorderable(false);
            studentsCheckOutDateColumn.setText("Дата выселения");
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

            studentsTable.getColumns().addAll(studentsCheckInDateColumn, studentsCheckOutDateColumn);

            ps = DatabaseWorker.connection.prepareStatement("SELECT students.*, check_ins_outs.* FROM students " +
                                                                 "LEFT JOIN check_ins_outs ON students.id_student = check_ins_outs.id_student " +
                                                                 "WHERE id_room = ? " +
                                                                 "AND check_out_date >= CURRENT_DATE");
            ps.setInt(1, id);

            resultSet = ps.executeQuery();

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
                                                           0,
                                                           resultSet.getDate(11).toLocalDate(),
                                                           resultSet.getDate(12).toLocalDate()));
            }

            studentsTable.sort();
        }
        else if (tableType == TableType.Holders)
        {
            TableView<Students> studentsTable = multifunctionalTable;

            TableColumn<Students, Integer> studentsIdColumn = new TableColumn<>();
            studentsIdColumn.setVisible(false);
            studentsIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
            studentsTable.getColumns().add(studentsIdColumn);

            addBaseStudentHeader(studentsTable);

            TableColumn<Students, Integer> studentsRoomNumberColumn = new TableColumn<>();
            studentsRoomNumberColumn.setPrefWidth(75.0);
            studentsRoomNumberColumn.setReorderable(false);
            studentsRoomNumberColumn.setText("Номер комнаты");
            studentsRoomNumberColumn.setCellValueFactory(cellData -> cellData.getValue().roomNumberProperty().asObject());

            TableColumn<Students, Integer> studentsInvCountColumn = new TableColumn<>();
            studentsInvCountColumn.setPrefWidth(75.0);
            studentsInvCountColumn.setReorderable(false);
            studentsInvCountColumn.setText("Количество");
            studentsInvCountColumn.setCellValueFactory(cellData -> cellData.getValue().invCountProperty().asObject());

            TableColumn<Students, LocalDate> studentsInvIssueDateColumn = new TableColumn<>();
            studentsInvIssueDateColumn.setPrefWidth(75.0);
            studentsInvIssueDateColumn.setReorderable(false);
            studentsInvIssueDateColumn.setText("Дата выдачи");
            studentsInvIssueDateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper(cellData.getValue().getInvIssueDate()));
            studentsInvIssueDateColumn.setCellFactory(cellData -> new TableCell<>() {
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

            TableColumn<Students, LocalDate> studentsInvTurnInDateColumn = new TableColumn<>();
            studentsInvTurnInDateColumn.setReorderable(false);
            studentsInvTurnInDateColumn.setText("Дата сдачи");
            studentsInvTurnInDateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper(cellData.getValue().getInvTurnInDate()));
            studentsInvTurnInDateColumn.setCellFactory(cellData -> new TableCell<>() {
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

            studentsTable.getColumns().addAll(studentsRoomNumberColumn, studentsInvCountColumn, studentsInvIssueDateColumn, studentsInvTurnInDateColumn);

            ps = DatabaseWorker.connection.prepareStatement("SELECT students.*, issued_inventory.*, rooms.room_number FROM students " +
                                                                "LEFT JOIN issued_inventory ON students.id_student = issued_inventory.id_student " +
                                                                "LEFT JOIN check_ins_outs ON students.id_student = check_ins_outs.id_student " +
                                                                "LEFT JOIN rooms ON check_ins_outs.id_room = rooms.id_room " +
                                                                "WHERE issued_inventory.id_inventory = ? " +
                                                                "AND turn_in_date >= CURRENT_DATE");
            ps.setInt(1, id);

            resultSet = ps.executeQuery();

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
                                                          resultSet.getInt(15),
                                                          resultSet.getInt(14),
                                                          resultSet.getDate(12).toLocalDate(),
                                                          resultSet.getDate(13).toLocalDate()));
            }

            studentsTable.sort();
        }
        else if (tableType == TableType.Cleanings)
        {
            TableView<Cleanings> cleaningsTable = multifunctionalTable;

            TableColumn<Cleanings, Integer> cleaningsIdColumn = new TableColumn<>();
            cleaningsIdColumn.setVisible(false);
            cleaningsIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

            TableColumn<Cleanings, LocalDateTime> cleaningsDateTimeColumn = new TableColumn<>();
            cleaningsDateTimeColumn.setReorderable(false);
            cleaningsDateTimeColumn.setText("Дата и время уборки");
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

            TableColumn<Cleanings, String> cleaningsCleanedColumn = new TableColumn<>();
            cleaningsCleanedColumn.setReorderable(false);
            cleaningsCleanedColumn.setText("Убранные помещения и территории");
            cleaningsCleanedColumn.setCellValueFactory(cellData -> cellData.getValue().cleanedProperty());

            cleaningsTable.getColumns().addAll(cleaningsIdColumn, cleaningsDateTimeColumn, cleaningsCleanedColumn);

            ps = DatabaseWorker.connection.prepareStatement("SELECT cleanings.* FROM cleanings " +
                                                                "LEFT JOIN performed_cleanings ON cleanings.id_cleaning = performed_cleanings.id_cleaning " +
                                                                "WHERE id_employee = ?");
            ps.setInt(1, id);

            resultSet = ps.executeQuery();

            while (resultSet.next()){
                cleaningsTable.getItems().add(new Cleanings(resultSet.getInt(1),
                                                            resultSet.getTimestamp(2).toLocalDateTime(),
                                                            resultSet.getString(3)));
            }

            cleaningsTable.getSortOrder().clear();
            cleaningsTable.getSortOrder().add(cleaningsDateTimeColumn);
            cleaningsTable.sort();
        }
        else if (tableType == TableType.Cleaners)
        {
            TableView<Employees> employeesTable = multifunctionalTable;

            TableColumn<Employees, Integer> employeesIdColumn = new TableColumn<>();
            employeesIdColumn.setVisible(false);
            employeesIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

            TableColumn<Employees, String> employeesSurnameColumn = new TableColumn<>();
            employeesSurnameColumn.setPrefWidth(75.0);
            employeesSurnameColumn.setReorderable(false);
            employeesSurnameColumn.setText("Фамилия");
            employeesSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());

            TableColumn<Employees, String> employeesNameColumn = new TableColumn<>();
            employeesNameColumn.setPrefWidth(75.0);
            employeesNameColumn.setReorderable(false);
            employeesNameColumn.setText("Имя");
            employeesNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

            TableColumn<Employees, String> employeesPatronymicColumn = new TableColumn<>();
            employeesPatronymicColumn.setPrefWidth(75.0);
            employeesPatronymicColumn.setReorderable(false);
            employeesPatronymicColumn.setText("Отчество");
            employeesPatronymicColumn.setCellValueFactory(cellData -> cellData.getValue().patronymicProperty());

            TableColumn<Employees, Button> employeesPhotoColumn = new TableColumn<>();
            employeesPhotoColumn.setMaxWidth(85.0);
            employeesPhotoColumn.setMinWidth(85.0);
            employeesPhotoColumn.setPrefWidth(85.0);
            employeesPhotoColumn.setReorderable(false);
            employeesPhotoColumn.setResizable(false);
            employeesPhotoColumn.setSortable(false);
            employeesPhotoColumn.setText("Фотография");
            employeesPhotoColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper(cellData.getValue().getPhoto()));

            TableColumn<Employees, String> employeesPositionColumn = new TableColumn<>();
            employeesPositionColumn.setPrefWidth(75.0);
            employeesPositionColumn.setReorderable(false);
            employeesPositionColumn.setText("Должность");
            employeesPositionColumn.setCellValueFactory(cellData -> cellData.getValue().positionProperty());
            employeesPositionColumn.setSortType(TableColumn.SortType.DESCENDING);

            employeesTable.getColumns().addAll(employeesIdColumn, employeesSurnameColumn, employeesNameColumn, employeesPatronymicColumn, employeesPhotoColumn, employeesPositionColumn);

            if (tableState == TableState.Select){
                multifunctionalB.setText("Выбрать");

                employeesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

                if (CleaningInfoController.areThereAnybodyWhoAreNotWorkingNow)
                {
                    TableColumn<Employees, String> employeesIsWorkingNowColumn = new TableColumn<>();
                    employeesIsWorkingNowColumn.setReorderable(false);
                    employeesIsWorkingNowColumn.setText("Трудоустроен(а)");
                    employeesIsWorkingNowColumn.setCellValueFactory(cellData -> cellData.getValue().isIsWorkingNow()?new SimpleStringProperty("Да") :new SimpleStringProperty("Нет"));
                    employeesTable.getColumns().add(employeesIsWorkingNowColumn);

                    resultSet = DatabaseWorker.statement.executeQuery("SELECT * FROM employees");
                }
                else
                {
                    resultSet = DatabaseWorker.statement.executeQuery("SELECT * FROM employees WHERE is_working_now = TRUE");
                }

                if (CleaningInfoController.areThereAnybodyWhoAreNotWorkingNow)
                {
                    while (resultSet.next())
                    {
                        if (CleaningInfoController.employeesListID.contains(resultSet.getInt(1)))
                        {
                            continue;
                        }

                        employeesTable.getItems().add(new Employees(resultSet.getInt(1),
                                                                    resultSet.getString(2),
                                                                    resultSet.getString(3),
                                                                    resultSet.getString(4),
                                                                    new Button("Открыть"),
                                                                    resultSet.getBytes(5),
                                                                    resultSet.getString(6),
                                                                    resultSet.getBoolean(7)));
                    }
                }
                else
                {
                    while (resultSet.next())
                    {
                        if (CleaningInfoController.employeesListID.contains(resultSet.getInt(1)))
                        {
                            continue;
                        }

                        employeesTable.getItems().add(new Employees(resultSet.getInt(1),
                                                                    resultSet.getString(2),
                                                                    resultSet.getString(3),
                                                                    resultSet.getString(4),
                                                                    new Button("Открыть"),
                                                                    resultSet.getBytes(5),
                                                                    resultSet.getString(6),
                                                                    true));
                    }
                }

                employeesTable.getSortOrder().clear();
                employeesTable.getSortOrder().add(employeesPositionColumn);
                employeesTable.sort();
            }
            else{
                TableColumn<Employees, String> employeesIsWorkingNowColumn = new TableColumn<>();
                employeesIsWorkingNowColumn.setReorderable(false);
                employeesIsWorkingNowColumn.setText("Трудоустроен(а)");
                employeesIsWorkingNowColumn.setCellValueFactory(cellData -> cellData.getValue().isIsWorkingNow()?new SimpleStringProperty("Да") :new SimpleStringProperty("Нет"));
                employeesTable.getColumns().add(employeesIsWorkingNowColumn);

                ps = DatabaseWorker.connection.prepareStatement("SELECT employees.* FROM employees " +
                                                                    "LEFT JOIN performed_cleanings ON employees.id_employee = performed_cleanings.id_employee " +
                                                                    "WHERE id_cleaning = ?");
                ps.setInt(1, id);

                resultSet = ps.executeQuery();

                while (resultSet.next())
                {
                    employeesTable.getItems().add(new Employees(resultSet.getInt(1),
                                                                resultSet.getString(2),
                                                                resultSet.getString(3),
                                                                resultSet.getString(4),
                                                                new Button("Открыть"),
                                                                resultSet.getBytes(5),
                                                                resultSet.getString(6),
                                                                resultSet.getBoolean(7)));
                }

                employeesTable.getSortOrder().clear();
                employeesTable.getSortOrder().add(employeesSurnameColumn);
                employeesTable.sort();
            }
        }
        else if (tableType == TableType.Visited)
        {
            TableView<Students> studentsTable = multifunctionalTable;

            TableColumn<Students, Integer> studentsIdColumn = new TableColumn<>();
            studentsIdColumn.setVisible(false);
            studentsIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
            studentsTable.getColumns().add(studentsIdColumn);

            addBaseStudentHeader(studentsTable);

            TableColumn<Students, Integer> studentsRoomNumberColumn = new TableColumn<>();
            studentsRoomNumberColumn.setReorderable(false);
            studentsRoomNumberColumn.setText("Номер комнаты");
            studentsRoomNumberColumn.setCellValueFactory(cellData -> cellData.getValue().roomNumberProperty().asObject());
            studentsTable.getColumns().add(studentsRoomNumberColumn);

            multifunctionalB.setText("Выбрать");

            resultSet = DatabaseWorker.statement.executeQuery("SELECT students.*, rooms.room_number FROM students " +
                                                                  "LEFT JOIN check_ins_outs ON students.id_student = check_ins_outs.id_student " +
                                                                  "LEFT JOIN rooms ON check_ins_outs.id_room = rooms.id_room " +
                                                                  "WHERE check_out_date >= CURRENT_DATE");

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
                                                          resultSet.getInt(9),
                                                          null, null));
            }

            Students student = null;
            for (var item : studentsTable.getItems()) {
                if (item.getId() == id){
                    student = item;
                    break;
                }
            }

            if (student == null && id != 0){
                studentsRoomNumberColumn.setPrefWidth(75.0);

                TableColumn<Students, LocalDate> studentsCheckOutDateColumn = new TableColumn<>();
                studentsCheckOutDateColumn.setReorderable(false);
                studentsCheckOutDateColumn.setText("Дата выселения");
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
                studentsTable.getColumns().add(studentsCheckOutDateColumn);

                resultSet = DatabaseWorker.statement.executeQuery("SELECT check_out_date FROM students " +
                                                                      "LEFT JOIN check_ins_outs ON students.id_student = check_ins_outs.id_student " +
                                                                      "WHERE check_out_date >= CURRENT_DATE");

                for (var item : studentsTable.getItems()) {
                    resultSet.next();
                    item.setCheckOutDate(resultSet.getDate(1).toLocalDate());
                }

                ps = DatabaseWorker.connection.prepareStatement("SELECT students.*, rooms.room_number, check_out_date FROM students " +
                                                                    "LEFT JOIN check_ins_outs ON students.id_student = check_ins_outs.id_student " +
                                                                    "LEFT JOIN rooms ON check_ins_outs.id_room = rooms.id_room " +
                                                                    "WHERE students.id_student = ?");

                ps.setInt(1, id);

                resultSet = ps.executeQuery();
                resultSet.next();

                student = new Students(resultSet.getInt(1),
                                       resultSet.getString(2),
                                       resultSet.getString(3),
                                       resultSet.getString(4),
                                       resultSet.getBoolean(6),
                                       resultSet.getDate(7).toLocalDate(),
                                       new Button("Открыть"),
                                       resultSet.getBytes(5),
                                       resultSet.getString(8),
                                       resultSet.getInt(9),
                                       null,
                                       resultSet.getDate(10).toLocalDate());

                studentsTable.getSortOrder().clear();
                studentsTable.getItems().add(student);
            }

            studentsTable.sort();

            if (id != 0){
                studentsTable.getSelectionModel().select(student);
                studentsTable.scrollTo(student);
            }
        }
        else if (tableType == TableType.Rooms)
        {
            TableView<Rooms> roomsTable = multifunctionalTable;

            TableColumn<Rooms, Integer> roomsIdColumn = new TableColumn<>();
            roomsIdColumn.setVisible(false);
            roomsIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

            TableColumn<Rooms, Integer> roomsRoomNumberColumn = new TableColumn<>();
            roomsRoomNumberColumn.setPrefWidth(75.0);
            roomsRoomNumberColumn.setReorderable(false);
            roomsRoomNumberColumn.setText("Номер комнаты");
            roomsRoomNumberColumn.setCellValueFactory(cellData -> cellData.getValue().roomNumberProperty().asObject());

            TableColumn<Rooms, Integer> roomsBedsColumn = new TableColumn<>();
            roomsBedsColumn.setReorderable(false);
            roomsBedsColumn.setText("Количество мест (всего)");
            roomsBedsColumn.setCellValueFactory(cellData -> cellData.getValue().bedsProperty().asObject());

            roomsTable.getColumns().addAll(roomsIdColumn, roomsRoomNumberColumn, roomsBedsColumn);

            multifunctionalB.setText("Выбрать");

            resultSet = DatabaseWorker.statement.executeQuery("SELECT * FROM rooms " +
                    "WHERE beds > (SELECT COUNT(*) FROM check_ins_outs " +
                    "WHERE check_ins_outs.id_room = rooms.id_room " +
                    "AND check_ins_outs.check_out_date >= CURRENT_DATE) " +
                    "AND (EXISTS (SELECT students.sex FROM check_ins_outs " +
                    "LEFT JOIN students ON check_ins_outs.id_student = students.id_student " +
                    "WHERE check_ins_outs.id_room = rooms.id_room " +
                    "AND check_ins_outs.check_out_date >= CURRENT_DATE " +
                    "AND students.sex IS NOT " + !boolData + ") " +
                    "OR NOT EXISTS (SELECT id_room FROM check_ins_outs " +
                    "WHERE check_ins_outs.id_room = rooms.id_room " +
                    "AND check_ins_outs.check_out_date >= CURRENT_DATE))");

            while (resultSet.next()){
                roomsTable.getItems().add(new Rooms(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3)));
            }

            Rooms room = null;
            for (var item : roomsTable.getItems()) {
                if (item.getId() == id){
                    room = item;
                    break;
                }
            }

            if (room == null && id != 0){
                ps = DatabaseWorker.connection.prepareStatement("SELECT * FROM rooms WHERE id_room = ?");
                ps.setInt(1, id);

                resultSet = ps.executeQuery();
                resultSet.next();

                room = new Rooms(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3));

                roomsTable.getItems().add(room);
            }

            roomsTable.getSortOrder().clear();
            roomsTable.getSortOrder().add(roomsRoomNumberColumn);
            roomsTable.sort();

            if (id != 0){
                roomsTable.getSelectionModel().select(room);
                roomsTable.scrollTo(room);
            }
        }
    }

    /**
     * Устанавливает базовые значения для окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     */
    public void setParams(TableState tableState, TableType tableType) throws SQLException {
        setParams(tableState, tableType, 0, false);
    }

    /**
     * Устанавливает значения для окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     * @param id идентификатор вызывающего объекта
     */
    public void setParams(TableState tableState, TableType tableType, int id) throws SQLException {
        setParams(tableState, tableType, id, false);
    }

    /**
     * Устанавливает значения для окна при его открытии. <br>
     * Использовать после получения экземпляра, иначе работа окна будет некорректной.
     * @param boolData какое-либо логическое значение
     */
    public void setParams(TableState tableState, TableType tableType, boolean boolData) throws SQLException {
        setParams(tableState, tableType, 0, boolData);
    }

    public void multifunctionalTable_CellDoubleClick(MouseEvent mouseEvent) throws SQLException, IOException {
        if (mouseEvent.getClickCount() == 2 && multifunctionalTable.getSelectionModel().getSelectedItem() != null){
            if (buttonsBox.isVisible()){
                multifunctionalB_Click();
            }
        }
    }

    /**
     * Загружает из базы данных информацию об инвентаре для выдачи.
     */
    private void inventoryAddSelect() throws SQLException {
        TableView<Inventory> inventoryTable = multifunctionalTable;

        ResultSet resultSet = DatabaseWorker.statement.executeQuery("SELECT id_inventory, inv_type, " +
                "inv_count-(SELECT COALESCE(SUM(iss_count), 0) FROM issued_inventory " +
                "WHERE inventory.id_inventory = issued_inventory.id_inventory " +
                "AND issued_inventory.turn_in_date >= CURRENT_DATE) " +
                "FROM inventory " +
                "WHERE (inv_count-(SELECT COALESCE(SUM(iss_count), 0) FROM issued_inventory " +
                "WHERE inventory.id_inventory = issued_inventory.id_inventory " +
                "AND issued_inventory.turn_in_date >= CURRENT_DATE))>0");

        while (resultSet.next()){
            inventoryTable.getItems().add(new Inventory(resultSet.getInt(1), resultSet.getString(2), 0, resultSet.getInt(3), 0));
        }

        inventoryTable.getSortOrder().clear();
        inventoryTable.getSortOrder().add(inventoryTable.getColumns().get(1));
        inventoryTable.sort();
    }

    /**
     * Загружает из базы данных информацию о выданном инвентаре.
     */
    private void inventoryUpdateSelect() throws SQLException {
        TableView<InventoryGiven> inventoryGivenTable = multifunctionalTable;

        PreparedStatement ps = DatabaseWorker.connection.prepareStatement("SELECT issued_inventory.*, inventory.inv_type FROM issued_inventory " +
                "LEFT JOIN inventory ON issued_inventory.id_inventory = inventory.id_inventory " +
                "LEFT JOIN students ON issued_inventory.id_student = students.id_student " +
                "WHERE issued_inventory.id_student = ?");
        ps.setInt(1, id);

        ResultSet resultSet = ps.executeQuery();

        while (resultSet.next()){
            inventoryGivenTable.getItems().add(new InventoryGiven(resultSet.getInt(3),
                                                                  resultSet.getString(7),
                                                                  resultSet.getInt(1),
                                                                  resultSet.getInt(6),
                                                                  resultSet.getDate(4).toLocalDate(),
                                                                  resultSet.getDate(5).toLocalDate()));
        }

        inventoryGivenTable.getSortOrder().clear();
        inventoryGivenTable.getSortOrder().add(inventoryGivenTable.getColumns().get(5));
        inventoryGivenTable.sort();
    }

    /**
     * Добавляет в многофункциональную таблицу стандартные поля с информацией о студенте.
     */
    private void addBaseStudentHeader(TableView<Students> table)
    {
        TableColumn<Students, String> studentsSurnameColumn = new TableColumn<>();
        studentsSurnameColumn.setPrefWidth(75.0);
        studentsSurnameColumn.setReorderable(false);
        studentsSurnameColumn.setText("Фамилия");
        studentsSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());

        TableColumn<Students, String> studentsNameColumn = new TableColumn<>();
        studentsNameColumn.setPrefWidth(75.0);
        studentsNameColumn.setReorderable(false);
        studentsNameColumn.setText("Имя");
        studentsNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<Students, String> studentsPatronymicColumn = new TableColumn<>();
        studentsPatronymicColumn.setPrefWidth(75.0);
        studentsPatronymicColumn.setReorderable(false);
        studentsPatronymicColumn.setText("Отчество");
        studentsPatronymicColumn.setCellValueFactory(cellData -> cellData.getValue().patronymicProperty());

        TableColumn<Students, String> studentsSexColumn = new TableColumn<>();
        studentsSexColumn.setPrefWidth(75.0);
        studentsSexColumn.setReorderable(false);
        studentsSexColumn.setText("Пол");
        studentsSexColumn.setCellValueFactory(cellData -> cellData.getValue().sexProperty());

        TableColumn<Students, LocalDate> studentsBirthdayColumn = new TableColumn<>();
        studentsBirthdayColumn.setPrefWidth(75.0);
        studentsBirthdayColumn.setReorderable(false);
        studentsBirthdayColumn.setText("Дата рождения");
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

        TableColumn<Students, Button> studentsPhotoColumn = new TableColumn<>();
        studentsPhotoColumn.setMaxWidth(85.0);
        studentsPhotoColumn.setMinWidth(85.0);
        studentsPhotoColumn.setPrefWidth(85.0);
        studentsPhotoColumn.setReorderable(false);
        studentsPhotoColumn.setResizable(false);
        studentsPhotoColumn.setSortable(false);
        studentsPhotoColumn.setText("Фотография");
        studentsPhotoColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper(cellData.getValue().getPhoto()));

        TableColumn<Students, String> studentsRecordBookNumberColumn = new TableColumn<>();
        studentsRecordBookNumberColumn.setPrefWidth(75.0);
        studentsRecordBookNumberColumn.setReorderable(false);
        studentsRecordBookNumberColumn.setText("Номер\nзачётной книжки");
        studentsRecordBookNumberColumn.setCellValueFactory(cellData -> cellData.getValue().recordBookNumberProperty());

        table.getColumns().addAll(studentsSurnameColumn, studentsNameColumn, studentsPatronymicColumn, studentsSexColumn, studentsBirthdayColumn, studentsPhotoColumn, studentsRecordBookNumberColumn);

        table.getSortOrder().clear();
        table.getSortOrder().add(studentsSurnameColumn);
    }

    /**
     * Обновляет данные таблицы об инвентаре.
     */
    private void inventoryReselect() throws SQLException {
        multifunctionalTable.getItems().clear();

        if (tableState == TableState.Add)
        {
            inventoryAddSelect();
        }
        else
        {
            inventoryUpdateSelect();
        }

        dialogResult = "OK";
    }

    /**
     * Обновляет данные таблицы о нарушениях.
     */
    private void violationsReselect() throws SQLException {
        TableView<Violations> violationsTable = multifunctionalTable;

        violationsTable.getItems().clear();

        PreparedStatement ps = DatabaseWorker.connection.prepareStatement("SELECT violations.* FROM violations " +
                "LEFT JOIN commited_violations ON violations.id_violation = commited_violations.id_violation " +
                "WHERE commited_violations.id_student = ?");
        ps.setInt(1, id);

        ResultSet resultSet = ps.executeQuery();

        while (resultSet.next()){
            violationsTable.getItems().add(new Violations(resultSet.getInt(1),
                                                          resultSet.getString(2),
                                                          resultSet.getString(3),
                                                          resultSet.getTimestamp(4).toLocalDateTime()));
        }

        violationsTable.getSortOrder().clear();
        violationsTable.getSortOrder().add(violationsTable.getColumns().get(3));
        violationsTable.sort();
    }

    /**
     * Открепляет нарушение от студента. <br>
     * Удаляет нарушение, если оно больше ни к кому не прикреплено.
     */
    public void detachB_Click() throws SQLException {
        if (multifunctionalTable.getSelectionModel().getSelectedItem() == null){
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Открепление нарушения");
        alert.setHeaderText(null);
        alert.setContentText("Вы действительно хотите открепить выбранное нарушение от данного студента?");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(new ButtonType("Да", ButtonType.YES.getButtonData()));
        alert.getButtonTypes().add(new ButtonType("Нет", ButtonType.NO.getButtonData()));
        alert.showAndWait();

        if (alert.getResult().getText().equals("Нет")){
            return;
        }

        TableView<Violations> violationsTable = multifunctionalTable;

        PreparedStatement ps = DatabaseWorker.connection.prepareStatement("SELECT COUNT(*) FROM commited_violations WHERE id_violation = ?");
        ps.setInt(1, violationsTable.getSelectionModel().getSelectedItem().getId());

        ResultSet resultSet = ps.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);

        if (count == 1){
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Открепление нарушения");
            alert.setHeaderText(null);
            alert.setContentText("Внимание: это единственная запись о совершении данного нарушения. При откреплении информация о нарушении также будет удалена. Продолжить?");
            alert.getButtonTypes().clear();
            alert.getButtonTypes().add(new ButtonType("Да", ButtonType.YES.getButtonData()));
            alert.getButtonTypes().add(new ButtonType("Нет", ButtonType.NO.getButtonData()));
            alert.showAndWait();

            if (alert.getResult().getText().equals("Нет")){
                return;
            }
        }

        ps = DatabaseWorker.connection.prepareStatement("DELETE FROM commited_violations WHERE id_violation = ? AND id_student = ?");

        ps.setInt(1, violationsTable.getSelectionModel().getSelectedItem().getId());
        ps.setInt(2, id);

        ps.executeUpdate();

        if (count == 1){
            ps = DatabaseWorker.connection.prepareStatement("DELETE FROM violations WHERE id_violation = ?");
            ps.setInt(1, violationsTable.getSelectionModel().getSelectedItem().getId());
            ps.executeUpdate();
        }

        Alert inf_alert = new Alert(Alert.AlertType.INFORMATION);
        inf_alert.setTitle("Открепление нарушения");
        inf_alert.setHeaderText(null);
        inf_alert.setContentText("Нарушение успешно откреплено");
        inf_alert.showAndWait();

        if (violationsTable.getItems().size()-1 != 0){
            violationsReselect();
        }
        else{
            dialogStage.close();
        }
    }

    public String dialogResult = "None";

    /**
     * Идентификатор полученного объекта.
     */
    public int returnID;

    /**
     * Совершает действие в зависимости от типа данных в таблице.
     */
    public void multifunctionalB_Click() throws SQLException, IOException {
        if (multifunctionalTable.getSelectionModel().getSelectedItem() == null){
            return;
        }

        PreparedStatement ps;
        ResultSet resultSet;

        if (tableType == TableType.Inventory){
            ps = DatabaseWorker.connection.prepareStatement("SELECT inv_count-(SELECT COALESCE(SUM(iss_count), 0) FROM issued_inventory WHERE id_inventory = ?) FROM inventory WHERE id_inventory = ?");

            if (tableState == TableState.Add){
                TableView<Inventory> inventoryTable = multifunctionalTable;

                ps.setInt(1, inventoryTable.getSelectionModel().getSelectedItem().getId());
                ps.setInt(2, inventoryTable.getSelectionModel().getSelectedItem().getId());
            }
            else{
                TableView<InventoryGiven> inventoryGivenTable = multifunctionalTable;

                ps.setInt(1, inventoryGivenTable.getSelectionModel().getSelectedItem().getInventoryID());
                ps.setInt(2, inventoryGivenTable.getSelectionModel().getSelectedItem().getInventoryID());
            }

            resultSet = ps.executeQuery();
            resultSet.next();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("inventoryGivenInfo.fxml"));

            Parent page = loader.load();
            Stage addStage = new Stage();
            addStage.initModality(Modality.WINDOW_MODAL);
            addStage.initOwner(Program.getPrimaryStage());

            Scene scene = new Scene(page);
            addStage.setScene(scene);
            addStage.setResizable(false);
            addStage.getIcons().add(new Image(Program.class.getResourceAsStream("quarantine.png")));

            InventoryGivenInfoController controller = loader.getController();

            if (tableState == TableState.Add){
                addStage.setTitle("Выдать инвентарь");

                TableView<Inventory> inventoryTable = multifunctionalTable;
                controller.setParams(inventoryTable.getSelectionModel().getSelectedItem().getId(), id, resultSet.getInt(1));
            }
            else{
                addStage.setTitle("Изменить сведения о выданном инвентаре");

                TableView<InventoryGiven> inventoryGivenTable = multifunctionalTable;
                controller.setParams(inventoryGivenTable.getSelectionModel().getSelectedItem(), id, resultSet.getInt(1));
            }

            controller.setAddStage(addStage);
            addStage.showAndWait();

            if (controller.dialogResult.equals("OK")){
                inventoryReselect();
                return;
            }
            else if (controller.dialogResult.equals("Abort")){
                if (multifunctionalTable.getItems().size()-1 != 0){
                    inventoryReselect();
                    return;
                }
                else{
                    this.dialogResult = "OK";
                }
            }
            else{
                return;
            }
        }
        else if (tableType == TableType.Violations){
            TableView<Violations> violationsTable = multifunctionalTable;

            if (tableState == TableState.Select){
                ps = DatabaseWorker.connection.prepareStatement("INSERT INTO commited_violations (id_student, id_violation) VALUES (?, ?)");

                ps.setInt(1, id);
                ps.setInt(2, violationsTable.getSelectionModel().getSelectedItem().getId());

                ps.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Добавление нарушения");
                alert.setHeaderText(null);
                alert.setContentText("Нарушение успешно добавлено");
                alert.showAndWait();
            }
            else{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("violationInfo.fxml"));

                Parent page = loader.load();
                Stage addStage = new Stage();
                addStage.setTitle("Изменить сведения о нарушении");
                addStage.initModality(Modality.WINDOW_MODAL);
                addStage.initOwner(Program.getPrimaryStage());

                Scene scene = new Scene(page);
                addStage.setScene(scene);
                addStage.setResizable(false);
                addStage.getIcons().add(new Image(Program.class.getResourceAsStream("quarantine.png")));

                ViolationInfoController controller = loader.getController();
                controller.setAddStage(addStage);
                controller.setParams(id, violationsTable.getSelectionModel().getSelectedItem());
                addStage.showAndWait();

                if (controller.dialogResult.equals("OK")){
                    violationsReselect();
                    return;
                }
                else if (controller.dialogResult.equals("Abort")){
                    if (multifunctionalTable.getItems().size()-1 != 0){
                        violationsReselect();
                        return;
                    }
                    else{
                        this.dialogResult = "OK";
                    }
                }
                else{
                    return;
                }
            }
        }
        else if (tableType == TableType.Cleaners){
            TableView<Employees> employeesTable = multifunctionalTable;
            for (var item : employeesTable.getSelectionModel().getSelectedItems()) {
                CleaningInfoController.employeesListID.add(item.getId());
            }

            var tmp = CleaningInfoController.employeesListID.stream().distinct().toList();
            CleaningInfoController.employeesListID = new ArrayList<>(tmp);

            dialogResult = "OK";
        }
        else if (tableType == TableType.Visited){
            TableView<Students> studentsTable = multifunctionalTable;
            returnID = studentsTable.getSelectionModel().getSelectedItem().getId();
            dialogResult = "OK";
        }
        else if (tableType == TableType.Rooms){
            TableView<Rooms> roomsTable = multifunctionalTable;
            returnID = roomsTable.getSelectionModel().getSelectedItem().getId();
            dialogResult = "OK";
        }

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
