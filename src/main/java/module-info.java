module com.example.cryptofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.cryptofx to javafx.fxml;
    exports com.example.cryptofx;
}