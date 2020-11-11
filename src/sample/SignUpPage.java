package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import netUtil.NetworkUtil;

import java.io.IOException;

public class SignUpPage {
    private Stage stage;
    private Login client;
    private NetworkUtil networkUtil;

    @FXML
    public TextField userText;

    @FXML
    public PasswordField confirmPasswordText;

    @FXML
    public PasswordField passwordText;

    public void createNewAccount() throws IOException {
        String username = userText.getText();
        String password = passwordText.getText();
        String confirmedPassword = confirmPasswordText.getText();
        if ((!username.isEmpty() && !password.isEmpty() && !confirmedPassword.isEmpty()) && password.equals(confirmedPassword)) {
            String signUpMessage = "S#" + username + " " + password;
            networkUtil.write(signUpMessage);
            if (networkUtil.read().toString().equals("saved")) {
                successAlert();
                stage.close();
                client.loginPage();
            } else if (networkUtil.read().toString().equals("accountExists")){
                existingAlert();
            } else {
                failedToSave();
            }
        } else {
            alert();
        }
    }

    public void keyPressed(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            createNewAccount();
        }
    }

    private void alert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Incorrect Credentials");
        alert.setHeaderText("Incorrect Credentials");
        alert.setContentText("The username or password did not match.");
        alert.showAndWait();
    }

    private void successAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Account Created");
        alert.setHeaderText("Your account has been created");
        alert.setContentText("Please log in to continue");
        alert.showAndWait();
    }

    private void failedToSave() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Account creation failed");
        alert.setHeaderText("Your account was not created");
        alert.setContentText("Try again");
        alert.showAndWait();
    }

    private void existingAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Account already exists");
        alert.setHeaderText("Your account already exists");
        alert.setContentText("Please log in");
        alert.showAndWait();
    }

    public void setClient(Login login) {
        this.client = login;
    }

    public void setNetworkUtil(NetworkUtil networkUtil) {
        this.networkUtil = networkUtil;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
