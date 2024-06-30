module perpus {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;

    opens perpus to javafx.fxml;
    exports perpus;
}
