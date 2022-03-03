package com.example.cryptofx;

import com.example.cryptofx.utilidades.CoinGecko;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import static com.example.cryptofx.utilidades.Const.*;

/**
 * Class that controls the main and login window
 */
public class MainController {

    @FXML
    private TextField passwordLogin;
    @FXML
    private TextField nicknameLogin;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField cryptoToFind;
    @FXML
    private TextArea showInfo;
    @FXML
    private CheckBox usd;
    @FXML
    private CheckBox eur;
    @FXML
    private CheckBox gbp;

    private static String nickname;

    /**
     * Method to login the user and show the main view
     * @param event
     */
    @FXML
    protected void login(ActionEvent event) {
        if (nicknameLogin.getText().equals("") || passwordLogin.getText().equals("")) {
            errorLabel.setText("Please fill all fields");
        } else {
            Connection con = null;
            try {
                con = DriverManager.getConnection(BD, USER, PASSWORD);
                PreparedStatement ps = con.prepareStatement("SELECT * FROM user WHERE nickname = ? AND password = ?");
                ps.setString(1, nicknameLogin.getText());
                ps.setString(2, passwordLogin.getText());

                if (ps.executeQuery().next()) {
                    Stage stage2 = (Stage) this.errorLabel.getScene().getWindow();
                    stage2.close();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Welcome to IntelliExchange!");
                    alert.setContentText("Hello " + nicknameLogin.getText() + "!\n");
                    alert.showAndWait();

                    nickname = nicknameLogin.getText();

                    Parent root = FXMLLoader.load(getClass().getResource("main-view.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = new Stage(StageStyle.DECORATED);
                    stage.setScene(scene);
                    Image ico = new Image("D:\\IdeaProjects\\CryptoFX\\src\\main\\java\\com\\example\\cryptofx\\img\\logo.png"); // No funciona con ruta relativa
                    stage.getIcons().add(ico);
                    stage.setTitle("Home | CryptoFX");
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

    /**
     * Link to the register view
     * @param event
     */
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
        Image ico = new Image("D:\\IdeaProjects\\CryptoFX\\src\\main\\java\\com\\example\\cryptofx\\img\\logo.png"); // No funciona con ruta relativa
        stage.getIcons().add(ico);
        stage.show();
    }

    /**
     * Method to find the crypto currency
     * @param event
     */
    @FXML
    protected void findPrice(ActionEvent event) {
        CoinGecko api = new CoinGecko();
        String crypto = cryptoToFind.getText().toLowerCase().replace(" ","-");
        String fiat = "";
        String price = "";
        showInfo.setText("");
        ArrayList<String> cryptos = (ArrayList<String>) api.getCoinsList();

        boolean exists = false;
        for (String s: cryptos) {
            if (crypto.equals(s)) {
                exists = true;
                break;
            }
        }

        if (crypto.equals("") || (!usd.isSelected() && !eur.isSelected() && !gbp.isSelected()) || !exists) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Please fill the fields");
            alert.showAndWait();

        } else {
            if (usd.isSelected()) {
                fiat = CURRENCY_USD;
                price += format(crypto) + " --> " + api.getCoinPrice(crypto, fiat) + "$\n";
                insertHistory(crypto, fiat, api.getCoinPrice(crypto, fiat), nickname);
            }

            if (eur.isSelected()) {
                fiat = CURRENCY_EUR;
                price += format(crypto) + " --> " + api.getCoinPrice(crypto, fiat) + "€\n";
                insertHistory(crypto, fiat, api.getCoinPrice(crypto, fiat), nickname);
            }

            if (gbp.isSelected()) {
                fiat = CURRENCY_GBP;
                price += format(crypto) + " --> " + api.getCoinPrice(crypto, fiat) + "£\n";
                insertHistory(crypto, fiat, api.getCoinPrice(crypto, fiat), nickname);
            }
            showInfo.appendText(showInfo.getText() + price);
        }
    }

    /**
     * Method to insert the history of the user
     * @param crypto
     * @param fiat
     * @param price
     * @param nickname
     */
    @FXML
    private void insertHistory(String crypto, String fiat, String price, String nickname) {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://109.106.246.51:3306/u380254938_CryptoFX", "u380254938_betis", "VivaElBetis7");
            PreparedStatement ps = con.prepareStatement("INSERT INTO history (currency, fiat, price, nickname) VALUES (?, ?, ?, ?)");
            ps.setString(1, crypto);
            ps.setString(2, fiat);
            ps.setString(3, price);
            ps.setString(4, nickname);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get a csv file with the history of all the users
     * @param event
     */
    @FXML
    private void getReport(ActionEvent event) {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://109.106.246.51:3306/u380254938_CryptoFX", "u380254938_betis", "VivaElBetis7");
            PreparedStatement ps = con.prepareStatement("SELECT * FROM history");
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            StringBuilder csv = new StringBuilder();
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) {
                    csv.append(",");
                }
                csv.append(rsmd.getColumnName(i));
            }

            csv.append("\n");
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) {
                        csv.append(",");
                    }
                    csv.append(rs.getString(i));
                }
                csv.append("\n");
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Report");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                FileWriter writer = new FileWriter(file);
                writer.write(csv.toString());
                writer.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("Please select a file");
                alert.showAndWait();

            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to format the crypto string
     * @param var
     * @return String
     */
    private String format(String var) {
        var = var.replace("-"," ");
        return var.substring(0, 1).toUpperCase() + var.substring(1);
    }

}
