package com.example.cryptofx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.HttpCookie;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MainController {
    @FXML
    private TextField passwordLogin;
    @FXML
    private TextField nicknameLogin;
    @FXML
    private Label errorLabel;

    @FXML
    protected void login(ActionEvent event) {
        if (nicknameLogin.getText().equals("") || passwordLogin.getText().equals("")) {
            errorLabel.setText("Please fill all fields");
        } else {
            Connection con = null;
            try {
                con = DriverManager.getConnection("jdbc:mysql://109.106.246.51:3306/u380254938_CryptoFX", "u380254938_betis", "VivaElBetis7");

                PreparedStatement ps = con.prepareStatement("SELECT * FROM user WHERE nickname = ? AND password = ?");

                ps.setString(1, nicknameLogin.getText());
                ps.setString(2, passwordLogin.getText());
                if (ps.executeQuery().next()) {
                    Stage stage2 = (Stage) this.errorLabel.getScene().getWindow();
                    stage2.close();

                    Parent root = FXMLLoader.load(getClass().getResource("main-view.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = new Stage(StageStyle.DECORATED);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    errorLabel.setText("User or password incorrect");
                }

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    @FXML
    protected void signupAccess(ActionEvent event) {
        Stage stage2 = (Stage) this.errorLabel.getScene().getWindow();
        stage2.close();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("signup-view.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Sign up | CryptoFX");
        stage.setScene(scene);
        stage.show();
    }
    protected void getCoin(ActionEvent event) {

    }
}