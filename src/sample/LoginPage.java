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

public class LoginPage {


    private Stage stage;
    private Login client;
    private NetworkUtil networkUtil;

    public LoginPage(){
        System.out.println("This is a constrtuctor");
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("HHEE");
            }
        };
        new Thread(r).start();
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setClient(Login client) {
        this.client = client;
    }

    public void setNetworkUtil(NetworkUtil networkUtil) {
        this.networkUtil = networkUtil;
    }

    @FXML
    private TextField userText;

    @FXML
    private PasswordField passwordText;

    public void loginButton() throws IOException {
        sendingInfo();
    }

    public void createNewAccount(ActionEvent event) throws IOException {
        stage.close();
        client.signUp();
    }

    public void keyPressed(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            sendingInfo();
        }
    }

    private void alert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Incorrect Credentials");
        alert.setHeaderText("Incorrect Credentials");
        alert.setContentText("The username and password you provided is not correct.");
        alert.showAndWait();
    }

    private void sendingInfo() throws IOException {
        String username = userText.getText();
        String password = passwordText.getText();

        if (!username.isEmpty() && !password.isEmpty()) {
            String loginMessage = "L#" + username + " " + password;
            networkUtil.write(loginMessage);
            String respond = (String) networkUtil.read();
            switch (respond) {
                case "success":
                    stage.close();
                    client.setTitle("Messenger- " + username);
                    client.option();
                    break;
                case "failed":
                    alert();
                    break;
                case "exist":
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Already Logged In");
                    alert.setHeaderText("Account Already Logged In");
                    alert.setContentText("You Are Already Logged In!");
                    alert.showAndWait();
                    break;
            }
        }
        else {
            alert();
        }
    }
}