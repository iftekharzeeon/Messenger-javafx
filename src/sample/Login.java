package sample;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.*;
import javafx.stage.Stage;
import netUtil.NetworkUtil;

import java.io.IOException;
import java.util.ArrayList;

public class Login {

    public static boolean activeClients = false;
    public static boolean flag = false;
    public static NetworkUtil networkUtil;
    Stage stage;

    Label label = new Label("Welcome to My Messenger");
    Button logoutButton = new Button("Log Out");
    Button refreshButton = new Button("Refresh");
    Line line = new Line();
    Label label2 = new Label("Active Clients");
    static ListView<String> listView = new ListView<>();
    static ListView<String> list = new ListView<>();
    static TextFlow textFlow = new TextFlow();
    TextArea textArea = new TextArea();
    Button sendButton = new Button("Send");
    String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public Login(Stage stage) throws IOException {
        this.stage = stage;

        loginPage();
    }

    public void loginPage() throws IOException {

        networkUtil = new NetworkUtil("192.168.0.107", 9990);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("loginPage.fxml"));
        Parent root = loader.load();

        LoginPage loginController = loader.getController();
        loginController.setClient(this);
        loginController.setNetworkUtil(networkUtil);
        loginController.setStage(stage);


        stage.setTitle("Messenger");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

    public void groupChat() {

        AnchorPane pane = new AnchorPane();
        ScrollPane scrollPane = new ScrollPane();

        layoutDesign(pane, scrollPane, label, logoutButton, refreshButton, line, label2, listView, textFlow, textArea, sendButton);

        //readThread();

        TaskThread thr = new TaskThread();
        Thread thread = new Thread(thr);
        thread.start();

        textArea.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                e.consume();
                if (e.isShiftDown()) {
                    textArea.appendText(System.getProperty("line.separator"));
                } else {
                    if (!textArea.getText().isEmpty()) {
                        String message = textArea.getText();
                        String chatMessage = "G#" + message;
                        networkUtil.write(chatMessage);
                        textArea.clear();
                    }
                }
            }
        });

        sendButton.setOnAction(e -> {
            String message = textArea.getText();
            String chatMessage = "G#" + message;
            networkUtil.write(chatMessage);
            textArea.clear();
        });

        refreshButton.setOnAction(e -> {
            listView.getItems().clear();
            networkUtil.write("R");
        });

        logoutButton.setOnAction(e -> {
            //thread.interrupt();
            logout();
        });

        pane.getChildren().addAll(label, logoutButton, refreshButton, line, label2, listView, scrollPane, textArea, sendButton);
        Scene scene = new Scene(pane, 800, 500);
        scene.getStylesheets().add("Styles.css");

        stage.setTitle(title + "- Group");
        stage.setScene(scene);
        stage.show();
    }

    public void signUp() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("SignUpPage.fxml"));
        Parent root = loader.load();

        SignUpPage signUpController = loader.getController();
        signUpController.setClient(this);
        signUpController.setNetworkUtil(networkUtil);
        signUpController.setStage(stage);

        stage.setTitle("Messenger");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

    public void option() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Option.fxml"));
        Parent root = loader.load();

        Option optionController = loader.getController();
        optionController.setClient(this);
        optionController.setNetworkUtil(networkUtil);
        optionController.setStage(stage);
        Scene scene = new Scene(root, 800, 500);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public void directMessage(String selectedItem) {
        stage.close();

        ScrollPane scrollPane = new ScrollPane();
        AnchorPane pane = new AnchorPane();

        Button backButton = new Button("Back");
        backButton.setLayoutX(52);
        backButton.setLayoutY(36);
        backButton.setPrefWidth(76);
        backButton.setPrefHeight(44);
        backButton.setFont(new Font(19));

        layoutDesign(pane, scrollPane, label, logoutButton, refreshButton, line, label2, listView, textFlow, textArea, sendButton);

        Thread thread = new Thread(new TaskThread());
        thread.start();

        //readThread();

        textArea.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                e.consume();
                if (e.isShiftDown()) {
                    textArea.appendText(System.getProperty("line.separator"));
                } else {
                    if (!textArea.getText().isEmpty()) {
                        String message = "D#" + selectedItem + "#" + textArea.getText();
                        networkUtil.write(message);
                        textArea.clear();
                    }
                }
            }
        });

        sendButton.setOnAction(e -> {
            String message = "D#" + selectedItem + "#" + textArea.getText();
            networkUtil.write(message);
            textArea.clear();
        });

        refreshButton.setOnAction(e -> networkUtil.write("R"));

        backButton.setOnAction(e -> {
            flag = true;
            networkUtil.write("DB#" + selectedItem + "#" + "Client left the chat");
//            try {
//                Thread.currentThread().wait();
//            } catch (InterruptedException interruptedException) {
//                interruptedException.printStackTrace();
//            }

            textFlow.getChildren().clear();
            listView.getItems().clear();
//            try {
//                thr.wait();
//            } catch (InterruptedException interruptedException) {
//                interruptedException.printStackTrace();
//            stage.close();
//
//            }
            //activeClients = true;

            thread.interrupt();
            stage.close();
            try {
                activeClients();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        logoutButton.setOnAction(e -> {
//            thread.interrupt();
//            thr.stop();
            logout();
        });

        pane.getChildren().addAll(label, logoutButton, backButton, refreshButton, line, label2, listView, scrollPane, textArea, sendButton);
        Scene scene = new Scene(pane, 800, 500);
        scene.getStylesheets().add("Styles.css");

        stage.setTitle(title + "- Direct");
        stage.setScene(scene);
        stage.show();
    }

    public void activeClients() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ActiveClients.fxml"));
        Parent root = loader.load();

        ActiveClients activeClientsController = loader.getController();
        activeClientsController.setClient(this);
        activeClientsController.setNetworkUtil(networkUtil);
        activeClientsController.setStage(stage);

        stage.setTitle(title);
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

    private void layoutDesign(AnchorPane pane,ScrollPane scrollPane, Label label, Button logoutButton, Button refreshButton, Line line, Label label2, ListView<String> listView, TextFlow textFlow, TextArea textArea, Button sendButton) {

        pane.setPrefWidth(800);
        pane.setPrefHeight(500);

        label.setLayoutX(413);
        label.setLayoutY(44);
        label.setGraphicTextGap(7);
        label.setFont(new Font("Algerian", 24));
        AnchorPane.setRightAnchor(label, 64.0);

        logoutButton.setLayoutX(152);
        logoutButton.setLayoutY(36);
        logoutButton.setPrefHeight(44);
        logoutButton.setPrefWidth(83);
        logoutButton.setFont(new Font(16));
        AnchorPane.setLeftAnchor(logoutButton, 152.0);

        refreshButton.setLayoutX(52);
        refreshButton.setLayoutY(444);
        refreshButton.setPrefHeight(44);
        refreshButton.setPrefWidth(93);
        refreshButton.setFont(new Font(18));
        AnchorPane.setLeftAnchor(refreshButton, 52.0);
        AnchorPane.setBottomAnchor(refreshButton, 12.0);

        line.setEndX(700);
        line.setLayoutX(101);
        line.setLayoutY(107);
        line.setStartX(-100);
        line.setScaleX(6.0);

        label2.setLayoutX(52);
        label2.setLayoutY(120);
        label2.setFont(new Font("Arial Narrow", 21));
        label2.setUnderline(true);

        listView.setLayoutX(52);
        listView.setLayoutY(152);
        listView.setPrefWidth(200);
        listView.setPrefHeight(283);
        listView.getStyleClass().add("list-view-background");
        AnchorPane.setBottomAnchor(listView, 65.0);
        AnchorPane.setTopAnchor(listView, 152.0);

        scrollPane.setPrefViewportHeight(315);
        scrollPane.setPrefViewportWidth(418.5);
        scrollPane.setPrefHeight(425);
        scrollPane.setPrefWidth(330);
        scrollPane.setLayoutX(364);
        scrollPane.setLayoutY(120);
        AnchorPane.setBottomAnchor(scrollPane, 63.0);
        AnchorPane.setTopAnchor(scrollPane, 120.0);
        AnchorPane.setLeftAnchor(scrollPane, 364.0);
        AnchorPane.setRightAnchor(scrollPane, 16.0);

        textFlow.setLayoutX(364);
        textFlow.setLayoutY(120);
        textFlow.setLineSpacing(5);
        textFlow.getStyleClass().add("textflow-background");
        textFlow.prefHeightProperty().bind(scrollPane.heightProperty().subtract(5));
        textFlow.prefWidthProperty().bind(scrollPane.widthProperty().subtract(5));
        scrollPane.setContent(textFlow);

        textArea.setLayoutX(379);
        textArea.setLayoutY(444);
        textArea.setPrefWidth(314);
        textArea.setPrefHeight(44);
        textArea.setMaxWidth(314);
        AnchorPane.setRightAnchor(textArea, 112.5);
        AnchorPane.setBottomAnchor(textArea, 12.0);
        AnchorPane.setLeftAnchor(textArea, 379.0);

        sendButton.setLayoutY(444);
        sendButton.setLayoutX(701);
        sendButton.setPrefHeight(44);
        sendButton.setPrefWidth(93);
        sendButton.setFont(new Font(20));
        AnchorPane.setRightAnchor(sendButton, 7.5);
        AnchorPane.setBottomAnchor(sendButton, 12.0);
    }

    private void logout(){
        networkUtil.write("B#");
        textFlow.getChildren().clear();
        listView.getItems().clear();
        stage.close();
        //networkUtil.closeConnection();
        try {
            loginPage();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void activeClients2() {
        AnchorPane pane = new AnchorPane();
        pane.setPrefHeight(500);
        pane.setPrefWidth(800);

        Label label = new Label("Active Clients");

        Button refreshButton = new Button("Refresh");
        refreshButton.setLayoutY(87);
        refreshButton.setLayoutX(689);
        refreshButton.setOnAction(e -> networkUtil.write("R"));

        list.setLayoutX(301);
        list.setLayoutY(211);
        list.setPrefHeight(200);
        list.setPrefWidth(200);

        Button startChat = new Button("Start Chat");
        startChat.setLayoutX(352);
        startChat.setLayoutY(430);
        startChat.setOnAction(e -> {
            activeClients = false;
            directMessage(list.getSelectionModel().getSelectedItem());
        });
        pane.getChildren().addAll(label, list, startChat, refreshButton);

        Scene scene = new Scene(pane, 800,500);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }
}

class TaskThread implements Runnable{


    @Override
    public void run() {
        System.out.println("helllo");
        while (!Thread.interrupted()) {
            System.out.println("Not interrupted");
            System.out.println("Check");
            if (Login.networkUtil != null) {
                Object o = Login.networkUtil.read();
                if (o instanceof String) {
                    String received = o.toString();
                    String respond = received + "\n";
                    Text text = new Text(respond);
                    if (!Login.flag) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println(Thread.currentThread() + "for msg");
                                Login.textFlow.getChildren().add(text);
                            }
                        });
                    }
                    else {
                        Login.flag = false;
                    }
                } else if (o instanceof ArrayList){
                    ArrayList<String> activeClientsList = (ArrayList<String>) o;
                    if (Login.activeClients) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                //System.out.println(Thread.activeCount() + "for list");
                                for (String s : activeClientsList) {
                                    if (!Login.list.getItems().contains(s)) {
                                        Login.list.getItems().add(s);
                                    }
                                }
                            }
                        });
                    }
                    else {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println(Thread.activeCount() + "for list");
                                for (String s : activeClientsList) {
                                    if (!Login.listView.getItems().contains(s)) {
                                        Login.listView.getItems().add(s);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }
        System.out.println(" interrupted");
       //System.out.println("Outside the loop");
    }
}