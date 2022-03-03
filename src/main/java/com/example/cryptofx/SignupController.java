package com.example.cryptofx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class that handles the signup process
 */
public class SignupController {
    @FXML
    private TextField nameSignup;

    @FXML
    private TextField lastNameSignup;

    @FXML
    private TextField nicknameSignup;

    @FXML
    private PasswordField passwordSignup;

    @FXML
    private Label errorLabel;

    /**
     * Method that handles the signup process
     * @param event
     */
    @FXML
    private void signup(ActionEvent event) {
        if (nameSignup.getText().isEmpty() || lastNameSignup.getText().isEmpty() || nicknameSignup.getText().isEmpty() || passwordSignup.getText().isEmpty()) {
            errorLabel.setText("Please fill all the fields");
        } else {

            Connection con = null;
            try {
                con = DriverManager.getConnection("jdbc:mysql://109.106.246.51:3306/u380254938_CryptoFX", "u380254938_betis", "VivaElBetis7");
                PreparedStatement ps = con.prepareStatement("SELECT * FROM user WHERE nickname = ?");
                ps.setString(1, nicknameSignup.getText());
                if (!ps.executeQuery().next()) {
                    ps = con.prepareStatement("INSERT INTO user(name, last_name, nickname, password) VALUES (?, ?, ?, ?)");

                    ps.setString(1, nameSignup.getText());
                    ps.setString(2, lastNameSignup.getText());
                    ps.setString(3, nicknameSignup.getText());
                    ps.setString(4, passwordSignup.getText());
                    if (ps.executeUpdate() == 1) {
                        Stage stage2 = (Stage) this.nameSignup.getScene().getWindow();
                        stage2.close();

                        Stage stage;
                        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
                        Scene scene = new Scene(root);
                        stage = new Stage(StageStyle.DECORATED);
                        stage.setScene(scene);
                        stage.show();


                    } else {
                        errorLabel.setText("Error creating user");
                    }
                } else {
                    errorLabel.setText("Nickname already in use");
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

    /**
     * Method that handles the back button
     * @param event
     */
    public void loginAccess(ActionEvent event) {
        Stage stage2 = (Stage) this.nameSignup.getScene().getWindow();
        stage2.close();

        Stage stage;
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Log in | CryptoFX");
        stage.setScene(scene);
        stage.show();
    }
}
