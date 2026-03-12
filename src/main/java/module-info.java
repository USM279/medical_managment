module com.sau.med {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;

    opens com.sau.med to javafx.fxml;
    exports com.sau.med;
    exports com.sau.med.controller;
    opens com.sau.med.controller to javafx.fxml;
}
