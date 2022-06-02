module ru.cw.dormitoryais {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens ru.cw.dormitoryais to javafx.fxml;
    exports ru.cw.dormitoryais;
    exports ru.cw.dormitoryais.Models;
    opens ru.cw.dormitoryais.Models to javafx.fxml;
    exports ru.cw.dormitoryais.Controllers;
    opens ru.cw.dormitoryais.Controllers to javafx.fxml;
}