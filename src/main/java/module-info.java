module com.example.cryptofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires httpcore;
    requires httpclient;
    requires java.net.http;


    opens com.example.cryptofx to javafx.fxml;
    exports com.example.cryptofx;
}