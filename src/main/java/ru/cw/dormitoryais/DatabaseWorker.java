package ru.cw.dormitoryais;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;
import java.util.Properties;

/**
 * Обеспечивает работу с СУБД.
 */
public class DatabaseWorker {
    private enum ConnectionResult
    {
        Successful,
        FbException,
        WrongStructure,
        NoFile
    }

    /**
     * Пытается загрузить информацию из базы данных и сообщает, если что-то идёт не так.
     */
    public static boolean load(String path) throws SQLException {
        ConnectionResult connectionResult = Connect(path);

        switch (connectionResult)
        {
            case Successful:
                return true;
            case WrongStructure:
                ErrorMessage.show("Выбранный файл базы данных не подходит для данного приложения. Пожалуйста, загрузите подходящий файл.");
                break;
            default:
                break;
        }

        return false;
    }

    /**
     * Хранит информацию о соединении с базой данных.
     */
    public static Connection connection;
    /**
     * Переменная для записи простого SQL-запроса.
     */
    public static Statement statement;
    /**
     * Хранит путь к файлу базы данных.
     */
    public static String savedPath;

    /**
     * На самом деле пытается загрузить информацию из базы данных и сообщает, если что-то идёт не так. <br>
     * Инициализирует переменные connection и statement. <br>
     * Проверяет соответствие структуры файла базы данных требуемой.
     */
    private static ConnectionResult Connect(String path) throws SQLException {
        while (true)
        {
            try
            {
                Properties props = new Properties();
                props.setProperty("user", "SYSDBA");
                props.setProperty("password", "masterkey");
                props.setProperty("encoding", "win1251");

                connection = DriverManager.getConnection("jdbc:firebirdsql://localhost:3050/"+path, props);
                statement = connection.createStatement();
                savedPath = path;

                break;
            }
            catch (Exception e)
            {
                ErrorMessage.show(e.getMessage());

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Ошибка при запросе к базе данных");
                alert.setHeaderText(null);
                alert.setContentText("Повторить попытку?");
                alert.getButtonTypes().clear();
                alert.getButtonTypes().add(new ButtonType("Повторить", ButtonType.OK.getButtonData()));
                alert.getButtonTypes().add(new ButtonType("Отмена", ButtonType.CANCEL.getButtonData()));
                alert.showAndWait();

                if (alert.getResult().getText().equals("Отмена"))
                {
                    return ConnectionResult.FbException;
                }
            }
        }

        ResultSet resultSet = statement.executeQuery("SELECT TRUE FROM RDB$RELATIONS WHERE RDB$RELATION_NAME = 'STUDENTS'");

        boolean condition;
        if (!resultSet.next()){
            condition = false;
        }
        else{
            condition = resultSet.getBoolean(1);
        }

        if (condition)
        {
            resultSet = statement.executeQuery("select R.RDB$FIELD_POSITION, R.RDB$FIELD_NAME, F.RDB$FIELD_LENGTH " +
                                                   "from RDB$FIELDS F, RDB$RELATION_FIELDS R " +
                                                   "where F.RDB$FIELD_NAME = R.RDB$FIELD_SOURCE and R.RDB$SYSTEM_FLAG = 0 and RDB$RELATION_NAME = 'STUDENTS' " +
                                                   "order by R.RDB$FIELD_POSITION, R.RDB$RELATION_NAME");

            while (resultSet.next())
            {
                if (resultSet.getString(1).equals("0") && resultSet.getString(2).replace(" ", "").equals("ID_STUDENT"))
                {
                    continue;
                }
                else if (resultSet.getString(1).equals("1") && resultSet.getString(2).replace(" ", "").equals("SURNAME") && resultSet.getString(3).equals("30"))
                {
                    continue;
                }
                else if (resultSet.getString(1).equals("2") && resultSet.getString(2).replace(" ", "").equals("NAME") && resultSet.getString(3).equals("30"))
                {
                    continue;
                }
                else if (resultSet.getString(1).equals("3") && resultSet.getString(2).replace(" ", "").equals("PATRONYMIC") && resultSet.getString(3).equals("30"))
                {
                    continue;
                }
                else if (resultSet.getString(1).equals("4") && resultSet.getString(2).replace(" ", "").equals("PHOTO"))
                {
                    continue;
                }
                else if (resultSet.getString(1).equals("5") && resultSet.getString(2).replace(" ", "").equals("SEX"))
                {
                    continue;
                }
                else if (resultSet.getString(1).equals("6") && resultSet.getString(2).replace(" ", "").equals("BIRTHDAY"))
                {
                    continue;
                }
                else if (resultSet.getString(1).equals("7") && resultSet.getString(2).replace(" ", "").equals("RECORD_BOOK_NUMBER") && resultSet.getString(3).equals("10"))
                {
                    continue;
                }
                else
                {
                    return ConnectionResult.WrongStructure;
                }
            }

            return ConnectionResult.Successful;
        }
        else
        {
            return ConnectionResult.WrongStructure;
        }
    }

    /**
     * Закрывает соединение с базой данных.
     * Обязательно использовать при закрытии приложения.
     */
    public static void closeConnection() throws SQLException {
        if (connection != null)
        {
            connection.close();
            statement.close();
        }
    }
}
